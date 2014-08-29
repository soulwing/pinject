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
package org.soulwing.cdi.properties.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import javax.mail.internet.InternetAddress;

import org.junit.Test;

/**
 * Unit tests for {@link InternetAddressPropertyConverter}.
 *
 * @author Carl Harris
 */
public class InternetAddressPropertyConverterTest {

  
  private InternetAddressPropertyConverter converter = 
      new InternetAddressPropertyConverter();
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(InternetAddress.class), is(true));
  }

  @Test
  public void testConvert() throws Exception {
    InternetAddress address = new InternetAddress("nobody@nowhere.net");
    assertThat((InternetAddress) converter.convert(address.toString(), null), 
        is(equalTo(address)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertInvalidAddress() throws Exception {
    converter.convert("INVALID_ADDRESS", null);
  }
  
}
