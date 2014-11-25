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
 * Unit tests for {@link CharacterPropertyConverterTest}.
 *
 * @author Carl Harris
 */
public class CharacterPropertyConverterTest {

  private static final String STRING_OF_LENGTH_1 = "C";
  private static final String STRING_WITH_WHITESPACE = "    C     ";
  private static final String STRING_WITH_ESCAPED_SPACE = "\\ ";
  private static final String STRING_OF_LENGTH_GT_1 = "CE";
  private static final String EMPTY_STRING = "";

  private CharacterPropertyConverter converter = 
      new CharacterPropertyConverter();
  
  @Test
  public void testSupportsPrimitive() throws Exception {
    assertThat(converter.supports(char.class), is(true));
  }

  @Test
  public void testSupportsWrapper() throws Exception {
    assertThat(converter.supports(Character.class), is(true));
  }

  @Test
  public void testConvertStringOfLength1() throws Exception {
    assertThat((Character) converter.convert(
        STRING_OF_LENGTH_1, null),
        is(equalTo(STRING_OF_LENGTH_1.charAt(0))));
  }

  @Test
  public void testConvertStringWithWhitespace() throws Exception {
    assertThat((Character) converter.convert(
        STRING_WITH_WHITESPACE, null),
        is(equalTo(STRING_WITH_WHITESPACE.trim().charAt(0))));
  }

  @Test
  public void testConvertStringWithEscapedSpace() throws Exception {
    assertThat((Character) converter.convert(
        STRING_WITH_ESCAPED_SPACE, null), is(equalTo(' ')));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringOfLengthGreaterThan1() throws Exception {
    converter.convert(STRING_OF_LENGTH_GT_1, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertEmptyString() throws Exception {
    converter.convert(EMPTY_STRING, null);
  }


}
