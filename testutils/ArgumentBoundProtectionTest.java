package com.scc.ptl.testutils;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

final class ArgumentBoundProtectionTest extends MethodArgumentsTest
{
   private static ArgumentBoundProtectionTest instance;

   private ArgumentBoundProtectionTest()
   {
   }

   static ArgumentBoundProtectionTest getInstance()
   {
      return instance == null ? new ArgumentBoundProtectionTest() : instance;
   }

   @Override
   Iterable<TestStep> getTestPlan(
      Object[] validValues,
      Iterable<ArgumentDescription> argumentDescriptions,
      IAction action,
      StringBuilder output)
   {
      HashSet<TestStep> testPlan = new HashSet<TestStep>();
      int testedArgumentIndex = 0;
      for (ArgumentDescription argumentDescription : argumentDescriptions)
      {
         if (argumentDescription.getValueProvider() instanceof IBoundValueProvider)
         {
            IBoundValueProvider boundValueProvider = (IBoundValueProvider) argumentDescription.getValueProvider();
            collectArgumentTestPlan(testPlan, validValues, testedArgumentIndex, argumentDescription, boundValueProvider);
         }

         testedArgumentIndex++;
      }

      // test step with valid values isn't intended to be tested by MethodArgumentsTest
      TestStep validTestStep = new TestStep(validValues);
      testPlan.remove(validTestStep);
      return testPlan;
   }

   private void collectArgumentTestPlan(
      HashSet<TestStep> testPlan,
      Object[] validValues,
      int testedArgumentIndex,
      ArgumentDescription argumentDescription,
      IBoundValueProvider boundValueProvider)
   {
      Iterable inRangeValues = Iterables.concat(
            boundValueProvider.getLowerBoundValues(),
            boundValueProvider.getOverLowerBoundValues(),
            boundValueProvider.getUnderUpperBoundValues(),
            boundValueProvider.getUpperBoundValues()
      );
      collectInRangeValuesTestPlan(testPlan, validValues, testedArgumentIndex, argumentDescription, inRangeValues);

      Iterable outOfRangeValues = Iterables.concat(
            boundValueProvider.getUnderLowerBoundValues(),
            boundValueProvider.getOverUpperBoundValues()
      );
      collectOutOfRangeValueProtectedTestSteps(testPlan, validValues, testedArgumentIndex, argumentDescription, outOfRangeValues);
   }

   private void collectInRangeValuesTestPlan(
      HashSet<TestStep> testPlan,
      Object[] validValues,
      int testedArgumentIndex,
      ArgumentDescription argumentDescription,
      Iterable inRangeValues)
   {
      Object[] testStepValues;
      String expectationMessage;

      for (Object value : inRangeValues)
      {
         testStepValues = Arrays.copyOf(validValues, validValues.length);
         testStepValues[testedArgumentIndex] = value;
         expectationMessage =
               !Strings.isNullOrEmpty(argumentDescription.getArgumentName())
                     ? String.format("Argument '%1$s' must allow value '%2$s'", argumentDescription.getArgumentName(), value)
                     : String.format("Argument with index '%1$s' and type '%2$s' must allow value '%3$s'", testedArgumentIndex, value.getClass(), value);
         testPlan.add(new TestStep(expectationMessage, testStepValues, null));
      }
   }

   private void collectOutOfRangeValueProtectedTestSteps(
      HashSet<TestStep> testPlan,
      Object[] validValues,
      int testedArgumentIndex,
      ArgumentDescription argumentDescription,
      Iterable outOfRangeValues)
   {
      Object[] testStepValues;
      String expectationMessage;

      for (Object value : outOfRangeValues)
      {
         testStepValues = Arrays.copyOf(validValues, validValues.length);
         testStepValues[testedArgumentIndex] = value;
         expectationMessage =
               !Strings.isNullOrEmpty(argumentDescription.getArgumentName())
                     ? String.format("Argument '%1$s' must be protected against value '%2$s'", argumentDescription.getArgumentName(), value)
                     : String.format("Argument with index '%1$s' and type '%2$s' must be protected against value '%3$s'", testedArgumentIndex, value.getClass(), value);
         testPlan.add(new TestStep(expectationMessage, testStepValues, IllegalArgumentException.class));
      }
   }
}
