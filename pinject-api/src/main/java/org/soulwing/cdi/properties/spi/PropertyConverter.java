/*
 * File created on Aug 27, 2014 
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
package org.soulwing.cdi.properties.spi;

import org.soulwing.cdi.properties.Property;

/**
 * An object that converts the string representation of property value to an 
 * instance of a target type.
 * <p>
 * A property converter is configured for the <em>Pinject</em> extension
 * by placing a provider configuration file in the resource directory 
 * {@code META-INF/services} of a directory or JAR file on the classpath
 * of the extension.  The name of the configuration file is:
 * <p>
 * {@code org.soulwing.cdi.properties.spi.PropertyConverter}
 * <p>
 * The file may contain one or more fully-qualified class names of classes
 * that implement this interface.
 *
 * @author Carl Harris
 */
public interface PropertyConverter {

  /**
   * A context for a property conversion.
   */
  interface Context {
    
    /**
     * Gets the target type for requested conversion.
     * @return target type (never {@code null})
     */
    Class<?> getTargetType();
    
    /**
     * Resolves a property name to a value.
     * @param name the name to resolve
     * @return resolved value or {@code defaultValue} if the name could not be 
     *    resolved
     */
    String resolve(String name, String defaultValue);
    
    /**
     * Converts a string representation of the given type to an instance 
     * of that type.
     * @param value the value to convert
     * @param type target type 
     * @return instance of {@code type}
     * @throws IllegalArgumentException if {@code value} cannot be converted
     *    into an instance of {@code type} because it is syntactically
     *    invalid
     * @throws UnsupportedOperationException if conversion to {@code type}
     *    is not supported by this context
     */
    Object convert(String value, Class<?> type);
    
  }
  
  /**
   * Gets the name for this property converter.
   * <p>
   * The {@link Property} qualifier allows a named converter to be specified.
   * 
   * @return converter name (never {@code null})
   */
  String getName();
  
  /**
   * Tests whether this converter supports a given type.
   * @param type the subject type
   * @return {@code true} if this converter can converter string representations
   *    of a property of the given {@code type}
   */
  boolean supports(Class<?> type);
  
  /**
   * Converts the given string representation of a property value to an 
   * instance of the underlying type supported by this converter.
   * @param value the value to convert
   * @param context conversion context
   * @return a representation of {@code value} as type {@code T}
   * @throws IllegalArgumentException if the given value is a syntactically
   *    invalid string representation for {@code type}
   */
  Object convert(String value, Context context) 
      throws IllegalArgumentException;
  
}
