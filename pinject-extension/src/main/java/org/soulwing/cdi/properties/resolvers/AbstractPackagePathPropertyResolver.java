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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * An abstract base for {@link PropertyResolvers} that treat property names
 * as a qualified path name to a properties resource.
 *
 * @author Carl Harris
 */
public abstract class AbstractPackagePathPropertyResolver 
    implements PropertyResolver {

  private final Map<String, PropertiesSet> pathCache =
      new ConcurrentHashMap<>();
  
  private final int priority;
  private final PropertiesSetLoader loader;
  
  /**
   * Constructs a new instance.
   * @param priority
   * @param loader
   */
  protected AbstractPackagePathPropertyResolver(int priority,
      PropertiesSetLoader loader) {
    this.priority = priority;
    this.loader = loader;
  }

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
  public final int getPriority() {
    return priority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String resolve(String name) {
    try {
      return resolve(new PropertyRef(name));
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Resolves the value for the property specified by the given reference.
   * @param ref property reference
   * @return property value
   * @throws IOException
   */
  private String resolve(PropertyRef ref) throws IOException {
    PropertiesSet propertiesSet = fetchPropertiesSet(ref);
    String value = propertiesSet.getProperty(ref.getName());
    if (value != null || ref.isRootPath()) {
      return value;
    }
    return resolve(ref.getParent());
  }

  /**
   * Fetches the properties set for the path specified by the given property
   * reference loading it from the underlying provider subtype as needed.
   * @param ref property reference
   * @return properties set
   * @throws IOException
   */
  private PropertiesSet fetchPropertiesSet(PropertyRef ref)
      throws IOException {
    PropertiesSet propertiesSet = pathCache.get(ref.getPath());
    if (propertiesSet == null) {
      propertiesSet = loader.load(ref);
      pathCache.put(ref.getPath(), propertiesSet);
    }
    return propertiesSet;
  }

}
