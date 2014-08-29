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
package org.soulwing.cdi.properties.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * Unit tests for {@link EnumPropertyConverter}.
 *
 * @author Carl Harris
 */
public class EnumPropertyConverterTest {

  public enum Color {
    RED,
    GREEN,
    BLUE;
  }
  
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private PropertyConverter.Context converterContext;
  
  private EnumPropertyConverter converter = new EnumPropertyConverter();
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(Color.class), is(true));
  }
  
  @Test
  public void testConvert() throws Exception {
    context.checking(new Expectations() { { 
      oneOf(converterContext).getTargetType();
      will(returnValue(Color.class));
    } });
    
    assertThat((Color) converter.convert(Color.RED.name(), converterContext),
        is(equalTo(Color.RED)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertWithInvalidName() throws Exception {
    context.checking(new Expectations() { { 
      oneOf(converterContext).getTargetType();
      will(returnValue(Color.class));
    } });
    
   converter.convert("NOT A COLOR", converterContext);
  }

}
