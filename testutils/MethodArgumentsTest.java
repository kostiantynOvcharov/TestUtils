package com.scc.ptl.testutils;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.scc.ptl.contracts.Contract;

import javax.annotation.Nullable;

import static com.scc.ptl.testutils.ReflectionUtils.*;

import java.util.Arrays;
import java.util.List;

abstract class MethodArgumentsTest
{
   abstract Iterable<TestStep> getTestPlan(Object[] validValues, Iterable<ArgumentDescription> argumentDescriptions, IAction action, StringBuilder output);

   void run(IAction action, List<ArgumentDescription> argumentDescriptions, final StringBuilder output)
   {
      Object[] validValues = getValidValues(argumentDescriptions);
      String initialCheckError = initialCheck(action, validValues, output);
      if (!output.toString().contains(initialCheckError))
      {
         output.append(initialCheckError);
      }

      if (!Strings.isNullOrEmpty(initialCheckError))
      {
         return;
      }

      Iterable<TestStep> testPlan = getTestPlan(validValues, argumentDescriptions, action, output);
      for (TestStep testStep : testPlan)
      {
         Object[] testStepValues = testStep.getTestStepValues();
         Class expectedException = testStep.getExpectedException();
         try
         {
            action.invoke(testStepValues);
            if (expectedException != null)
            {
               output
                     .append(String.format("Problem with '%1$s', reason: %2$s",
                        action.getName(),
                        testStep.getExpectationMessage()))
                     .append(LINE_SEPARATOR);
            }
         }
         catch (Throwable e)
         {
            if (!Iterables.contains(Arrays.asList(action.getExpectedExceptions()), e.getClass()))
            {
               if (expectedException != null && !expectedException.isAssignableFrom(e.getClass()))
               {
                  output.append(testStep.getExpectationMessage()).append(LINE_SEPARATOR);
               }
               else if (expectedException == null )
               {
                  output
                        .append(String.format(
                              "Invocation error for method '%1$s' with argument values '%2$s', reason: %3$s",
                              action.getName(),
                              Arrays.toString(testStepValues),
                              e.getMessage()))
                        .append(LINE_SEPARATOR);
               }
            }
         }
      }
   }

   private String initialCheck(IAction action, Object[] arguments, final StringBuilder output)
   {
      try
      {
         action.invoke(arguments);
      }
      catch (Throwable e)
      {
         if (action.getExpectedExceptions() != null && !Iterables.contains(Arrays.asList(action.getExpectedExceptions()), e.getClass()))
         {
            String causeMessage;
            if (e.getCause() != null && e.getCause().getMessage() != null)
            {
               causeMessage = e.getCause().getMessage();
            }
            else if (e.getMessage() != null)
            {
               causeMessage = e.getMessage();
            }
            else
            {
               causeMessage = e.toString();
            }

            output
               .append(String.format(
                     "Unable to perform valid call for method '%1$s' with argument values '%2$s', reason: %3$s",
                     action.getName(),
                     Arrays.toString(arguments),
                     causeMessage))
               .append(LINE_SEPARATOR);
         }
      }
      return output.toString();
   }

   private Object[] getValidValues(List<ArgumentDescription> argumentDescriptions)
   {
      Object[] values = new Object[argumentDescriptions.size()];
      int i = 0;
      for (ArgumentDescription argumentDescription : argumentDescriptions)
      {
         Iterable<?> validValue = argumentDescription.getValueProvider().getValidValues();
         values[i] = (validValue == null) ? null : Iterables.getFirst(validValue, null);
         i++;
      }
      return values;
   }

   static class TestStep
   {
      private final String expectationMessage;
      private final Object[] testStepValues;
      private final Class expectedException;

      public TestStep(Object[] testStepValues)
      {
         this(null, testStepValues, null);
      }

      TestStep(final @Nullable String expectationMessage, final Object[] testStepValues, final @Nullable Class expectedException)
      {
         Contract.requiresNotNull(testStepValues);
         this.expectationMessage = expectationMessage;
         this.testStepValues = Arrays.copyOf(testStepValues, testStepValues.length);
         this.expectedException = expectedException;
      }

      String getExpectationMessage()
      {
         return expectationMessage;
      }

      Object[] getTestStepValues()
      {
         return testStepValues.clone();
      }

      Class getExpectedException()
      {
         return expectedException;
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o)
            return true;
         if (o == null || getClass() != o.getClass())
            return false;

         TestStep testStep = (TestStep) o;
         return Arrays.equals(testStepValues, testStep.testStepValues);
      }

      @Override
      public int hashCode()
      {
         return Arrays.hashCode(testStepValues);
      }
   }
}
