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

import org.junit.Test;

/**
 * Unit tests for {@link PropertyRef}.
 *
 * @author Carl Harris
 */
public class PropertyRefTest {

  @Test
  public void testNameWithOneSegment() {
    PropertyRef ref = new PropertyRef("name");
    assertThat(ref.isRootPath(), is(true));
    assertThat(ref.getPath().isEmpty(), is(true));
    assertThat(ref.getName(), is(equalTo("name")));
  }

  @Test
  public void testNameWithTwoSegments() {
    PropertyRef ref = new PropertyRef("parent.child");
    assertThat(ref.isRootPath(), is(false));
    assertThat(ref.getPath(), is(equalTo("parent/")));
    assertThat(ref.getName(), is(equalTo("child")));
    PropertyRef parent = ref.getParent();
    assertThat(parent.isRootPath(), is(true));
    assertThat(parent.getPath().isEmpty(), is(true));
    assertThat(parent.getName(), is(equalTo("parent.child")));
  }

  @Test
  public void testNameWithThreeSegments() {
    PropertyRef ref = new PropertyRef("parent.child.grandchild");
    assertThat(ref.isRootPath(), is(false));
    assertThat(ref.getPath(), is(equalTo("parent/child/")));
    assertThat(ref.getName(), is(equalTo("grandchild")));
    PropertyRef parent = ref.getParent();
    assertThat(parent.isRootPath(), is(false));
    assertThat(parent.getPath(), is(equalTo("parent/")));
    assertThat(parent.getName(), is(equalTo("child.grandchild")));
  }

  @Test
  public void testGetResourcePathAtRoot() throws Exception {
    PropertyRef ref = new PropertyRef("name");
    assertThat(ref.isRootPath(), is(true));
    assertThat(ref.getPath("someResource"), is(equalTo("someResource")));
  }

  @Test
  public void testGetResourcePath() throws Exception {
    PropertyRef ref = new PropertyRef("parent.child");
    assertThat(ref.isRootPath(), is(false));
    assertThat(ref.getPath("someResource"), is(equalTo("parent/someResource")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalEmptyName() {
    new PropertyRef("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNameWithFewerSegmentsThanExpected() {
    new PropertyRef("name", 2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetParentFromRoot() {
    PropertyRef ref = new PropertyRef("name");
    assertThat(ref.isRootPath(), is(true));
    ref.getParent();
  }

}
