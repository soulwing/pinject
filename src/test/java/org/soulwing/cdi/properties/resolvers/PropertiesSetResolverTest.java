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

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link PropertiesSetResolverTest}.
 *
 * @author Carl Harris
 */
public class PropertiesSetResolverTest {

  private PropertiesSetResolver resolver;
  
  @Before
  public void setUp() throws Exception {
    Vector<URL> v = new Vector<URL>();
    v.add(getClass().getClassLoader().getResource(
        getClass().getSimpleName() + "/set1.properties"));
    v.add(getClass().getClassLoader().getResource(
        getClass().getSimpleName() + "/set2.properties"));
    resolver = new SimplePropertiesSetResolver(v.elements());
    resolver.init();
  }

  @After
  public void tearDown() throws Exception {
    resolver.destroy();
  }
  
  @Test
  public void testResolve() throws Exception {
    // defined only in set1.properties
    assertThat(resolver.resolve("property1"), is(equalTo("set1")));
    // defined only in set2.properties
    assertThat(resolver.resolve("property2"), is(equalTo("set2")));
    // defined in both -- set1 should take precedence
    assertThat(resolver.resolve("property3"), is(equalTo("set1")));
  }
  
  static class SimplePropertiesSetResolver extends PropertiesSetResolver {

    private final Enumeration<URL> locations;
    
    public SimplePropertiesSetResolver(Enumeration<URL> locations) {
      this.locations = locations;
    }

    @Override
    public void init() throws Exception {
      loadProperties(locations);
    }

    @Override
    public int getPriority() {
      return 0;
    }
    
  }

}

