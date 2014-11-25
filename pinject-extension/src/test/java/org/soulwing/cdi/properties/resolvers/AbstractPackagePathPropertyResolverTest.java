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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * An abstract base for testing {@link AbstractPackagePathPropertyResolver}
 * subtypes.
 *
 * @author Carl Harris
 */
public abstract class AbstractPackagePathPropertyResolverTest
    <T extends AbstractPackagePathPropertyResolver> {

  protected T resolver;

  @Before
  public void setUp() throws Exception {
    resolver = newResolver();
    resolver.init();
  }

  @After
  public void tearDown() throws Exception {
    resolver.destroy();
  }
  
  protected abstract T newResolver();

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
