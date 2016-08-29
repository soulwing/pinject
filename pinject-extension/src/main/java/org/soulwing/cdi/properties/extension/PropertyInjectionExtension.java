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

import java.lang.reflect.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

import org.soulwing.cdi.properties.Property;

/**
 * A CDI extension that supports injection of properties (e.g. numbers, 
 * strings, dates, etc).
 *
 * @author Carl Harris
 */
public class PropertyInjectionExtension implements Extension {
  
  private static final Logger logger = Logger.getLogger(
      PropertyInjectionExtension.class.getName());
  
  private final Map<Member, InjectionPoint> wrapperMap = 
      new ConcurrentHashMap<>();
  
  private final PropertyBeanContainer container;

  private ClassLoader deploymentClassLoader;
  
  /**
   * Constructs a new instance.
   */
  public PropertyInjectionExtension() {
    this(new SimplePropertyBeanContainer());
  }

  /**
   * Constructs a new instance.
   * @param container container for property beans discovered by 
   *    this extension
   */
  PropertyInjectionExtension(PropertyBeanContainer container) {
    this.container = container;
  }

  /**
   * Handles the event that fires before bean discovery.
   * @param event
   * @throws Exception
   */
  void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event) 
      throws Exception {
    logger.fine("starting property injection");
    deploymentClassLoader = Thread.currentThread().getContextClassLoader();
    logger.fine("deployment class loader: " + deploymentClassLoader);
    if (deploymentClassLoader == null) {
      logger.warning("no deployment class loader");
    }
    container.init();
  }

  /**
   * Handles the process injection point event.
   * @param event
   */
  <T,E> void processInjectionPoint(@Observes ProcessInjectionPoint<T,E> event) 
      throws Exception {

    // The calling thread here may not have the deployment class loader set as
    // thread context class loader. Since `beforeBeanDiscovery` seems to be
    // consistently called with the deployment class loader as the TCCL, make
    // sure the same TCCL is used here.

    final ClassLoader previousTccl =
        Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(deploymentClassLoader);
      InjectionPoint injectionPoint = event.getInjectionPoint();

      Property qualifier = injectionPoint.getAnnotated().getAnnotation(
          Property.class);
      if (qualifier == null) return;

      if (logger.isLoggable(Level.FINEST)) {
        logger.finest("injecting into " + injectionPoint);
      }

      InjectionPoint wrapper = wrapperMap.get(injectionPoint.getMember());
      if (wrapper == null) {
        wrapper = container.add(injectionPoint, qualifier);
        wrapperMap.put(injectionPoint.getMember(), wrapper);
      }
      else {
        if (logger.isLoggable(Level.FINE)) {
          logger.fine("found cached injection point wrapper for member "
              + injectionPoint.getMember());
        }
      }
      event.setInjectionPoint(wrapper);
    }
    finally {
      Thread.currentThread().setContextClassLoader(previousTccl);
    }
  }

  private void setBeanContextClassLoader(InjectionPoint injectionPoint) {
    final Bean<?> bean = injectionPoint.getBean();
    if (bean == null) return;
    final Class<?> beanClass = bean.getBeanClass();
    if (beanClass == null) return;

    final ClassLoader beanClassLoader = beanClass.getClassLoader();
    Thread.currentThread().setContextClassLoader(beanClassLoader);
    logger.fine("set bean class loader: " + beanClassLoader);
  }


  /**
   * Handles the {@link AfterBeanDiscovery} event.
   * @param event the subject event
   */
  void afterBeanDiscovery(@Observes AfterBeanDiscovery event) {
    container.copyAll(event);
    container.destroy();
    logger.fine("property injection complete");
  }
  
  void beforeShutdown(@Observes BeforeShutdown event) {
    wrapperMap.clear();
    logger.fine("property injection shut down");
  }
  
}
