/*
 * File created on Jul 9, 2015
 *
 * Copyright (c) 2015 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.cdi.properties.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import javax.ejb.ScheduleExpression;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ScheduleExpressionPropertyConverter}.
 *
 * @author Carl Harris
 */
public class ScheduleExpressionPropertyConverterTest {

  
  private final ScheduleExpressionPropertyConverter converter =
      new ScheduleExpressionPropertyConverter();
  
  @Before
  public void setUp() throws Exception {
    assertThat(converter.isAvailable(), is(true));
  }
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(ScheduleExpression.class), is(true));
  }

  @Test
  public void testConvertWithSixTokens() throws Exception {
    ScheduleExpression expression = (ScheduleExpression)
        converter.convert("1 2 3 4 5 6", null);
    assertThat(expression.getSecond(), is(equalTo("1")));
    assertThat(expression.getMinute(), is(equalTo("2")));
    assertThat(expression.getHour(), is(equalTo("3")));
    assertThat(expression.getDayOfMonth(), is(equalTo("4")));
    assertThat(expression.getMonth(), is(equalTo("5")));
    assertThat(expression.getDayOfWeek(), is(equalTo("6")));
  }

  @Test
  public void testConvertWithSevenTokens() throws Exception {
    ScheduleExpression expression = (ScheduleExpression)
        converter.convert("1 2 3 4 5 6 7", null);
    assertThat(expression.getSecond(), is(equalTo("1")));
    assertThat(expression.getMinute(), is(equalTo("2")));
    assertThat(expression.getHour(), is(equalTo("3")));
    assertThat(expression.getDayOfMonth(), is(equalTo("4")));
    assertThat(expression.getMonth(), is(equalTo("5")));
    assertThat(expression.getDayOfWeek(), is(equalTo("6")));
    assertThat(expression.getYear(), is(equalTo("7")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertWithoutEnoughTokens() throws Exception {
    converter.convert("1 2 3 4 5", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertWithTooManyTokens() throws Exception {
    converter.convert("1 2 3 4 5 6 7 8", null);
  }

}
