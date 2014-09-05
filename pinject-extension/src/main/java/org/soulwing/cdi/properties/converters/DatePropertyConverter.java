/*
 * File created on Aug 30, 2014 
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
package org.soulwing.cdi.properties.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyConverter} for {@link Date} and {@link Calendar} 
 * properties.
 *
 * @author Carl Harris
 */
public class DatePropertyConverter extends AbstractPropertyConverter {

  public static final String PATTERN = DatePropertyConverter.class.getName();
  
  public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(Class<?> type) {
    return Date.class.isAssignableFrom(type)
        || Calendar.class.isAssignableFrom(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convert(String value, Context context)
      throws IllegalArgumentException {
    String pattern = context.resolve(PATTERN, ISO8601);
    DateFormat df = new SimpleDateFormat(pattern);
    try {
      Date date = df.parse(value);
      if (Date.class.isAssignableFrom(context.getTargetType())) {
        return date;
      }
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar;
    }
    catch (ParseException ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

}
