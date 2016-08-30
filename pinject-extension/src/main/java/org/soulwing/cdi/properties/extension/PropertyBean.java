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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.soulwing.cdi.properties.Property;

/**
 * A {@link Bean} that represents an injected property value.
 *
 * @author Carl Harris
 */
class PropertyBean implements Bean<Object> {

  private final Object value; 
  private final Class<?> type;
  private final Set<Annotation> qualifiers;
  
  /**
   * Constructs a new instance.
   * @param value property value
   * @param type property type
   * @param qualifiers a set of qualifiers which <em>must</em> include
   *    a {@link Property} qualifier that <em>uniquely</em> identifies the
   *    injection point
   */
  PropertyBean(Object value, Class<?> type, Set<Annotation> qualifiers) {
    this.value = value;
    this.type = type;
    this.qualifiers = qualifiers;
  }

  @Override
  public Object create(CreationalContext<Object> ctx) {
    return value;
  }

  @Override
  public void destroy(Object instance, CreationalContext<Object> ctx) {
    // nothing to do for beans of this type
  }

  @Override
  public String getName() {
    return null;  // properties don't have an EL name
  }

  @Override
  public Set<Annotation> getQualifiers() {
    return qualifiers;    
  }

  @Override
  public Class<? extends Annotation> getScope() {
    return Dependent.class;
  }

  @Override
  public Set<Class<? extends Annotation>> getStereotypes() {
    return Collections.emptySet();
  }

  @Override
  public Set<Type> getTypes() {
    Set<Type> types = new HashSet<>();
    types.add(type);
    types.add(Object.class);
    return types;
  }

  @Override
  public boolean isAlternative() {
    return false;
  }

  @Override
  public Class<?> getBeanClass() {
    return type;
  }

  @Override
  public Set<InjectionPoint> getInjectionPoints() {
    return Collections.emptySet();
  }

  @Override
  public boolean isNullable() {
    // As of CDI 1.1 the result here is irrelevant    
    // @see javax.enterprise.inject.spi.Bean#isNullable()
    return false;  
  }

}
