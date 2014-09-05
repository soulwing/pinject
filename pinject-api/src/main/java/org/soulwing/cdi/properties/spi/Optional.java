/*
 * File created on Aug 29, 2014 
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
package org.soulwing.cdi.properties.spi;

/**
 * A provider component that depends on classes or other resources that
 * might not be available at runtime can implement this interface to indicate
 * that it is optional.
 *
 * @author Carl Harris
 */
public interface Optional {

  /**
   * Tests whether this provider is available.
   * <p>
   * This method will be invoked once after the component is instantiated via
   * its constructor.  If the return value is {@code false} no subsequent 
   * method invocations will occur on the provider instance.
   * @return {@code true} if the provider is should be considered available 
   */
  boolean isAvailable();
  
}
