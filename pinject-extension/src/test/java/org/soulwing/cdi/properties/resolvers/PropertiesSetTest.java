/*
 * File created on Aug 28, 2014 
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link PropertiesSetTest}.
 *
 * @author Carl Harris
 */
public class PropertiesSetTest {

  private final PropertiesSet propertiesSet = new PropertiesSet();
  
  @Before
  public void setUp() throws Exception {
    propertiesSet.load(getClass().getClassLoader().getResource(
        getClass().getSimpleName() + "/set1.properties"));
    propertiesSet.load(getClass().getClassLoader().getResource(
        getClass().getSimpleName() + "/set2.properties"));
  }

  
  @Test
  public void testGetProperty() throws Exception {
    // defined only in set1.properties
    assertThat(propertiesSet.getProperty("property1"), is(equalTo("set1")));
    // defined only in set2.properties
    assertThat(propertiesSet.getProperty("property2"), is(equalTo("set2")));
    // defined in both -- set1 should take precedence
    assertThat(propertiesSet.getProperty("property3"), is(equalTo("set1")));
  }
  
}

