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

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyResolver} that holds an ordered set of {@link Properties}
 * collections, and resolves properties by consulting them in order.
 *
 * @author Carl Harris
 */
abstract class PropertiesSetResolver implements PropertyResolver {

  private final Set<Properties> propertiesSet = new LinkedHashSet<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() throws Exception {
    propertiesSet.clear();
  }

  protected void loadProperties(Enumeration<URL> locations) 
      throws IOException {
    while (locations.hasMoreElements()) {
      URL location = locations.nextElement();
      propertiesSet.add(loadProperties(location));
    }    
  }
  
  private Properties loadProperties(URL url) throws IOException {
    InputStream inputStream = url.openStream();
    if (inputStream == null) {
      throw new FileNotFoundException(url.toString());
    }
    try {
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
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
   * {@inheritDoc}
   */
  @Override
  public final String resolve(String name) {
    for (Properties properties : propertiesSet) {
      String value = properties.getProperty(name);
      if (value != null) return value;
    }
    return null;
  }

}
