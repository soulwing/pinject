/*
 * File created on Aug 29, 2014 
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
 * A {@link PropertyConverter} that supports conversion of enumeration
 * types.
 *
 * @author Carl Harris
 */
public class EnumPropertyConverter extends AbstractPropertyConverter {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(Class<?> type) {
    return Enum.class.isAssignableFrom(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convert(String value, Context context)
      throws IllegalArgumentException {
    value = value.trim();
    Enum[] names = (Enum[]) context.getTargetType().getEnumConstants();
    for (Enum name : names) {
      if (name.name().equals(value) || name.toString().equals(value)) return name;
    }

    throw new IllegalArgumentException("value must be one of " 
        + allowedNames(names));
  }
  
  private String allowedNames(Object[] names) {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for (int i = 0; i < names.length; i++) {
      sb.append(names[i]);
      if (i < names.length - 1) {
        sb.append(", ");
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

}
