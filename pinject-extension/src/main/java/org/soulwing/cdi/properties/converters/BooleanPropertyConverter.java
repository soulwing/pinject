/*
 * File created on Aug 27, 2014 
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

/**
 * A {@link PropertyConverter} for boolean primitive or wrapper types.
 *
 * @author Carl Harris
 */
public class BooleanPropertyConverter extends AbstractPropertyConverter {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(Class<?> type) {
    return boolean.class.isAssignableFrom(type)
        || Boolean.class.isAssignableFrom(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convert(String value, Context context) {
    return Boolean.valueOf(value);
  }

}
