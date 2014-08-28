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
import static org.hamcrest.Matchers.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MetaInfBeanPropertiesResolver}.
 *
 * @author Carl Harris
 */
public class MetaInfBeansPropertiesResolverTest {

  private MetaInfBeanPropertiesResolver resolver =
      new MetaInfBeanPropertiesResolver();
  
  @Before
  public void setUp() throws Exception {
    resolver.init();
  }

  @After
  public void tearDown() throws Exception {
    resolver.destroy();
  }
  
  @Test
  public void testResolve() throws Exception {
    // this property is defined in src/test/resources/META-INF/beans.properties
    assertThat(resolver.resolve("testProperty"), is(equalTo("testValue")));
    // this property is not defined in src/test/resources/META-INF/beans.properties
    assertThat(resolver.resolve("DOES_NOT_EXIST"), is(nullValue()));
  }
}
