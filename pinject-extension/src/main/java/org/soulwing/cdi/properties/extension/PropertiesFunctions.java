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
 * Functions used to access the properties available to a
 * {@link PropertyValueResolver}.
 * <p>
 * Because EL functions must be static methods, the design of this class is
 * a little unusual.  A thread that wishes to call any of the public static
 * methods must first call {@link #setResolver(PropertyValueResolver)} to
 * specify {@link PropertyValueResolver} that will be used to resolve property
 * names to string values.  Typically this is done right before an expression
 * is evaluated.  After the expression is evaluated, the same thread must call
 * {@link #clearResolver()} to remove the resolver from the thread local managed
 * by this class.
 *
 * @author Carl Harris
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PropertiesFunctions {

  private static final ThreadLocal<PropertyValueResolver> resolver =
      new ThreadLocal<>();

  /**
   * Sets the resolver to use by a calling thread.
   * @param resolver subject resolver
   */
  static void setResolver(PropertyValueResolver resolver) {
    PropertiesFunctions.resolver.set(resolver);
  }

  /**
   * Clears any resolver set by the calling thread.
   */
  static void clearResolver() {
    PropertiesFunctions.resolver.remove();
  }

  /**
   * Gets a property value.
   * @param name name of the property.
   * @return property value
   * @throws PropertyNotFoundException if {@code name} cannot be resolved
   */
  public static String get(String name) {
    final String value = resolver.get().resolve(name);
    if (value == null) {
      throw new PropertyNotFoundException(name);
    }
    return value;
  }

  /**
   * Gets a property value.
   * @param name name of the property.
   * @param defaultValue default value
   * @return property value or {@code defaultValue} if the property has no value
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
