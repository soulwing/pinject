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
package org.soulwing.cdi.properties.resolvers;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.soulwing.cdi.properties.converters.UrlPropertyConverter;
import org.soulwing.cdi.properties.spi.PropertyResolver;

/**
 * A resolver that looks up a JNDI environment variable and (if found)
 * interprets it as a space and/or comma delimited list of URLs to 
 * properties files to consult (in the given order) for name resolution.
 *
 * @author Carl Harris
 */
public class JndiEnvBeanPropertiesResolver implements PropertyResolver {

  private static final Logger logger = Logger.getLogger(
      JndiEnvBeanPropertiesResolver.class.getName());
  
  public static final String BINDING = 
      "java:comp/env/beans.properties.location";

  public static final int PRIORITY = -5;
  
  private final PropertiesSet propertiesSet = new PropertiesSet();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws IOException, NamingException {
    UrlPropertyConverter converter = new UrlPropertyConverter();
    Object boundValue = getBoundValue();
    if (boundValue == null) return;
    String[] locations = boundValue.toString().split("\\s*(,|\\s)\\s*");
    
    for (String location : locations) {
      propertiesSet.load((URL) converter.convert(location, null));
    }
  }

  /**
   * Gets the JNDI environment variable bound as {@link BINDING}.
   * @return bound value or {@code null} if the variable does not exist or
   *    if the JNDI query failed because the initial context for the lookup
   *    could not be created (indicating that we're not running in a container)
   */
  private Object getBoundValue() throws NamingException {
    try {
      InitialContext ctx = new InitialContext();
      return ctx.lookup(BINDING);
    }
    catch (NameNotFoundException ex) {
      logger.info("JNDI lookup for " + BINDING + " returned nothing");
      return null;
    }
    catch (NoInitialContextException ex) {
      logger.fine("no initial context; probably not running in a container");
      return null;
    }
  }
  
  @Override
  public void destroy() throws Exception {
    propertiesSet.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public String resolve(String name) {
    return propertiesSet.getProperty(name);
  }

}
