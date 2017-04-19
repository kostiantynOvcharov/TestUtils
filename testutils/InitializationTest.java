package com.scc.ptl.testutils;

import com.google.common.collect.Iterables;
import com.scc.ptl.contracts.Contract;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.scc.ptl.testutils.ReflectionUtils.LINE_SEPARATOR;
import static com.scc.ptl.testutils.ReflectionUtils.getInstance;

public final class InitializationTest implements ITest
{
   private final Class clazz;
   private final Iterable<MethodDescription> constructorDescriptions;

   public InitializationTest(Class clazz, Iterable<MethodDescription> constructorDescriptions){
      Contract.requiresNotNull(clazz);
      Contract.requiresNotNull(constructorDescriptions);
      this.clazz = clazz;
      this.constructorDescriptions = constructorDescriptions;
   }

   @Override
   public void execute(StringBuilder output)
   {
      for (MethodDescription constructorDescription : constructorDescriptions)
      {
         runInitializationTest(clazz, constructorDescription, output);
      }
   }

   private void runInitializationTest(Class clazz, MethodDescription constructorDescription, StringBuilder output)
   {
      List<ArgumentDescription> argumentDescriptions = constructorDescription.getArgumentDescriptions();
      if (!verifyArgumentNames(argumentDescriptions, output))
      {
         return;
      }

      Object[][] validValueCortages;
      int numberOfUniqueValueCortages = 2;

      try
      {
         validValueCortages = TestUtils.getUniqueValidValueCorteges(argumentDescriptions, numberOfUniqueValueCortages);
      }
      catch (IllegalValueProviderException e)
      {
         output
               .append(String.format("Unable to perform initialization test for class '%1$s', reason: %2$s",
                     clazz,
                     e.getMessage()))
               .append(LINE_SEPARATOR);
         return;
      }

      Object[] firstCortage = validValueCortages[0];
      Object[] secondCortage = validValueCortages[1];

      Object firstInstance = getInstance(clazz, firstCortage, argumentDescriptions, output);
      Object secondInstance = getInstance(clazz, secondCortage, argumentDescriptions, output);

      verifyAccordance(clazz, firstInstance, firstCortage, argumentDescriptions, output);
      verifyAccordance(clazz, secondInstance, secondCortage, argumentDescriptions, output);
   }

   private boolean verifyArgumentNames(Iterable<ArgumentDescription> argumentDescriptions, StringBuilder output)
   {
      boolean result = true;
      int argumentIndex = 0;
      for (ArgumentDescription argumentDescription : argumentDescriptions)
      {
         if (StringUtils.isBlank(argumentDescription.getArgumentName()))
         {
            output
                  .append(
                        String.format("InitializationTest: please, provide argument name for argument with index '%1$s' and type '%2$s'",
                              argumentIndex,
                              argumentDescription.getValueProvider().getClazz()))
                  .append(LINE_SEPARATOR);
            result = false;
         }
         argumentIndex++;
      }
      return result;
   }

   private void verifyAccordance(
         Class clazz,
         Object instance,
         Object[] argumentCortage,
         List<ArgumentDescription> argumentDescriptions,
         StringBuilder output)
   {
      Iterable<TestStep> testPlan = getInitializationTestPlan(argumentDescriptions, output);
      if (instance == null || argumentDescriptions.size() != Iterables.size(testPlan))
      {
         return;
      }

      for (TestStep testStep : testPlan)
      {
         Object getterResult = null;
         try
         {
            getterResult = testStep.getter.invoke(instance, null);
         }
         catch (Exception e)
         {
            output
                  .append(String.format("Unable to perform initialization test for class '%1$s', reason: %2$s",
                        clazz,
                        e.getMessage()))
                  .append(LINE_SEPARATOR);
            continue;
         }

         Object fieldValue = argumentCortage[testStep.argumentIndex];

         if (fieldValue == null && getterResult != null)
         {
            output
                  .append(String.format("Provided value is null but value that returned from getter is '%1$s' for argument '%2$s'",
                        getterResult,
                        testStep.argumentName))
                  .append(LINE_SEPARATOR);
         }

         if (fieldValue != null && getterResult == null)
         {
            output
                  .append(String.format("Provided value is '%1$s' but value that returned from getter is null for argument '%2$s'",
                        fieldValue,
                        testStep.argumentName))
                  .append(LINE_SEPARATOR);
         }

         if (getterResult != null && fieldValue != null)
         {
            if (argumentDescriptions.get(testStep.argumentIndex).getValueProvider().getClazz() != Iterable.class)
            {
               if (!getterResult.equals(fieldValue))
               {
                  output
                        .append(String.format("Class '%1$s' can't return value '%2$s' for argument '%3$s' with type '%4$s'",
                              clazz,
                              fieldValue,
                              testStep.argumentName,
                              fieldValue.getClass()))
                        .append(LINE_SEPARATOR);
               }
            }
            else
            {
               Iterable castFieldElement = null;
               Iterable castGetterElement = null;
               try
               {
                  castFieldElement = (Iterable) fieldValue;
                  castGetterElement = (Iterable) getterResult;
               }
               catch (ClassCastException e)
               {
                  output
                        .append(String.format("Can't cast '%1$s' or '%2$s' to Iterable.class for argument '%3$s'",
                              fieldValue,
                              getterResult,
                              testStep.argumentName))
                        .append(LINE_SEPARATOR);
               }
               if (!Iterables.elementsEqual(castFieldElement, castGetterElement))
               {
                  output
                        .append(String.format(
                              "Provided Iterable '%1$s' is not equal to the Iterable '%2$s' returned from getter for argument '%3$s'",
                              castFieldElement,
                              castGetterElement,
                              testStep.argumentName))
                        .append(LINE_SEPARATOR);
               }
            }
         }
      }
   }

   private Iterable<TestStep> getInitializationTestPlan(List<ArgumentDescription> argumentDescriptions, StringBuilder output)
   {
      ArrayList<TestStep> testPlan = new ArrayList<TestStep>();
      int testedArgumentIndex = 0;

      for (ArgumentDescription argumentDescription : argumentDescriptions)
      {
         String argumentName = argumentDescription.getArgumentName();
         String getterName = TestUtils.getGetterName(argumentName);
         Method getter = ReflectionUtils.getMethod(clazz, getterName, output);
         if (getter != null)
         {
            testPlan.add(new TestStep(argumentName, testedArgumentIndex, getter));
         }
         testedArgumentIndex++;

      }
      return testPlan;
   }

   private static class TestStep
   {
      private final String argumentName;
      private final int argumentIndex;
      private final Method getter;

      private TestStep(String argumentName, int argumentIndex, Method getter)
      {
         this.argumentName = argumentName;
         this.argumentIndex = argumentIndex;
         this.getter = getter;
      }
   }
}
