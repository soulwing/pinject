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
import java.util.ServiceLoader;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyValueResolver} that delegates to providers located using
 * the {@link ServiceLoader} mechanism.
 *
 * @author Carl Harris
 */
class DelegatingPropertyValueResolver implements PropertyValueResolver {

  private final List<PropertyResolver> resolvers = new ArrayList<>();
  
  /**
   * Constructs a new instance.
   */
  DelegatingPropertyValueResolver() {
  }

  public void init() throws Exception {
    for (PropertyResolver resolver : 
      ServiceLoader.load(PropertyResolver.class)) {
      resolver.init();
      resolvers.add(resolver);
    }
    Collections.sort(resolvers, new Comparator<PropertyResolver>() {
      @Override
      public int compare(PropertyResolver a, PropertyResolver b) {
        return b.getPriority() - a.getPriority();
      }      
    });

  }
  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(String name) {
    for (PropertyResolver resolver : resolvers) {
      String value = resolver.resolve(name);
      if (value != null) return value;
    }
    return null;
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
