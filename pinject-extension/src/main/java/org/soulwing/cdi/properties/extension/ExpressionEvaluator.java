/*
 * File created on Mar 1, 2016
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

/**
 * An expression evaluator.
 *
 * @author Carl Harris
 */
interface ExpressionEvaluator {

  /**
   * Initializes this expression evaluator.
   * @throws Exception if initialization fails
   */
  void init() throws Exception;

  /**
   * Evaluates an expression to produce a string.
   * <p>
   * If the evaluation of an expression produces another expression, it is
   * recursively evaluated.
   *
   * @param expression the expression to evaluate
   * @param resolver property value resolver
   *
   * @return evaluated expression
   * @throws UnresolvedExpressionException if the expression evaluates to a null value
   */
  String evaluate(String expression, PropertyValueResolver resolver)
      throws UnresolvedExpressionException;

}
