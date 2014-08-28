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
package org.soulwing.cdi.properties;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

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
   * Handles the {@link ProcessInjectionTarget} event.
   * @param event the subject event
   */
  <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> event) {
    
    try {
      container.init();
      Set<InjectionPoint> injectionPoints = event.getInjectionTarget()
          .getInjectionPoints();
      
      for (InjectionPoint injectionPoint : injectionPoints) {
        Property qualifier = injectionPoint.getAnnotated().getAnnotation(
            Property.class);
        
        if (qualifier == null) continue;
        
        if (!(injectionPoint.getType() instanceof Class<?>)) continue;
        Class<?> type = (Class<?>) injectionPoint.getType();
        
        String name = qualifier.name();
        if (name.isEmpty()) {
          name = fullyQualifiedMemberName(event.getAnnotatedType(), 
              injectionPoint);
        }
  
        try {
          container.add(name, type, qualifier);
        }
        catch (UnsupportedTypeException ex) {
          // provide additional details not known to the bean container
          throw new UnsupportedTypeException(
              fullyQualifiedMemberName(event.getAnnotatedType(),
                  injectionPoint), type);
        }
      }
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private <T> String fullyQualifiedMemberName(AnnotatedType<T> annotatedType,
      InjectionPoint injectionPoint) {
    String beanClassName  = annotatedType.getJavaClass().getName();
    String memberName = injectionPoint.getMember().getName();
    return beanClassName + "." + memberName;
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
