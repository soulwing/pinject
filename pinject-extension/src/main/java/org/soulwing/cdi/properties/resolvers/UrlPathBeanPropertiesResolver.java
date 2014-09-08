/*
 * File created on Sep 7, 2014 
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A {@link PropertyResolver} that resolves values by considering each
 * property name to be a path to a {@link beans.properties} resource on
 * the class path.
 *
 * @author Carl Harris
 */
public class UrlPathBeanPropertiesResolver
    extends AbstractPackagePathPropertyResolver {

  private static final Logger logger = Logger.getLogger(
      UrlPathBeanPropertiesResolver.class.getName());
  
  /**
   * Constructs a new instance.
   * @param priority priority for this resolver
   * @param location base URL
   */
  public UrlPathBeanPropertiesResolver(int priority, URL location) {
    super(priority, new UrlPropertiesSetLoader(location));
  }

  /**
   * A {@link PropertiesSetLoader} that loads {@code beans.properties} using
   * the class loader.
   */
  static class UrlPropertiesSetLoader implements PropertiesSetLoader {

    private final URL location;
        
    /**
     * Constructs a new instance.
     * @param location
     */
    public UrlPropertiesSetLoader(URL location) {
      try {
        if (!location.toExternalForm().endsWith("/")) {
          location = new URL(location.toExternalForm() + "/");
        }
        this.location = location;
      }
      catch (MalformedURLException ex) {
        throw new IllegalArgumentException(ex);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesSet load(PropertyRef ref) throws IOException {
      URLConnection connection = connect(ref);
      PropertiesSet propertiesSet = new PropertiesSet();
      if (connection != null) {
        propertiesSet.load(connection.getInputStream());
      }
      return propertiesSet;
    }

    private URLConnection connect(PropertyRef ref)
        throws MalformedURLException, IOException {
      URL url = new URL(location, ref.getPath(BeansProperties.NAME));
      try {
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
      }
      catch (IOException ex) {
        if (logger.isLoggable(Level.FINEST)) {
          logger.finest(String.format("error loading properties from %s: %s",
              url, ex));
        }
        return null;
      }
    }
  
  }

}
