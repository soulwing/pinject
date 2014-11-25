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

/**
 * An object that resolves a property name to a value.
 * <p>
 * A property resolver is configured for the <em>Pinject</em> extension
 * by placing a provider configuration file in the resource directory 
 * {@code META-INF/services} of a directory or JAR file on the classpath
 * of the extension.  The name of the configuration file is:
 * <p>
 * {@code org.soulwing.cdi.properties.spi.PropertyResolver}
 * <p>
 * The file may contain one or more fully-qualified class names of classes
 * that implement this interface.
*
 * @author Carl Harris
 */
public interface PropertyResolver {
  
  /**
   * Initializes this property resolver.
   * @throws Exception to indicate that initialization failed
   */
  void init() throws Exception;

  /**
   * Notifies this property resolver that it is no longer needed and that
   * it should consequently release any resources that it is holding.
   * @throws Exception to indicate that the resolver was not successful
   *    in cleaning up the resources it holds
   */
  void destroy() throws Exception;
  
  /**
   * Gets the priority of this resolver relative to other resolvers.
   * <p>
   * Given resolvers A and B, resolver A will be consulted
   * before resolver B iff {@code A.getPriority() > B.getPriority()}.
   * @return priority
   */
  int getPriority();
  
  /**
   * Resolves a property name to a value.
   * @param name the name to resolve
   * @return value or {@code null} if name has no corresponding value known
   *    to this resolver
   */
  String resolve(String name);
  
}
