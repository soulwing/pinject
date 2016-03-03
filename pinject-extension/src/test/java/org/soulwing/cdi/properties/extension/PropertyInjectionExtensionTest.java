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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the CDI-based property injection.
 *
 * @author Carl Harris
 */
public class PropertyInjectionExtensionTest {

  private static final String SYSTEM_PROPERTY_VALUE = "some system property";
  private Weld weld;
  private WeldContainer container;
  
  @Before
  public void setUp() throws Exception {
    System.setProperty(
        FieldInjectionTargetBean.class.getName() + ".systemProperty", 
        SYSTEM_PROPERTY_VALUE);
    weld = new Weld();
    container = weld.initialize();    
  }
  
  @After
  public void tearDown() throws Exception {
    weld.shutdown();
  }
  
  @Test
  public void testInject() throws Exception {
    FieldInjectionTargetBean bean = 
        container.instance().select(FieldInjectionTargetBean.class).get();
    assertThat(bean, is(not(nullValue())));
    assertThat(bean.stringProperty, is(equalTo("some value")));
    assertThat(bean.anotherStringProperty, is(equalTo("referenced some value")));
    assertThat(bean.integerProperty, is(equalTo(42)));
    assertThat(bean.systemProperty, is(equalTo(SYSTEM_PROPERTY_VALUE)));
    assertThat(bean.intProperty, is(equalTo(99)));
    assertThat(bean.path, is(not(nullValue())));
    System.out.println(bean.path);
  }
}
