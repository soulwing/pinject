/*
 * File created on Aug 27, 2014 
 *
 * Copyright (c) 2014 Virginia Polytechnic Institute and State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.soulwing.cdi.properties.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.soulwing.cdi.properties.spi.Optional;
import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyValueResolver} that delegates to providers located using
 * the {@link ServiceLoader} mechanism.
 *
 * @author Carl Harris
 */
class DelegatingPropertyValueResolver implements PropertyValueResolver {

  private static final Logger logger = Logger.getLogger( 
      DelegatingPropertyValueResolver.class.getName());
  
  private final List<PropertyResolver> resolvers = new ArrayList<>();
  
  private final Map<String, String> cache = 
      new ConcurrentHashMap<String, String>();
  
  /**
   * Constructs a new instance.
   */
  DelegatingPropertyValueResolver() {
  }

  public void init() throws Exception {
    for (PropertyResolver resolver : 
      ServiceLoader.load(PropertyResolver.class)) {
      if (resolver instanceof Optional 
          && !((Optional) resolver).isAvailable()) {
        logger.info("resolver not available: " + resolver.getClass().getName());
        continue;
      }
      resolver.init();
      resolvers.add(resolver);
    }
    Collections.sort(resolvers, new Comparator<PropertyResolver>() {
      @Override
      public int compare(PropertyResolver a, PropertyResolver b) {
        return b.getPriority() - a.getPriority();
      }      
    });
    if (logger.isLoggable(Level.FINE)) {
      for (PropertyResolver resolver : resolvers) {
        logger.fine(String.format("resolver: priority=%d type=%s",
            resolver.getPriority(),
            resolver.getClass().getName()));
      }
    }
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(String name) {
    String value = getCachedValue(name);
    if (value == null) {
      for (PropertyResolver resolver : resolvers) {
        value = resolve(name, resolver);
        if (value != null) break;
      }
    }
    if (value != null) {
      cache(name, value);
    }
    return value;
  }

  private String resolve(String name, PropertyResolver resolver) {
    String value = resolver.resolve(name);
    if (value != null) {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine(String.format("resolved %s=%s (%s)", name, value,
            resolver.getClass().getSimpleName()));
      }
    }
    return value;
  }

  private String getCachedValue(String name) {
    String value = cache.get(name);
    if (value != null && logger.isLoggable(Level.FINEST)) {
      logger.finest(String.format("resolved %s=%s (cached)", name, value));
    }
    return value;
  }

  private void cache(String name, String value) {
    cache.put(name, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    for (PropertyResolver resolver : resolvers) {
      try {
        resolver.destroy();
      }
      catch (Exception ex) {
        System.err.println(resolver.getClass().getName() 
            + ": failed to release resolver resources");
        ex.printStackTrace(System.err);
      }
    } 
  }
  
}
