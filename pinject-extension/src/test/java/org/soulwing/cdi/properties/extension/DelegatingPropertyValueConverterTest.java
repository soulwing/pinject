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
package org.soulwing.cdi.properties.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.ScheduleExpression;
import javax.mail.internet.InternetAddress;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soulwing.cdi.properties.converters.BooleanPropertyConverter;
import org.soulwing.cdi.properties.converters.BytePropertyConverter;
import org.soulwing.cdi.properties.converters.CharacterPropertyConverter;
import org.soulwing.cdi.properties.converters.DatePropertyConverter;
import org.soulwing.cdi.properties.converters.DoublePropertyConverter;
import org.soulwing.cdi.properties.converters.EnumPropertyConverter;
import org.soulwing.cdi.properties.converters.EnumPropertyConverterTest.Color;
import org.soulwing.cdi.properties.converters.FilePropertyConverter;
import org.soulwing.cdi.properties.converters.FloatPropertyConverter;
import org.soulwing.cdi.properties.converters.IntegerPropertyConverter;
import org.soulwing.cdi.properties.converters.InternetAddressPropertyConverter;
import org.soulwing.cdi.properties.converters.LongPropertyConverter;
import org.soulwing.cdi.properties.converters.ScheduleExpressionPropertyConverter;
import org.soulwing.cdi.properties.converters.ShortPropertyConverter;
import org.soulwing.cdi.properties.converters.StringPropertyConverter;
import org.soulwing.cdi.properties.converters.UriPropertyConverter;
import org.soulwing.cdi.properties.converters.UrlPropertyConverter;

/**
 * Tests for {@link DelegatingPropertyValueConverterTest}.
 * <p>
 * This test simply exercises the built-in converters to ensure that the
 * services configuration specified in {@code META-INF/services} is correct.
 *
 * @author Carl Harris
 */
public class DelegatingPropertyValueConverterTest {

