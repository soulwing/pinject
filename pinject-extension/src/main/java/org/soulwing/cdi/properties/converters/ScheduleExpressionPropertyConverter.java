/*
 * File created on Jul 9, 2015
 *
 * Copyright (c) 2015 Carl Harris, Jr
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

import javax.ejb.ScheduleExpression;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyConverter} that supports the {@link ScheduleExpression}
 * type.
 *
 * @author Carl Harris
 */
public class ScheduleExpressionPropertyConverter extends OptionalPropertyConverter {

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean checkAvailability() {
    try {
      Thread.currentThread().getContextClassLoader().loadClass(
          "javax.ejb.ScheduleExpression");
      return true;
    }
    catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PropertyConverter newConverter() {
    return new InnerConverter();
  }

  private static class InnerConverter implements PropertyConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return ScheduleExpressionPropertyConverter.class.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> type) {
      return ScheduleExpression.class.isAssignableFrom(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(String value, Context context)
        throws IllegalArgumentException {
      String[] tokens = value.split("\\s+");
      if (tokens.length < 6 || tokens.length > 7) {
        throw new IllegalArgumentException(
            "expression must include 6 or 7 space-delimited tokens; got '"
            + value + "'");
      }
      ScheduleExpression se = new ScheduleExpression();
      se.second(tokens[0]);
      se.minute(tokens[1]);
      se.hour(tokens[2]);
      se.dayOfMonth(tokens[3]);
      se.month(tokens[4]);
      se.dayOfWeek(tokens[5]);
      if (tokens.length == 7) {
        se.year(tokens[6]);
      }
      return se;
    }
  }
}
