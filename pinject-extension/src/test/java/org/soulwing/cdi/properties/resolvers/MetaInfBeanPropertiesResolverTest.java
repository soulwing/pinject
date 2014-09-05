/*
 * File created on Aug 28, 2014 
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
 * Tests for {@link MetaInfBeanPropertiesResolver}.
 *
 * @author Carl Harris
 */
public class MetaInfBeanPropertiesResolverTest {

  private static final String META_INF_PROPERTY = "testMetaInfProperty";

  private ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();
  
  private MetaInfBeanPropertiesResolver resolver =
      new MetaInfBeanPropertiesResolver();
  
  @Before
  public void setUp() throws Exception {
    classLoaderUtil.setUp(getClass());
    resolver.init();
  }

  @After
  public void tearDown() throws Exception {
    resolver.destroy();
    classLoaderUtil.tearDown();
  }
  
  @Test
  public void testClassLoaderSetUp() throws Exception {
    assertThat(Thread.currentThread().getContextClassLoader()
        .getResource(MetaInfBeanPropertiesResolver.META_INF_BEANS_PROPERTIES), 
        is(not(nullValue())));
  }

  @Test
  public void testResolve() throws Exception {
    assertThat(resolver.resolve(META_INF_PROPERTY), 
        is(equalTo(META_INF_PROPERTY)));
    assertThat(resolver.resolve("DOES_NOT_EXIST"), is(nullValue()));
  }
}
