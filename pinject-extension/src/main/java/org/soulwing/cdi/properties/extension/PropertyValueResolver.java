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
package org.soulwing.cdi.properties.extension;

/**
 * An object that resolves a property name to a corresponding string value.
 *
 * @author Carl Harris
 */
interface PropertyValueResolver {

  /**
   * Initializes this resolver.
   * @throws Exception to indicate that resolver could not be successfully
   *    initialized
   */
  void init() throws Exception;
  
  /**
   * Notifies this resolver that its services are no longer needed and that
   * it should clean up any resources it is holding. 
   */
  void destroy();
  
  /**
   * Resolves a property name to a value.
   * @param name the name to resolve
   * @return value or {@code null} if name has no corresponding value known
   *    to this resolver
   */
  String resolve(String name);

}
