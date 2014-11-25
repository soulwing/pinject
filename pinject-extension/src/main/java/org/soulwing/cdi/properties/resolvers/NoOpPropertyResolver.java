/*
 * File created on Sep 8, 2014 
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
 * A {@link PropertyResolver} that does nothing at all.
 * <p>
 * This exists to allow resolvers that delegate to another resolver to
 * always have a delegate.
 *
 * @author Carl Harris
 */
class NoOpPropertyResolver implements PropertyResolver {

  private final int priority;
  
  /**
   * Constructs a new instance.
   * @param priority
   */
  public NoOpPropertyResolver(int priority) {
    this.priority = priority;
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
  public int getPriority() {
    return priority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(String name) {
    return null;
  }

}
