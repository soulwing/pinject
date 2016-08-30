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

import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.PropertyNotFoundException;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

/**
 * An {@link ExpressionEvaluator} implemented using unified EL.
 *
 * @author Carl Harris
 */
class ELExpressionEvaluator implements ExpressionEvaluator {

  private static final Logger logger = Logger.getLogger(
      ELExpressionEvaluator.class.getName());

  private static final String LOCAL_NAME_REQUIRED = "required";
  private static final String LOCAL_NAME_OPTIONAL = "optional";
  private static final String PREFIX_PROPERTIES = "p";
  private static final String PREFIX_ENVIRONMENT = "e";

  private ExpressionFactory expressionFactory;

  private ELContext context;

  @Override
  public void init() {
    final ClassLoader previousTccl =
        Thread.currentThread().getContextClassLoader();
    try {
      if (previousTccl == null) {
        Thread.currentThread().setContextClassLoader(
            getClass().getClassLoader());
      }
      expressionFactory = ExpressionFactory.newInstance();
      logger.fine("loaded expression factory provider "
          + expressionFactory.getClass().getName());
      logger.fine("expression factory loaded via class loader "
          + getClass().getClassLoader());

      context = new StandardELContext(expressionFactory);

      final FunctionMapper functionMapper = context.getFunctionMapper();
      functionMapper.mapFunction(PREFIX_PROPERTIES, LOCAL_NAME_REQUIRED,
          PropertiesFunctions.class.getMethod("get", String.class));
      functionMapper.mapFunction(PREFIX_PROPERTIES, LOCAL_NAME_OPTIONAL,
          PropertiesFunctions.class.getMethod("getOptional", String.class,
              String.class));
      functionMapper.mapFunction(PREFIX_ENVIRONMENT, LOCAL_NAME_REQUIRED,
          EnvironmentFunctions.class.getMethod("get", String.class));
      functionMapper.mapFunction(PREFIX_ENVIRONMENT, LOCAL_NAME_OPTIONAL,
          EnvironmentFunctions.class.getMethod("getOptional", String.class,
              String.class));
    }
    catch (NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    }
    finally {
      Thread.currentThread().setContextClassLoader(previousTccl);
    }
  }

  @Override
  public String evaluate(String expression, PropertyValueResolver resolver)
      throws UnresolvedExpressionException {
    PropertiesFunctions.setResolver(resolver);
    try {
      ValueExpression ve = expressionFactory.createValueExpression(
          context, expression, String.class);

      String value = null;
      try {
        value = (String) ve.getValue(context);
      }
      catch (ELException ex) {
        if (ex.getCause() instanceof PropertyNotFoundException) {
          throw new UnresolvedExpressionException(
              "expression contains unresolved values: " + expression);
        }
        throw ex;
      }

      if (value == null) {
        throw new UnresolvedExpressionException(
            "expression evaluates to a null value: " + expression);
      }

      ve = expressionFactory.createValueExpression(context, value, String.class);
      if (!ve.isLiteralText()) {
        throw new UnresolvedExpressionException(
            "expression contains unresolved values: " +  expression + " => " + value);
      }
      return value;
    }
    finally {
      PropertiesFunctions.clearResolver();
    }
  }

}
