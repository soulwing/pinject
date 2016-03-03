/*
 * File created on Mar 1, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
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
package org.soulwing.cdi.properties.extension;

import javax.el.PropertyNotFoundException;

/**
 * Functions used to access the system environment.
 *
 * @author Carl Harris
 */
public class EnvironmentFunctions {

  /**
   * Gets the value of an environment variable.
   * @param name name of the environment variable
   * @return value or {@code null} if the specified environment variable is not set
   */
  public static String get(String name) {
    final String value = System.getenv(name);
    if (value == null) {
      throw new PropertyNotFoundException(name);
    }
    return value;
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the environment variable
   * @param defaultValue default value
   * @return value or {@code defaultValue} if the specified environment variable
   *    is not set
   */
  public static String getOptional(String name, String defaultValue) {
    try {
      return get(name);
    }
    catch (PropertyNotFoundException ex) {
      return defaultValue;
    }
  }

}
