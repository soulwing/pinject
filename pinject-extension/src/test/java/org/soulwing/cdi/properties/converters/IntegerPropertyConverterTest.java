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
package org.soulwing.cdi.properties.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link IntegerPropertyConverter}.
 *
 * @author Carl Harris
 */
public class IntegerPropertyConverterTest {

  private final IntegerPropertyConverter converter = new IntegerPropertyConverter();
  
  @Test
  public void testSupportsPrimitive() throws Exception {
    assertThat(converter.supports(int.class), is(true));
  }

  @Test
  public void testSupportsWrapper() throws Exception {
    assertThat(converter.supports(Integer.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    assertThat((Integer) converter.convert(
        Integer.toString(Integer.MIN_VALUE), null), 
        is(equalTo(Integer.MIN_VALUE)));
    assertThat((Integer) converter.convert("0", null), 
        is(equalTo(0)));
    assertThat((Integer) converter.convert(
        Integer.toString(Integer.MAX_VALUE), null), 
        is(equalTo(Integer.MAX_VALUE)));
  }

}
