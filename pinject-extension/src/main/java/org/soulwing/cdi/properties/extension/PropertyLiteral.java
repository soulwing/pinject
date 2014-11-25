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
import java.util.UUID;

import javax.enterprise.util.AnnotationLiteral;

import org.soulwing.cdi.properties.Property;

/**
 * A {@link Property} whose {@code name} attribute is a UUID.
 *
 * @author Carl Harris
 */
@SuppressWarnings("all")
public class PropertyLiteral extends AnnotationLiteral<Property> 
    implements Property {

  private static final long serialVersionUID = -5694363040452378974L;
  
  private final Property qualifier;

  private final long id;

  /**
   * Constructs a new instance.
   * @param qualifier
   * @param id unique identifier assigned to this qualifer
   */
  public PropertyLiteral(Property qualifier, long id) {
    if (qualifier == null) {
      throw new NullPointerException("qualifier is required");
    }
    this.qualifier = qualifier;
    this.id = id;
  }

  @Override
  public String name() {
    return Long.toString(id);
  }

  @Override
  public String value() {
    return qualifier.value();
  }

  @Override
  public String converter() {
    return qualifier.converter();
  }

}
