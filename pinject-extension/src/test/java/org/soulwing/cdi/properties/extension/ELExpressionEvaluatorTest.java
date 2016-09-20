/*
 * File created on Sep 20, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.cdi.properties.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for {@link ELExpressionEvaluator}.
 *
 * @author Carl Harris
 */
public class ELExpressionEvaluatorTest {

  private static final String ESCAPED_STRING = "\\w";

  private static final String DOUBLE_ESCAPED_STRING = "\\\\w";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  PropertyValueResolver resolver;

  private ELExpressionEvaluator evaluator;

  @Before
  public void setUp() throws Exception {
    evaluator = new ELExpressionEvaluator();
    evaluator.init();
  }

  @Test
  public void testEvaluateSimpleExpression() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(resolver).resolve("propertyName");
        will(returnValue("propertyValue"));
      }
    });

    assertThat(evaluator.evaluate("${p:required('propertyName')}", resolver),
        is(equalTo("propertyValue")));
  }

  @Test
  public void testEvaluateEscapedPropertyValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(resolver).resolve("propertyName");
        will(returnValue(ESCAPED_STRING));
      }
    });

    assertThat(evaluator.evaluate("${p:required('propertyName')}", resolver),
        is(equalTo(ESCAPED_STRING)));
  }

  @Test
  public void testEvaluateDoubleEscapedPropertyValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(resolver).resolve("propertyName");
        will(returnValue(DOUBLE_ESCAPED_STRING));
      }
    });

    assertThat(evaluator.evaluate("${p:required('propertyName')}", resolver),
        is(equalTo(DOUBLE_ESCAPED_STRING)));
  }

}
