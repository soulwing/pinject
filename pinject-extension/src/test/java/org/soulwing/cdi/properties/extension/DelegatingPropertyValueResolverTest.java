/*
 * File created on Aug 28, 2014 
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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soulwing.cdi.properties.resolvers.BeansProperties;
import org.soulwing.cdi.properties.resolvers.MetaInfBeanPropertiesResolver;
import org.soulwing.cdi.properties.support.ClassLoaderUtil;

/**
 * Tests for {@link DelegatingPropertyValueResolver}.
 * <p>
 * This test simply exercises the built-in resolvers to ensure that the
 * services configuration specified in {@code META-INF/services} is correct.

 * @author Carl Harris
 */
public class DelegatingPropertyValueResolverTest {

  private static final String ROOT_PROPERTY = "testRootProperty";
  private static final String PACKAGE_NAME = "package";
  private static final String PACKAGE_PROPERTY = "testPackageProperty";
  private static final String META_INF_PROPERTY = "testMetaInfProperty";
  private static final String SHARED_PROPERTY = "testSharedProperty";
  private static final String SYSTEM_PROPERTY = "systemProperty";
  
  private ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();

  private DelegatingPropertyValueResolver resolver =
      new DelegatingPropertyValueResolver();
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    LogManagerUtil.configure();
  }

  @Before
  public void setUp() throws Exception {
    classLoaderUtil.setUp(getClass());
    resolver.init();
    System.setProperty(SYSTEM_PROPERTY, SYSTEM_PROPERTY);
  }

  @After
  public void tearDown() throws Exception {
    resolver.destroy();
    classLoaderUtil.tearDown();
    System.clearProperty(SYSTEM_PROPERTY);
  }

  @Test
  public void testClassLoaderSetUp() throws Exception {
    assertThat(Thread.currentThread().getContextClassLoader()
        .getResource(BeansProperties.NAME), is(not(nullValue())));
    assertThat(Thread.currentThread().getContextClassLoader()
        .getResource(MetaInfBeanPropertiesResolver.META_INF_BEANS_PROPERTIES), 
        is(not(nullValue())));
  }
  
  @Test
  public void testResolvers() throws Exception {
    
    // this property is defined in beans.properties
    assertThat(resolver.resolve(ROOT_PROPERTY), 
        is(equalTo(ROOT_PROPERTY)));

    // this property is defined in package/beans.properties
    assertThat(resolver.resolve(PACKAGE_NAME + "." + PACKAGE_PROPERTY), 
        is(equalTo(PACKAGE_PROPERTY)));

    // this property is defined in META-INF/beans.properties
    assertThat(resolver.resolve(META_INF_PROPERTY), 
        is(equalTo(META_INF_PROPERTY)));
    
    // this property is defined in both beans.properties and
    // META-INF/beans.properties (the latter should override)
    assertThat(resolver.resolve(SHARED_PROPERTY), 
        is(equalTo(META_INF_PROPERTY)));
    
    // this property is defined as a system property
    assertThat(resolver.resolve(SYSTEM_PROPERTY),
        is(equalTo(SYSTEM_PROPERTY)));
    
    // this property is not defined anywhere
    assertThat(resolver.resolve("DOES_NOT_EXIST"), is(nullValue()));
  }
  
}