  private final PropertyValueConverter converter =
      new DelegatingPropertyValueConverter(
          new DelegatingPropertyValueResolver());
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    LogManagerUtil.configure();
  }

  @Before
  public void setUp() throws Exception {
    converter.init();
  }

  @Test
  public void testConvertersByType() throws Exception {
    assertThat((Boolean) converter.convert(
        Boolean.TRUE.toString(), boolean.class),
        is(true));
    
    assertThat((Byte) converter.convert(
        Byte.toString(Byte.MAX_VALUE), byte.class),
        is(equalTo(Byte.MAX_VALUE)));

    assertThat((Character) converter.convert(
        "C", char.class), 
        is(equalTo('C')));
    
    Date now = new Date();
    assertThat((Date) converter.convert(
        new SimpleDateFormat(DatePropertyConverter.ISO8601).format(now), 
        Date.class), is(equalTo(now)));

    assertThat((Double) converter.convert(
        Double.toString(Math.E), double.class),
        is(equalTo(Math.E)));

    assertThat((Color) converter.convert(
        Color.RED.name(), Color.class),
        is(equalTo(Color.RED)));
    
    assertThat((Float) converter.convert(
        Float.toString((float) Math.E), float.class),
        is(equalTo((float) Math.E)));

    assertThat((Integer) converter.convert(        
        Integer.toString(Integer.MAX_VALUE), int.class),
        is(equalTo(Integer.MAX_VALUE)));

    InternetAddress internetAddress = new InternetAddress(
        "nobody@nowhere.net");
    assertThat((InternetAddress) converter.convert(
        internetAddress.toString(), InternetAddress.class),
        is(equalTo(internetAddress)));
    
    assertThat((Long) converter.convert(
        Long.toString(Long.MAX_VALUE), long.class),
        is(equalTo(Long.MAX_VALUE)));

    assertThat(converter.convert(
            "1 2 3 4 5 6", ScheduleExpression.class),
        is(instanceOf(ScheduleExpression.class)));

    assertThat((Short) converter.convert(
        Short.toString(Short.MAX_VALUE), short.class),
        is(equalTo(Short.MAX_VALUE)));

    assertThat((String) converter.convert(
        "S", String.class),
        is(equalTo("S")));

    File file = new File("someFile");
    assertThat((File) converter.convert(file.toString(), File.class),
        is(equalTo(file)));

    URI uri = URI.create("file:/path");
    assertThat((URI) converter.convert(uri.toString(), URI.class), 
        is(equalTo(uri)));

    URL url = new URL("file:/path");
    assertThat((URL) converter.convert(url.toString(), URL.class), 
        is(equalTo(url)));

  }

  @Test
  public void testConvertersByName() throws Exception {
    assertThat((Boolean) converter.convert(
        BooleanPropertyConverter.class.getName(),
        Boolean.TRUE.toString(), boolean.class),
        is(true));
    
    assertThat((Byte) converter.convert(
        BytePropertyConverter.class.getName(),
        Byte.toString(Byte.MAX_VALUE), byte.class),
        is(equalTo(Byte.MAX_VALUE)));

    assertThat((Character) converter.convert(
        CharacterPropertyConverter.class.getName(),
        "C", char.class), 
        is(equalTo('C')));
    
    Date now = new Date();
    assertThat((Date) converter.convert(
        DatePropertyConverter.class.getName(),
        new SimpleDateFormat(DatePropertyConverter.ISO8601).format(now), 
        Date.class), is(equalTo(now)));
    
    assertThat((Double) converter.convert(
        DoublePropertyConverter.class.getName(),
        Double.toString(Math.E), double.class),
        is(equalTo(Math.E)));

    assertThat((Color) converter.convert(
        EnumPropertyConverter.class.getName(),
        Color.RED.name(), Color.class),
        is(equalTo(Color.RED)));
    
    assertThat((Float) converter.convert(
        FloatPropertyConverter.class.getName(),
        Float.toString((float) Math.E), float.class),
        is(equalTo((float) Math.E)));

    assertThat((Integer) converter.convert(
        IntegerPropertyConverter.class.getName(),
        Integer.toString(Integer.MAX_VALUE), int.class),
        is(equalTo(Integer.MAX_VALUE)));

    InternetAddress internetAddress = new InternetAddress(
        "nobody@nowhere.net");
    assertThat((InternetAddress) converter.convert(
        InternetAddressPropertyConverter.class.getName(), 
        internetAddress.toString(), InternetAddress.class),
        is(equalTo(internetAddress)));
    
    assertThat((Long) converter.convert(
        LongPropertyConverter.class.getName(),
        Long.toString(Long.MAX_VALUE), long.class),
        is(equalTo(Long.MAX_VALUE)));

    assertThat(converter.convert(
            ScheduleExpressionPropertyConverter.class.getName(),
            "1 2 3 4 5 6", ScheduleExpression.class),
        is(instanceOf(ScheduleExpression.class)));

    assertThat((Short) converter.convert(
        ShortPropertyConverter.class.getName(),
        Short.toString(Short.MAX_VALUE), short.class),
        is(equalTo(Short.MAX_VALUE)));

    assertThat((String) converter.convert(
        StringPropertyConverter.class.getName(),
        "S", String.class),
        is(equalTo("S")));

    File file = new File("someFile");
    assertThat((File) converter.convert(
            FilePropertyConverter.class.getName(),
            file.toString(), File.class),
        is(equalTo(file)));

    URI uri = URI.create("file:/path");
    assertThat(
        (URI) converter.convert(
            UriPropertyConverter.class.getName(), 
            uri.toString(), URI.class), 
        is(equalTo(uri)));

    URL url = new URL("file:/path");
    assertThat((URL) converter.convert(
        UrlPropertyConverter.class.getName(),
        url.toString(), URL.class), is(equalTo(url)));
  }

  
}
