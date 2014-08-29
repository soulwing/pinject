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

/**
 * A resolver that looks for {@code META-INF/beans.properties} resources on the
 * classpath and uses all such properties resources to attempt to resolve bean
 * properties.
 *
 * @author Carl Harris
 */
public class MetaInfBeanPropertiesResolver 
    extends PropertiesSetResolver {

  public static final String BEANS_PROPERTIES = "META-INF/beans.properties";
  
  public static final int PRIORITY = -10;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws IOException {
    loadProperties(Thread.currentThread().getContextClassLoader()
        .getResources(BEANS_PROPERTIES));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return PRIORITY;
  }

}
