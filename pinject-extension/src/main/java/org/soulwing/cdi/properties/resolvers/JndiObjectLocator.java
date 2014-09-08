/*
 * File created on Sep 8, 2014 
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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

/**
 * A singleton JNDI object locator utility.
 *
 * @author Carl Harris
 */
public class JndiObjectLocator {

  private static final Logger logger = Logger.getLogger(
      JndiObjectLocator.class.getName());
  
  private static final Lock lock = new ReentrantLock();  
  private static JndiObjectLocator instance;
  
  private Context ctx;
  
  /**
   * Constructs a new instance.
   */
  private JndiObjectLocator() {    
  }
  
  /**
   * Initializes this instance.
   * @throws NamingException if an unrecoverable JNDI error occurs
   */
  private void init() throws NamingException {
    ctx = new InitialContext();
  }
  
  /**
   * Gets the JNDI object bound as {@code name} if it exists.
   * @param name the name to lookup
   * @return bound value or {@code null} if no object is bound with the
   *    given {@code name} or if the initial context for the lookup
   *    could not be created (indicating that we're not running in a container)
   */
  public Object lookup(String name) throws NamingException {
    try {
      return ctx.lookup(name);
    }
    catch (NoInitialContextException ex) {
      logger.fine("no initial context; probably not running in a container");
      return null;
    }
    catch (NameNotFoundException ex) {
      logger.info("JNDI lookup for '" + name + "' returned nothing");
      return null;
    }
  }

  /**
   * Gets the singleton instance.
   * @return JNDI locator object
   * @throws NamingException if an unrecoverable JNDI error occurs in 
   *    creating the singleton instance
   */
  public static JndiObjectLocator getInstance() throws NamingException {
    lock.lock();
    try {
      if (instance == null) {
        instance = new JndiObjectLocator();
        instance.init();
      }
      return instance;
    }
    finally {
      lock.unlock();
    }
  }
  
}
