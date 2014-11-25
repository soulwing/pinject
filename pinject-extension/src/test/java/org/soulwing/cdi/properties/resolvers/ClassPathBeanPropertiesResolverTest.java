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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soulwing.cdi.properties.support.ClassLoaderUtil;

/**
 * Unit tests for {@link ClassPathBeanPropertiesResolver}.
 *
 * @author Carl Harris
 */
public class ClassPathBeanPropertiesResolverTest 
    extends AbstractPackagePathPropertyResolverTest<ClassPathBeanPropertiesResolver> {

  private ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();
  
  @Before
  public void setUp() throws Exception {
    classLoaderUtil.setUp(getClass().getSuperclass());
    super.setUp();
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
    classLoaderUtil.tearDown();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ClassPathBeanPropertiesResolver newResolver() {
    return new ClassPathBeanPropertiesResolver();
  }

  @Test
  public void testClassLoaderSetUp() throws Exception {
    assertThat(Thread.currentThread().getContextClassLoader()
        .getResource(BeansProperties.NAME), is(not(nullValue())));
  }

}
