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
package org.soulwing.cdi.properties.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link BooleanPropertyConverter}.
 *
 * @author Carl Harris
 */
public class BooleanPropertyConverterTest {

  private BooleanPropertyConverter converter = new BooleanPropertyConverter();
  
  @Test
  public void testSupportsPrimitive() throws Exception {
    assertThat(converter.supports(boolean.class), is(true));
  }

  @Test
  public void testSupportsWrapper() throws Exception {
    assertThat(converter.supports(Boolean.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    assertThat((Boolean) converter.convert("false", null), is(false));
    assertThat((Boolean) converter.convert("true", null), is(true));
  }

}
