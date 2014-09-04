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
package org.soulwing.cdi.properties.resolvers;

import java.io.IOException;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A resolver that looks for {@code META-INF/beans.properties} resources on the
 * classpath and uses all such properties resources to attempt to resolve bean
 * properties.
 *
 * @author Carl Harris
 */
public class MetaInfBeanPropertiesResolver implements PropertyResolver {

  public static final String META_INF_BEANS_PROPERTIES = 
      "META-INF/" + BeansProperties.NAME;
  
  public static final int PRIORITY = -9;

  private final PropertiesSet propertiesSet = new PropertiesSet();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws IOException {
    propertiesSet.load(Thread.currentThread().getContextClassLoader()
        .getResources(META_INF_BEANS_PROPERTIES));
  }

  @Override
  public void destroy() throws Exception {
    propertiesSet.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public String resolve(String name) {
    return propertiesSet.getProperty(name);
  }

}
