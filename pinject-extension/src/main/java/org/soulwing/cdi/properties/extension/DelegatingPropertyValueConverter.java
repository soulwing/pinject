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
package org.soulwing.cdi.properties.extension;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.soulwing.cdi.properties.spi.Optional;
import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyValueConverter} that delegates to providers located using
 * the {@link ServiceLoader} mechanism.
 *
 * @author Carl Harris
 */
class DelegatingPropertyValueConverter implements PropertyValueConverter {

  private static final Logger logger =
      Logger.getLogger(DelegatingPropertyValueConverter.class.getName());
  
  private final Set<PropertyConverter> converters = new LinkedHashSet<>();
  
  private final Map<String, PropertyConverter> converterMap = 
      new LinkedHashMap<>();
  
  private final PropertyValueResolver resolver;
  
  /**
   * Constructs a new instance.
   * @param resolver property value resolver
   */
  DelegatingPropertyValueConverter(PropertyValueResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void init() throws Exception {
    final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    if (tccl != null) {
      logger.fine("loading converters from thread context class loader " + tccl);
      loadConverters(tccl);
    }
    else {
      logger.fine("loading converters from default class loader "
          + getClass().getClassLoader());
      loadConverters(getClass().getClassLoader());
    }
  }

  private void loadConverters(ClassLoader classLoader) {
    final ClassLoader previousTccl =
        Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(classLoader);
      for (PropertyConverter converter :
          ServiceLoader.load(PropertyConverter.class)) {
        if (converter instanceof Optional
            && !((Optional) converter).isAvailable()) {
          logger.info("converter not available: " + converter.getClass().getName());
          continue;
        }

        String name = converter.getName();
        String className = converter.getClass().getName();
        if (name == null) {
          name = converter.getClass().getName();
        }

        if (logger.isLoggable(Level.FINE)) {
          if (!name.equals(className)) {
            logger.fine(String.format("converter: %s (%s)", name, className));
          }
          else {
            logger.fine(String.format("converter: %s", className));
          }
        }

        converters.add(converter);
        converterMap.put(converter.getName(), converter);
      }
    }
    finally {
      Thread.currentThread().setContextClassLoader(previousTccl);
    }
  }

  @Override
  public Object convert(String value, Class<?> type)
      throws UnsupportedTypeException {
    for (PropertyConverter converter : converters) {
      if (!converter.supports(type)) continue;
      return converter.convert(value, new ConversionContext(type, resolver, this));
    }
    throw new UnsupportedTypeException();
  }

  @Override
  public Object convert(String converterName, String value, Class<?> type)
      throws NoSuchConverterException, UnsupportedTypeException {
    PropertyConverter converter = converterMap.get(converterName);
    if (converter == null) {
      throw new NoSuchConverterException();
    }
    if (!(converter.supports(type))) {
      throw new UnsupportedTypeException();
    }
    return converter.convert(value, new ConversionContext(type, resolver, this));
  }
  
}
