/*
 * File created on Sep 7, 2014 
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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyResolver} that resolves values by considering each
 * property name to be a path to a {@code beans.properties} resource on
 * the class path.
 *
 * @author Carl Harris
 */
public class ClassPathBeanPropertiesResolver
    extends AbstractPackagePathPropertyResolver {

  private static final Logger logger = Logger.getLogger(
      ClassLoaderPropertiesSetLoader.class.getName());

  public static final int PRIORITY = -10;

  /**
   * Constructs a new instance.
   */
  public ClassPathBeanPropertiesResolver() {
    super(PRIORITY, new ClassLoaderPropertiesSetLoader());
  }

  /**
   * A {@link PropertiesSetLoader} that loads {@code beans.properties} using
   * the class loader.
   */
  static class ClassLoaderPropertiesSetLoader implements PropertiesSetLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesSet load(PropertyRef ref) throws IOException {
      ClassLoader classLoader = (Thread.currentThread().getContextClassLoader() != null) ?
          Thread.currentThread().getContextClassLoader() :
          getClass().getClassLoader();


      Enumeration<URL> locations = classLoader.getResources(
              ref.getPath(BeansProperties.NAME));

      if (!locations.hasMoreElements()) {
        logger.fine(
          "found no `" + BeansProperties.NAME + "` resources on classloader "
              + classLoader);
      }

      PropertiesSet propertiesSet = new PropertiesSet();
      propertiesSet.load(locations);
      return propertiesSet;
    }
  
  }

}
