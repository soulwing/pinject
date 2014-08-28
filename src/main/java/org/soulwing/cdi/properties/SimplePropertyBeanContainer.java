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

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AfterBeanDiscovery;

/**
 * A {@link PropertyBeanContainer} backed by a {@link Set}.
 *
 * @author Carl Harris
 */
public class SimplePropertyBeanContainer implements PropertyBeanContainer {

  private final Set<PropertyBean> beans = new HashSet<>();

  private final PropertyValueResolver resolver;
  private final PropertyValueConverter converter;
  
  public SimplePropertyBeanContainer() {
    this(new DelegatingPropertyValueResolver(), new DelegatingPropertyValueConverter());
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
  public void add(String name, Class<?> type, Property qualifier) 
      throws UnresolvedPropertyException, NoSuchConverterException,
      UnsupportedTypeException {
    
    String stringValue = resolver.resolve(name);
    if (stringValue == null) {
      if (qualifier.value().isEmpty()) {
        throw new UnresolvedPropertyException(name);
      }
      stringValue = qualifier.value();
    }

    Object value = null;
    if (qualifier.converter().isEmpty()) {
      value = converter.convert(stringValue, type);
    }
    else {
      value = converter.convert(qualifier.converter(), stringValue, type);
    }
    
    beans.add(new PropertyBean(value, type, qualifier));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copyAll(AfterBeanDiscovery event) {
    for (PropertyBean bean : beans) {
      event.addBean(bean);
    }
  }

}
