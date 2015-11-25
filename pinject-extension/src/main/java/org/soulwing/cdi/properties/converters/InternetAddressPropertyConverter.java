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

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyConverter} that supports the {@link InternetAddress}
 * type.
 *
 * @author Carl Harris
 */
public class InternetAddressPropertyConverter 
    extends OptionalPropertyConverter {

  protected boolean checkAvailability() {
    ClassLoader classLoader = (Thread.currentThread().getContextClassLoader() != null) ?
        Thread.currentThread().getContextClassLoader() :
        InternetAddressPropertyConverter.class.getClassLoader();
    try {
      classLoader.loadClass("javax.mail.internet.InternetAddress");
      return true;
    }
    catch (ClassNotFoundException ex) {
      return false;
    }
  }

  protected PropertyConverter newConverter() {
    return new InnerConverter();
  }
  
  private class InnerConverter implements PropertyConverter {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return InternetAddressPropertyConverter.this.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> type) {
      return InternetAddress.class.isAssignableFrom(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(String value, Context context)
        throws IllegalArgumentException {
      try {
        return new InternetAddress(value);
      }
      catch (MessagingException ex) {
        throw new IllegalArgumentException(ex);
      }
    }

  }
    
}
