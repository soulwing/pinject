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
 * Unit tests for {@link DoublePropertyConverter}.
 *
 * @author Carl Harris
 */
public class DoublePropertyConverterTest {

  private final DoublePropertyConverter converter = new DoublePropertyConverter();
  
  @Test
  public void testSupportsPrimitive() throws Exception {
    assertThat(converter.supports(double.class), is(true));
  }

  @Test
  public void testSupportsWrapper() throws Exception {
    assertThat(converter.supports(Double.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    assertThat((Double) converter.convert(
        Double.toString(Double.NEGATIVE_INFINITY), null), 
        is(equalTo(Double.NEGATIVE_INFINITY)));
    assertThat((Double) converter.convert(
        Double.toString(0.0), null),
        is(equalTo(0.0)));
    assertThat((Double) converter.convert(
        Double.toString(Math.E), null),
        is(equalTo(Math.E)));
    assertThat((Double) converter.convert(
        Double.toString(Math.PI), null),
        is(equalTo(Math.PI)));
    assertThat((Double) converter.convert(
        Double.toString(Double.NaN), null), 
        is(equalTo(Double.NaN)));
    assertThat((Double) converter.convert(
        Double.toString(Double.POSITIVE_INFINITY), null), 
        is(equalTo(Double.POSITIVE_INFINITY)));
  }

}
