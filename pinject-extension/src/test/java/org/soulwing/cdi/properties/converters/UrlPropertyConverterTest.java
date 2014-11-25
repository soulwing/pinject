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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.net.URL;

import org.junit.Test;

/**
 * Unit tests for {@link UrlPropertyConverter}.
 *
 * @author Carl Harris
 */
public class UrlPropertyConverterTest {

  private UrlPropertyConverter converter = new UrlPropertyConverter();
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(URL.class), is(true));
  }
  
  @Test
  public void testFileUrl() throws Exception {
    URL url = new URL("file:/path");
    assertThat((URL) converter.convert(url.toString(), null), 
        is(equalTo(url)));
  }

  @Test
  public void testHttpUrl() throws Exception {
    URL url = new URL("http://authority/path");
    assertThat((URL) converter.convert(url.toString(), null), 
        is(equalTo(url)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMalformedUrl() throws Exception {
    converter.convert("not a URL", null);
  }
  
  @Test
  public void testClasspathUrl() throws Exception {
    assertThat((URL) converter.convert(
        "classpath:META-INF/beans.properties", null), 
        is(not(nullValue())));
  }

  @Test
  public void testClasspathUrlWithLeadingSlash() throws Exception {
    assertThat((URL) converter.convert(
        "classpath:/META-INF/beans.properties", null), 
        is(not(nullValue())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testClasspathUrlWhenResourceNotFound() throws Exception {
    converter.convert("classpath:DOES_NOT_EXIST", null);
  }


}
