/*
 * File created on Aug 30, 2014 
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
package org.soulwing.cdi.properties.support;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Allows the classpath to be manipulated to make test resources in a given
 * source folder appear to be at the root of classpath.
 *
 * @author Carl Harris
 */
public class ClassLoaderUtil {

  private ClassLoader tccl;
  private URLClassLoader classLoader;
  
  public void setUp(Class<?> testClass) throws Exception {
    URL testResources = testClass.getClassLoader().
        getResource(testClass.getSimpleName());
    if (testResources == null) {
      throw new IllegalArgumentException(
          testClass.getSimpleName() + " does not have any test resources");
    }
    URL url = new URL(testResources + "/");
    tccl = Thread.currentThread().getContextClassLoader();
    classLoader = new URLClassLoader(new URL[] { url }, 
        testClass.getClassLoader());
    Thread.currentThread().setContextClassLoader(classLoader);
  }
  
  public void tearDown() throws Exception {
    if (tccl == null) {
      throw new IllegalStateException();
    }
    Thread.currentThread().setContextClassLoader(tccl);
    classLoader.close();
  }

}
