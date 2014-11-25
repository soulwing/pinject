/*
 * File created on Aug 30, 2014 
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

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyResolver} that consults system properties.
 *
 * @author Carl Harris
 */
public class SystemPropertiesResolver implements PropertyResolver {

  public static final int PRIORITY = -1;

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
    return PRIORITY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(String name) {
    return System.getProperty(name);
  }

}
