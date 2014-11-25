/*
 * File created on Aug 27, 2014 
 *
 * Copyright (c) 2014 Carl Harris, Jr.
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
package org.soulwing.cdi.properties.resolvers;

import java.net.URL;
import java.util.logging.Logger;

import org.soulwing.cdi.properties.converters.UrlPropertyConverter;
import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A resolver that looks up a JNDI environment variable and (if found)
 * interprets it as a space and/or comma delimited list of URLs to 
 * properties files to consult (in the given order) for name resolution.
 *
 * @author Carl Harris
 */
public class JndiEnvUrlPathBeanPropertiesResolver implements PropertyResolver {

  private static final Logger logger = Logger.getLogger(
      JndiEnvUrlPathBeanPropertiesResolver.class.getName());
  
  public static final String BINDING = 
      "java:comp/env/beans.properties.root";

  public static final int PRIORITY = -5;
  
  private PropertyResolver delegate = new NoOpPropertyResolver(PRIORITY);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws Exception {
    UrlPropertyConverter converter = new UrlPropertyConverter();
    Object boundValue = JndiObjectLocator.getInstance().lookup(BINDING);
    if (boundValue != null) {
      URL location = (URL) converter.convert(boundValue.toString(), null);
      delegate = new UrlPathBeanPropertiesResolver(PRIORITY, location);
      logger.info("root location for beans properties: " + location);
    }    
    delegate.init();
  }

  @Override
  public void destroy() throws Exception {
    delegate.destroy();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return delegate.getPriority();
  }

  @Override
  public String resolve(String name) {
    return delegate.resolve(name);
  }

}
