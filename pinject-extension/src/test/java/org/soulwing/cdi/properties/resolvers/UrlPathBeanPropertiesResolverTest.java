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

import java.net.URL;

/**
 * Unit tests for {@link UrlPathBeanPropertiesResolver}.
 *
 * @author Carl Harris
 */
public class UrlPathBeanPropertiesResolverTest 
    extends AbstractPackagePathPropertyResolverTest<UrlPathBeanPropertiesResolver> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected UrlPathBeanPropertiesResolver newResolver() {
    URL location = Thread.currentThread().getContextClassLoader()
        .getResource(getClass().getSuperclass().getSimpleName());
    if (location == null) {
      throw new IllegalStateException("can't find test resources");
    }
    return new UrlPathBeanPropertiesResolver(-1, location);
  }
  
}
