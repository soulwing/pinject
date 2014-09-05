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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * Unit tests for {@link DatePropertyConverter}.
 *
 * @author Carl Harris
 */
public class DatePropertyConverterTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private PropertyConverter.Context converterContext;
  
  private DatePropertyConverter converter = new DatePropertyConverter();
  
  private Date now;
  private String nowString;
  
  @Before
  public void setUp() throws Exception {
    now = new Date();
    nowString = new SimpleDateFormat(DatePropertyConverter.ISO8601).format(now);
  }
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(Date.class), is(true));
    assertThat(converter.supports(Calendar.class), is(true));
  }
  
  @Test
  public void testConvertToDate() throws Exception {
    context.checking(newContextExpectations(Date.class,
        DatePropertyConverter.ISO8601));
    assertThat((Date) converter.convert(nowString, converterContext),
        is(equalTo(now)));
  }

  @Test
  public void testConvertToCalendar() throws Exception {
    context.checking(newContextExpectations(Calendar.class,
        DatePropertyConverter.ISO8601));
    Calendar calendar = (Calendar) converter.convert(nowString, converterContext);
    assertThat(calendar.getTime(), is(equalTo(now)));
  }
  
  @Test
  public void testCustomFormat() throws Exception {
    String pattern = "EEE, d MMM yyyy HH:mm:ss.SSS Z";
    SimpleDateFormat df = new SimpleDateFormat(pattern);
    String nowString = df.format(now);
    context.checking(newContextExpectations(Date.class, pattern));
    Date date = (Date) converter.convert(nowString, converterContext);
    assertThat(date, is(equalTo(now)));
  }
  
  private Expectations newContextExpectations(
      final Class<?> targetType, final String pattern) {
    return new Expectations() { { 
      oneOf(converterContext).getTargetType();
        will(returnValue(targetType));
      oneOf(converterContext).resolve(with(DatePropertyConverter.PATTERN), 
          with(DatePropertyConverter.ISO8601));
        will(returnValue(pattern));
    } };
  }
  
}
