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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.soulwing.cdi.properties.Property;

/**
 * A bean that is used to test property inject into a field.
 *
 * @author Carl Harris
 */
@SuppressWarnings({"CdiInjectionPointsInspection", "CanBeFinal"})
@Dependent
class FieldInjectionTargetBean {

  @Inject @Property
  public String stringProperty;
  
  @Inject @Property
  public String anotherStringProperty;
  
  @Inject @Property
  public int intProperty;
  
  @Inject @Property("42")
  public Integer integerProperty;
  
  @Inject @Property
  public String systemProperty;

  // tests an EL-expression that uses an environment function
  @Inject @Property("#{e:required('PATH')}")
  public String path;

}
