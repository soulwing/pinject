/*
 * File created on Aug 28, 2014 
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URL;

import org.junit.Test;
import org.soulwing.cdi.properties.converters.BooleanPropertyConverter;
import org.soulwing.cdi.properties.converters.BytePropertyConverter;
import org.soulwing.cdi.properties.converters.CharacterPropertyConverter;
import org.soulwing.cdi.properties.converters.DoublePropertyConverter;
import org.soulwing.cdi.properties.converters.FloatPropertyConverter;
import org.soulwing.cdi.properties.converters.LongPropertyConverter;
import org.soulwing.cdi.properties.converters.ShortPropertyConverter;
import org.soulwing.cdi.properties.converters.StringPropertyConverter;
import org.soulwing.cdi.properties.converters.UrlPropertyConverter;
import org.soulwing.cdi.properties.extension.DelegatingPropertyValueConverter;
import org.soulwing.cdi.properties.extension.PropertyValueConverter;

/**
 * Tests for {@link DelegatingPropertyValueConverterTest}.
 * <p>
 * This test simply exercises the built-in converters to ensure that the
 * services configuration specified in {@code META-INF/services} is correct.
 *
 * @author Carl Harris
 */
public class DelegatingPropertyValueConverterTest {

  
  @Test
  public void testConvertersByType() throws Exception {
    PropertyValueConverter converter = new DelegatingPropertyValueConverter();
    
    assertThat((Boolean) converter.convert(
        Boolean.TRUE.toString(), boolean.class),
        is(true));
    
    assertThat((Byte) converter.convert(
        Byte.toString(Byte.MAX_VALUE), byte.class),
        is(equalTo(Byte.MAX_VALUE)));

    assertThat((Character) converter.convert(
        "C", char.class), 
        is(equalTo('C')));
    
    assertThat((Double) converter.convert(
        Double.toString(Math.E), double.class),
        is(equalTo(Math.E)));

    assertThat((Float) converter.convert(
        Float.toString((float) Math.E), float.class),
        is(equalTo((float) Math.E)));

    assertThat((Long) converter.convert(
        Long.toString(Long.MAX_VALUE), long.class),
        is(equalTo(Long.MAX_VALUE)));

    assertThat((Short) converter.convert(
        Short.toString(Short.MAX_VALUE), short.class),
        is(equalTo(Short.MAX_VALUE)));

    assertThat((String) converter.convert(
        "S", String.class),
        is(equalTo("S")));

    URL url = new URL("file:/path");
    assertThat((URL) converter.convert(url.toString(), URL.class), 
        is(equalTo(url)));

  }

  @Test
  public void testConvertersByName() throws Exception {
    PropertyValueConverter converter = new DelegatingPropertyValueConverter();
    
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
    
    assertThat((Double) converter.convert(
        DoublePropertyConverter.class.getName(),
        Double.toString(Math.E), double.class),
        is(equalTo(Math.E)));

    assertThat((Float) converter.convert(
        FloatPropertyConverter.class.getName(),
        Float.toString((float) Math.E), float.class),
        is(equalTo((float) Math.E)));

    assertThat((Long) converter.convert(
        LongPropertyConverter.class.getName(),
        Long.toString(Long.MAX_VALUE), long.class),
        is(equalTo(Long.MAX_VALUE)));

    assertThat((Short) converter.convert(
        ShortPropertyConverter.class.getName(),
        Short.toString(Short.MAX_VALUE), short.class),
        is(equalTo(Short.MAX_VALUE)));

    assertThat((String) converter.convert(
        StringPropertyConverter.class.getName(),
        "S", String.class),
        is(equalTo("S")));
    
    URL url = new URL("file:/path");
    assertThat((URL) converter.convert(
        UrlPropertyConverter.class.getName(),
        url.toString(), URL.class), is(equalTo(url)));
  }

  
}
