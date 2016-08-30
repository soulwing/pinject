/*
 * File created on Aug 29, 2014 
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
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.soulwing.cdi.properties.Property;

/**
 * A wrapper for an {@link InjectionPoint} that will be injected with a
 * property value.  
 * <p>
 * This wrapper is used to replace the original {@link Property} qualifier
 * with one that uniquely identifies the injection point.
 *
 * @author Carl Harris
 */
class PropertyInjectionPointWrapper implements InjectionPoint {

  private final Set<Annotation> qualifiers = new HashSet<>();
  
  private final InjectionPoint delegate;
  
  /**
   * Constructs a new instance.
   * @param delegate delegate injection point
   * @param wrappedQualifier property annotation wrapped such that it its
   *    name defaults to a unique value
   */
  PropertyInjectionPointWrapper(InjectionPoint delegate,
      Property wrappedQualifier) {
    qualifiers.addAll(delegate.getQualifiers());
    qualifiers.remove(delegate.getAnnotated().getAnnotation(Property.class));
    qualifiers.add(wrappedQualifier);
    this.delegate = delegate;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Annotated getAnnotated() {
    return delegate.getAnnotated();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bean<?> getBean() {
    return delegate.getBean();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Member getMember() {
    return delegate.getMember();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Annotation> getQualifiers() {
    return qualifiers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getType() {
    return delegate.getType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDelegate() {
    return delegate.isDelegate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTransient() {
    return delegate.isTransient();
  }
  
}
