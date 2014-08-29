/*
 * File created on Aug 29, 2014 
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
package org.soulwing.cdi.properties.extension;

import org.soulwing.cdi.properties.spi.PropertyConverter.Context;

/**
 * A simple immutable property conversion context.
 *
 * @author Carl Harris
 */
class ConversionContext implements Context {
  
  private final Class<?> targetType;
  private final PropertyValueConverter converter;
  
  /**
   * Constructs a new instance.
   * @param targetType
   * @param converter
   */
  public ConversionContext(Class<?> targetType,
      PropertyValueConverter converter) {
    this.targetType = targetType;
    this.converter = converter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getTargetType() {
    return targetType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convert(String value, Class<?> type) {
    try {
      return converter.convert(value, type);
    }
    catch (UnsupportedTypeException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }

}
