/*
 * File created on Sep 20, 2016
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

import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for {@link PatternPropertyConverter}.
 *
 * @author Michael Irwin
 */
public class PatternPropertyConverterTest {

  private final PatternPropertyConverter converter = new PatternPropertyConverter();
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(Pattern.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    String value = "test-\\d+-value";
    Pattern pattern = (Pattern) converter.convert(value, null);
    assertThat(pattern.matcher("test-1234-value").matches(), is(true));
  }

}
