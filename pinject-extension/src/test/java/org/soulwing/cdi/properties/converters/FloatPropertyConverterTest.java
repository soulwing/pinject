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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link FloatPropertyConverter}.
 *
 * @author Carl Harris
 */
public class FloatPropertyConverterTest {

  private FloatPropertyConverter converter = new FloatPropertyConverter();
  
  @Test
  public void testSupportsPrimitive() throws Exception {
    assertThat(converter.supports(float.class), is(true));
  }

  @Test
  public void testSupportsWrapper() throws Exception {
    assertThat(converter.supports(Float.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    assertThat((Float) converter.convert(
        Float.toString(Float.NEGATIVE_INFINITY), null), 
        is(equalTo(Float.NEGATIVE_INFINITY)));
    assertThat((Float) converter.convert(
        Float.toString(0.0f), null),
        is(equalTo(0.0f)));
    assertThat((Float) converter.convert(
        Float.toString((float) Math.E), null),
        is(equalTo((float) Math.E)));
    assertThat((Float) converter.convert(
        Float.toString((float) Math.PI), null),
        is(equalTo((float) Math.PI)));
    assertThat((Float) converter.convert(
        Float.toString(Float.NaN), null), 
        is(equalTo(Float.NaN)));
    assertThat((Float) converter.convert(
        Float.toString(Float.POSITIVE_INFINITY), null), 
        is(equalTo(Float.POSITIVE_INFINITY)));
  }

}
