/*
 * File created on Aug 11, 2026
 *
 * Copyright (c) 2026 Carl Harris, Jr.
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
 * A bean that is used to test injection of multiple distinct properties
 * into a single constructor.
 *
 * @author Carl Harris
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@Dependent
class ConstructorInjectionTargetBean {

  public final String firstProperty;
  public final String secondProperty;

  @Inject
  ConstructorInjectionTargetBean(
      @Property(name = "constructorFirstProperty") String firstProperty,
      @Property(name = "constructorSecondProperty") String secondProperty) {
    this.firstProperty = firstProperty;
    this.secondProperty = secondProperty;
  }

}
