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
package org.soulwing.cdi.properties.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
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
  
  private final PropertyBeanContainer container;
  
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
    container.init();
  }
  
  /**
   * Handles the process injection point event.
   * @param event
   */
  <T,E> void processInjectionPoint(@Observes ProcessInjectionPoint<T,E> event) 
      throws Exception {
    
    InjectionPoint injectionPoint = event.getInjectionPoint();

    Property qualifier = injectionPoint.getAnnotated().getAnnotation(
        Property.class);

    if (qualifier == null) return;
    
    event.setInjectionPoint(container.add(injectionPoint, qualifier));
  }

  /**
   * Handles the {@link AfterBeanDiscovery} event.
   * @param event the subject event
   */
  void afterBeanDiscovery(@Observes AfterBeanDiscovery event) {
    container.copyAll(event);
    container.destroy();
  }
  
}
