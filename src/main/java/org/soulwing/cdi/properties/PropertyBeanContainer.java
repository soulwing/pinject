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

import javax.enterprise.inject.spi.AfterBeanDiscovery;

/**
 * A container for {@link PropertyBean} objects.
 *
 * @author Carl Harris
 */
interface PropertyBeanContainer {

  /**
   * Initializes this container.
   * @throws Exception to indicate that container could not be successfully
   *    initialized
   */
  void init() throws Exception;
  
  /**
   * Notifies this container that its services are no longer needed and that
   * it should clean up any resources it is holding. 
   */
  void destroy();

  /**
   * Adds a bean to this container.
   * @param name name of the property
   * @param type target type of the property
   * @param qualifier the qualifier instance from the injection point
   * @throws UnresolvedPropertyException
   * @throws NoSuchConverterException
   * @throws UnsupportedTypeException
   */
  void add(String name, Class<?> type, Property qualifier) 
      throws UnresolvedPropertyException, NoSuchConverterException,
      UnsupportedTypeException;
  
  /**
   * Adds all of the beans in this container to the CDI container that 
   * produced the given {@link AfterDiscoveryEvent}.
   * @param event the subject CDI event
   */
  void copyAll(AfterBeanDiscovery event);
  
  
}
