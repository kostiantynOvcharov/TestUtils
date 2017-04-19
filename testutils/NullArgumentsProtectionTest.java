package com.scc.ptl.testutils;

import com.google.common.base.Strings;
import com.google.common.primitives.Primitives;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

final class NullArgumentsProtectionTest extends MethodArgumentsTest
{
   private static NullArgumentsProtectionTest instance;

   private NullArgumentsProtectionTest()
   {
   }

   static NullArgumentsProtectionTest getInstance()
   {
      return instance == null ? new NullArgumentsProtectionTest() : instance;
   }

   @Override
   Iterable<TestStep> getTestPlan(Object[] validValues, Iterable<ArgumentDescription> argumentDescriptions,
         IAction action, StringBuilder output)
   {
      HashSet<TestStep> testPlan = new HashSet<TestStep>();
      ArgumentDefinition[] arguments = action.getArguments();
      int testedArgumentIndex = 0;
      for (ArgumentDescription argumentDescription : argumentDescriptions)
      {
         if (!arguments[testedArgumentIndex].getType().isPrimitive())
         {
            try
            {
               testPlan.add(getArgumentTestStep(validValues, testedArgumentIndex, argumentDescription, arguments[testedArgumentIndex]));
            }
            catch (ArgumentDeclarationException e)
            {
               output.append(e.getMessage());
            }
         }
         testedArgumentIndex++;
      }

      // test step with valid values isn't intended to be tested by MethodArgumentsTest
      TestStep validTestStep = new TestStep(validValues);
      testPlan.remove(validTestStep);
      return testPlan;
   }

   private TestStep getArgumentTestStep(Object[] validValues, int testedArgumentIndex, ArgumentDescription argumentDescription,
         ArgumentDefinition argumentDefinition)
   {
      return argumentHasNullProtection(argumentDefinition)
            ? getNullProtectedArgumentTestStep(validValues, testedArgumentIndex, argumentDescription)
            : getNullAllowedArgumentTestStep(validValues, testedArgumentIndex, argumentDescription);
   }

   private TestStep getNullProtectedArgumentTestStep(Object[] validValues, int testedArgumentIndex, ArgumentDescription argumentDescription)
   {
      Object[] testStepValues = Arrays.copyOf(validValues, validValues.length);
      testStepValues[testedArgumentIndex] = null;

      String expectationMessage =
            !Strings.isNullOrEmpty(argumentDescription.getArgumentName())
                  ? String.format("Argument '%1$s' must be protected against null value", argumentDescription.getArgumentName())
                  : String.format("Argument with index '%1$s' and type '%2$s' must be protected against null value", testedArgumentIndex, validValues[testedArgumentIndex].getClass());

      return new TestStep(expectationMessage, testStepValues, IllegalArgumentException.class);
   }

   private TestStep getNullAllowedArgumentTestStep(Object[] validValues, int testedArgumentIndex, ArgumentDescription argumentDescription)
   {
      Object[] testStepValues = Arrays.copyOf(validValues, validValues.length);
      testStepValues[testedArgumentIndex] = null;

      String expectationMessage =
            !Strings.isNullOrEmpty(argumentDescription.getArgumentName())
                  ? String.format("Argument '%1$s' must allow null value", argumentDescription.getArgumentName())
                  : String.format("Argument with index '%1$s' and type '%2$s' must allow null value", testedArgumentIndex, validValues[testedArgumentIndex].getClass());

      return new TestStep(expectationMessage, testStepValues, null);
   }

   private boolean argumentHasNullProtection(ArgumentDefinition argument)
   {
      boolean argumentHasNullableAnnotation = argument.hasAnnotation(Nullable.class);
      if (argumentHasNullableAnnotation && Primitives.isWrapperType(argument.getType()))
      {
         throw new ArgumentDeclarationException("Primitive can't have annotation Nullable");
      }
      return !argumentHasNullableAnnotation;
   }
}
