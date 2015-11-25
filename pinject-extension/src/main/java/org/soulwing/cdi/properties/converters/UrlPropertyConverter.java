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

import java.net.MalformedURLException;
import java.net.URL;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyConverter} that converts URLs.
 *
 * @author Carl Harris
 */
public class UrlPropertyConverter extends AbstractPropertyConverter {

  private static final String CLASSPATH_SCHEME = "classpath:";

  @Override
  public boolean supports(Class<?> type) {
    return URL.class.isAssignableFrom(type);
  }

  @Override
  public Object convert(String value, Context context)
      throws IllegalArgumentException {
    if (value.startsWith(CLASSPATH_SCHEME)) {
      value = value.substring(CLASSPATH_SCHEME.length());
      while (value.startsWith("/")) {
        value = value.substring(1);
      }

      ClassLoader classLoader = (Thread.currentThread().getContextClassLoader() != null) ?
          Thread.currentThread().getContextClassLoader() :
          UrlPropertyConverter.class.getClassLoader();
      URL url = classLoader.getResource(value);
      if (url == null) {
        throw new IllegalArgumentException(
            "no such resource on classpath: " + value);
      }
      return url;
    }
    try {
      return new URL(value);
    }
    catch (MalformedURLException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

}
