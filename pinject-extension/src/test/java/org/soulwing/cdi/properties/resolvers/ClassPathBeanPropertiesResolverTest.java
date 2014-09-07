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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
public class ClassPathBeanPropertiesResolverTest {

  private ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();
  private ClassPathBeanPropertiesResolver resolver =
      new ClassPathBeanPropertiesResolver();
  
  @Before
  public void setUp() throws Exception {
    classLoaderUtil.setUp(getClass());
  }

  @After
  public void tearDown() throws Exception {
    classLoaderUtil.tearDown();
  }

  @Test
  public void testClassLoaderSetUp() throws Exception {
    assertThat(Thread.currentThread().getContextClassLoader()
        .getResource(BeansProperties.NAME), is(not(nullValue())));
  }
  
  @Test
  public void testGetPropertyWithNoRecursion() throws Exception {
    assertThat(resolver.resolve("rootProperty"), 
        is(equalTo("rootProperty")));
    assertThat(resolver.resolve("NO_SUCH_PROPERTY"), 
        is(nullValue()));
    assertThat(resolver.resolve("parent.parentProperty"), 
        is(equalTo("parentProperty")));
    assertThat(resolver.resolve("parent.child.childProperty"), 
        is(equalTo("childProperty")));
    assertThat(resolver.resolve("parent.child.grandchild.grandchildProperty"), 
        is(equalTo("grandchildProperty")));
  }
  
  @Test
  public void testGetPropertyWithRecursion() throws Exception {
    assertThat(resolver.resolve("parent.child.grandchild.parentProperty"), 
        is(equalTo("parentOfGrandchildProperty")));
    assertThat(resolver.resolve("parent.child.grandchild.grandparentProperty"), 
        is(equalTo("grandparentOfGrandchildProperty")));
    assertThat(resolver.resolve("parent.child.grandchild.greatGrandparentProperty"),
        is(equalTo("greatGrandparentOfGrandchildProperty")));
    assertThat(resolver.resolve("parent.child.grandchild.NO_SUCH_PROPERTY"),
        is(nullValue()));
  }

}
