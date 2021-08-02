/*
 * File created on Aug 2, 2021
 *
 * Copyright (c) 2021 Carl Harris, Jr
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

import java.time.Duration;

import org.junit.Test;

/**
 * Unit tests for {@link DurationPropertyConverter}.
 *
 * @author Carl Harris
 */
public class DurationPropertyConverterTest {

  private final DurationPropertyConverter converter =
      new DurationPropertyConverter();

  @Test
  public void testGetName() throws Exception {
    assertThat(converter.getName(),
        is(equalTo(DurationPropertyConverter.class.getName())));
  }

  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(Duration.class), is(true));
    assertThat(converter.supports(Object.class), is(false));
  }

  @Test
  public void testConvert() throws Exception {
    assertThat(converter.convert("PT5M", null),
        is(equalTo(Duration.ofMinutes(5))));

    assertThat(converter.convert("P5D", null),
        is(equalTo(Duration.ofDays(5))));
  }


}
