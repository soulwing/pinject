/*
 * File created on Mar 31, 2015
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URI;

import org.junit.Test;

/**
 * Unit tests for {@link FilePropertyConverter}.
 *
 * @author Carl Harris
 */
public class FilePropertyConverterTest {

  private FilePropertyConverter converter = new FilePropertyConverter();
  
  @Test
  public void testSupports() throws Exception {
    assertThat(converter.supports(File.class), is(true));
  }
  
  @Test
  public void testFile() throws Exception {
    File file = new File("someFile");
    assertThat((File) converter.convert(file.toString(), null),
        is(equalTo(file)));
  }

}
