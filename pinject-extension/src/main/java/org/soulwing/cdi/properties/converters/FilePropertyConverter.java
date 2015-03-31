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

import java.io.File;
import java.net.URI;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * A {@link PropertyConverter} that converts URIs.
 *
 * @author Carl Harris
 */
public class FilePropertyConverter extends AbstractPropertyConverter {

  @Override
  public boolean supports(Class<?> type) {
    return File.class.isAssignableFrom(type);
  }

  @Override
  public Object convert(String value, Context context)
      throws IllegalArgumentException {
    return new File(value);
  }

}
