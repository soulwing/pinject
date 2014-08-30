/*
 * File created on Aug 30, 2014 
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
package org.soulwing.cdi.properties.resolvers;

import org.jboss.weld.exceptions.UnsupportedOperationException;

/**
 * An object that segments a dotted property name into a path component and
 * a name component.
 *
 * @author Carl Harris
 */
class PropertyRef {
  
  private final int numNameSegments;
  private final String path;
  private final String name;

  /**
   * Constructs a new instance in which the delimited name is segmented such
   * that all but the last segment is considered the path, and the last 
   * segment is considered the name
   * @param name the delimited name
   */
  public PropertyRef(String name) {
    this(name, 1);
  }
  
  /**
   * Constructs a new instance in which the last {@code numNameSegments} of 
   * the delimited name is considered the name and the remaining segments
   * are considered the path
   * the name
   * @param name the delimited name
   * @param numNameSegments number of segments of the delimited name to use
   *    as the name
   */
  PropertyRef(String name, int numNameSegments) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException();
    }
    String[] segments = name.trim().split("[\\./]");
    if (segments.length < numNameSegments) {
      throw new IllegalArgumentException();
    }      
    this.path = path(segments, numNameSegments);
    this.name = name(segments, numNameSegments);
    this.numNameSegments = numNameSegments;      
  }

  /**
   * Constructs a delimited path from the given segments.
   * <p>
   * The first {@code segments.length - numNameSegments} are used to compose
   * the path.
   * @param segments 
   * @param numNameSegments
   * @return delimited path
   */
  private static String path(String[] segments, int numNameSegments) {
    StringBuilder path = new StringBuilder();
    for (int i = 0; i < segments.length - numNameSegments; i++) {
      path.append(segments[i]);
      path.append('/');
    }
    return path.toString();
  }
  
  /**
   * Constructs a delimited name from the given segments.
   * <p>
   * The last {@code numNameSegments} are used to compose the name.
   * @param segments 
   * @param numNameSegments
   * @return delimited name
   */
  private static String name(String[] segments, int numNameSegments) {
    StringBuilder name = new StringBuilder();
    for (int i = segments.length - numNameSegments; i < segments.length; i++) {
      name.append(segments[i]);
      if (i < segments.length - 1) {
        name.append('.');
      }
    }
    return name.toString();
  }
  
  /**
   * Tests whether the path of this property is the root path.
   * @return {@code true} if the path represents the root path
   */
  public boolean isRootPath() {
    return path.isEmpty();
  }
  
  /**
   * Gets the path.
   * @return path name; unless the path is the root path, the returned
   *    string always includes a trailing delimiter
   */
  public String getPath() {
    return path;
  }
  
  /**
   * Gets the path to given resource name (relative to the root).
   * @param resource resource name
   * @return path to the given resource
   */
  public String getPath(String resource) {
    return path + resource;
  }
  
  /**
   * Gets the name.
   * @return property name relative to the path of this ref
   */
  public String getName() {
    return name;
  }
  
  /**
   * Gets a ref for the parent of the receiver's path.
   * @return property ref in which the path is the parent of the receiver's
   *    path and the name is relative to the receiver's parent path
   */
  public PropertyRef getParent() {
    if (isRootPath()) {
      throw new UnsupportedOperationException();
    }
    return new PropertyRef(path + name, numNameSegments + 1);
  }
  
}