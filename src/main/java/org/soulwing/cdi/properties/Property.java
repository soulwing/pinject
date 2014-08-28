/*
 * File created on Aug 27, 2014 
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
package org.soulwing.cdi.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.soulwing.cdi.properties.spi.PropertyConverter;

/**
 * An annotation used to designate a parameter, field, method, constructor 
 * that will be injected with a property value.
 *
 * @author Carl Harris
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, 
  ElementType.CONSTRUCTOR })
public @interface Property {

  /**
   * Name of the property to inject.
   * <p>
   * The name defaults to fully qualified name of the annotated member when 
   * not specified.
   * @return property name 
   */
  String name() default "";
  
  /**
   * Provides a default value when there is no other value to inject into
   * the target.
   * @return string representation of default value
   */
  String value() default "";
  
  /**
   * Specifies the name of a property converter to use when converting 
   * string representations of property values to an instance of the target
   * type of the injection point.
   * @return converter name (as returned by {@link PropertyConverter#getName()})
   */
  String converter() default "";
  
}
