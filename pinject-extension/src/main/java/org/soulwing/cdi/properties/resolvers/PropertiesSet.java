/*
 * File created on Aug 28, 2014 
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * An ordered set of {@link Properties} collections.
 *
 * @author Carl Harris
 */
public class PropertiesSet {

  private final Set<Properties> propertiesSet = new LinkedHashSet<>();

  /**
   * Gets a property value from the set.
   * <p>
   * The properties collections in this set are consulted, in the order 
   * added, in order to locate a value for the given property.
   * @param name name of the property to obtain
   * @return property value or {@code null} if no properties collection
   *    in this set contains a property with the given name
   * 
   */
  public String getProperty(String name) {
    for (Properties properties : propertiesSet) {
      String value = properties.getProperty(name);
      if (value != null) return value;
    }
    return null;
  }

  /**
   * Adds a properties collection to the set. 
   * @param properties the collection to add
   */
  public void add(Properties properties) {
    propertiesSet.add(properties);
  }
  
  /**
   * Loads a properties collection into this set.
   * @param inputStream input stream from which the collection will be read
   * @throws IOException
   */
  public void load(InputStream inputStream) throws IOException {
    if (inputStream == null) {
      throw new NullPointerException("inputStream is required");
    }
    try {
      Properties properties = new Properties();
      properties.load(inputStream);
      add(properties);
    }
    finally {
      try {
        inputStream.close();
      }
      catch (IOException ex) {
        ex.printStackTrace(System.err);
      }
    }
  }

  /**
   * Loads a properties collection into this set.
   * @param location location of the collection to load
   * @throws IOException
   */
  public void load(URL location) throws IOException {
    if (location == null) {
      throw new NullPointerException("location is required");
    }
    InputStream inputStream = location.openStream();
    if (inputStream == null) {
      throw new FileNotFoundException(location.toString());
    }
    load(inputStream);
  }

  /**
   * Loads the properties collections found at each of the URLs 
   * in the given enumeration into this set.
   * @param locations locations of the properties collections to load into
   *    the set
   * @throws IOException
   */
  public void load(Enumeration<URL> locations) throws IOException {
    while (locations.hasMoreElements()) {
      URL location = locations.nextElement();
      load(location);
    }    
  }

  /**
   * Clears all properties collections from this set, resulting in an
   * empty set.
   */
  public void clear() {
    propertiesSet.clear();
  }

}
