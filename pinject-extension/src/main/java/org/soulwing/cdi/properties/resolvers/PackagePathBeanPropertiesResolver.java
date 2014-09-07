/*
 * File created on Aug 30, 2014 
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
import java.util.HashMap;
import java.util.Map;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyResolver} that treats each property name as a fully
 * qualified name to {@code beans.properties} file in a package on the
 * classpath.
 *
 * @author Carl Harris
 */
public class PackagePathBeanPropertiesResolver implements PropertyResolver {

  public static final int PRIORITY = -10;
  
  private final Map<String, PropertiesSet> pathCache =
      new HashMap<>();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws Exception {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() throws Exception {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return PRIORITY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(String name) {
    try {
      return resolve(new PropertyRef(name));
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private String resolve(PropertyRef ref) throws IOException {
    PropertiesSet propertiesSet = fetchPropertiesSet(ref);
    String value = propertiesSet.getProperty(ref.getName());
    if (value != null || ref.isRootPath()) {
      return value;
    }
    return resolve(ref.getParent());
  }

  private PropertiesSet fetchPropertiesSet(PropertyRef ref)
      throws IOException {
    PropertiesSet propertiesSet = pathCache.get(ref.getPath());
    if (propertiesSet == null) {
      propertiesSet = loadPropertiesSet(ref);
      pathCache.put(ref.getPath(), propertiesSet);
    }
    return propertiesSet;
  }

  private PropertiesSet loadPropertiesSet(PropertyRef ref) 
      throws IOException {
    Enumeration<URL> locations = Thread.currentThread()
        .getContextClassLoader().getResources(
            ref.getPath(BeansProperties.NAME));
    PropertiesSet propertiesSet = new PropertiesSet();
    propertiesSet.load(locations);
    return propertiesSet;
  }
 
}
