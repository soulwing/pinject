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

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.soulwing.cdi.properties.Property;

/**
 * A {@link PropertyBeanContainer} backed by a {@link Set}.
 *
 * @author Carl Harris
 */
class SimplePropertyBeanContainer implements PropertyBeanContainer {

  private final Lock lock = new ReentrantLock();
  
  private final Set<PropertyBean> beans = new HashSet<>();
  
  private final PropertyValueResolver resolver;
  private final PropertyValueConverter converter;
    
  private long nextId;
  
  public SimplePropertyBeanContainer() {
    this(new DelegatingPropertyValueResolver());
  }
  
  SimplePropertyBeanContainer(PropertyValueResolver resolver) {
    this(resolver, new DelegatingPropertyValueConverter(resolver));
  }
  
  SimplePropertyBeanContainer(PropertyValueResolver resolver, 
      PropertyValueConverter converter) {
    this.resolver = resolver;
    this.converter = converter;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws Exception {
    resolver.init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    resolver.destroy();
    beans.clear();    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InjectionPoint add(InjectionPoint injectionPoint, Property qualifier) 
      throws UnresolvedPropertyException, NoSuchConverterException,
      UnsupportedTypeException {
    
    InjectionPoint wrapper = wrap(injectionPoint);
    
    Class<?> type = type(injectionPoint);
    String name = name(injectionPoint, qualifier);
    Object value = convert(resolve(name, qualifier), type, qualifier, 
        fullyQualifiedMemberName(injectionPoint));

    store(new PropertyBean(value, type, wrapper.getQualifiers()));
    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copyAll(AfterBeanDiscovery event) {
    lock.lock();
    try {
      for (PropertyBean bean : beans) {
        event.addBean(bean);
      }
    }
    finally {
      lock.unlock();
    }
  }

  /**
   * Wraps the injection point, replacing the original {@link Property}
   * qualifier with one in which the {@link Property#name()} attribute
   * has been replaced with a unique identifier.  This ensures that every
   * injection point with our annotation is uniquely qualified to receive
   * the {@link PropertyBean} we will create for it.
   * 
   * @param injectionPoint target injection point
   * @return wrapped injection ponit
   */
  private InjectionPoint wrap(InjectionPoint injectionPoint) {
    Annotated annotated = injectionPoint.getAnnotated();
    Property qualifier = annotated.getAnnotation(Property.class);
    if (qualifier == null) throw new IllegalArgumentException();
    if (!qualifier.name().isEmpty()) return injectionPoint;
    
    return new PropertyInjectionPointWrapper(
        injectionPoint, new PropertyLiteral(qualifier, nextId++));
  }  

  /**
   * Gets the type of the injection point ensuring that is of {@link Class}
   * type.
   * @param injectionPoint subject injection point
   * @return injection point type
   */
  private Class<?> type(InjectionPoint injectionPoint) {
    if (!(injectionPoint.getType() instanceof Class<?>)) {
      throw new UnsupportedOperationException("@Property cannot be applied to " 
          + injectionPoint.getType());
    };
    Class<?> type = (Class<?>) injectionPoint.getType();
    return type;
  }

  /**
   * Determines the name of the property to inject at the given injection
   * point. 
   * @param injectionPoint the subject injection point
   * @param qualifier the qualifier applied to the injection point
   * @return property name
   */
  private String name(InjectionPoint injectionPoint, 
      Property qualifier) {
    String name = qualifier.name();
    if (name.isEmpty()) {
      name = fullyQualifiedMemberName(injectionPoint);
    }
    return name;
  }

  /**
   * Resolves a property name to a value.
   * @param name the name to resolve
   * @param qualifier qualifier from the injection point
   * @return resolved value
   * @throws UnresolvedPropertyException
   */
  private String resolve(String name, Property qualifier)
      throws UnresolvedPropertyException {
    String stringValue = resolver.resolve(name);
    if (stringValue == null) {
      if (qualifier.value().isEmpty()) {
        throw new UnresolvedPropertyException(name);
      }
      return qualifier.value();
    }
    return stringValue;
  }

  /**
   * Converts the string representation of a property to a given target type.
   * @param value the string representation to convert
   * @param type the target type
   * @param qualifier qualifier applied to the injection point
   * @param injectionPointName fully qualified name of the injection point
   * @return {@code value} converted to an instance of {@code type}
   * @throws UnsupportedTypeException
   * @throws NoSuchConverterException
   */
  private Object convert(String value, Class<?> type, Property qualifier, 
      String injectionPointName)
      throws UnsupportedTypeException, NoSuchConverterException {
    try {
      if (!qualifier.converter().isEmpty()) {
        return converter.convert(qualifier.converter(), value, type);
      }
      return converter.convert(value, type);
    }    
    catch (UnsupportedTypeException ex) {
      throw new UnsupportedTypeException(injectionPointName, type);
    }

  }

  /**
   * Gets the fully qualified member name of an injection point.
   * @param injectionPoint the subject injection point
   * @return qualified member name
   */
  private <T> String fullyQualifiedMemberName(InjectionPoint injectionPoint) {
    Member member = injectionPoint.getMember();
    String beanClassName  = member.getDeclaringClass().getName();
    String memberName = member.getName();
    return beanClassName + "." + memberName;
  }

  /**
   * Stores a property bean.
   * @param bean the bean to store
   */
  private void store(PropertyBean bean) {
    lock.lock();
    try {
      beans.add(bean);
    }
    finally {
      lock.unlock();
    }
  }

}
