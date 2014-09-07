/*
 * File created on Sep 7, 2014 
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
package org.soulwing.cdi.properties.resolvers;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * A {@link PropertyResolver} that resolves values by considering each
 * property name to be a path to a {@link beans.properties} resource on
 * the class path.
 *
 * @author Carl Harris
 */
public class ClassPathBeanPropertiesResolver
    extends AbstractPackagePathPropertyResolver {

  public static final int PRIORITY = -10;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws Exception {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() throws Exception {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    // TODO Auto-generated method stub
    return PRIORITY;
  }

  protected PropertiesSet loadPropertiesSet(PropertyRef ref) 
      throws IOException {
    Enumeration<URL> locations = Thread.currentThread()
        .getContextClassLoader().getResources(
            ref.getPath(BeansProperties.NAME));
    PropertiesSet propertiesSet = new PropertiesSet();
    propertiesSet.load(locations);
    return propertiesSet;
  }
 
}
