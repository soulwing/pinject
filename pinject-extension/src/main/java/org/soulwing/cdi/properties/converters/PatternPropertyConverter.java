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

import org.soulwing.cdi.properties.spi.PropertyConverter;

import java.util.regex.Pattern;

/**
 * A {@link PropertyConverter} for Pattern types.
 *
 * @author Michael Irwin
 */
public class PatternPropertyConverter extends AbstractPropertyConverter {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(Class<?> type) {
    return Pattern.class.isAssignableFrom(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convert(String value, Context context) {
    return Pattern.compile(value);
  }

}
