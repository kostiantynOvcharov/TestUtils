package com.scc.ptl.testutils;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.scc.ptl.contracts.Contract;

import javax.annotation.Nullable;

import static com.scc.ptl.testutils.ReflectionUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EquatabilityTest implements ITest
{
   private final Class clazz;
   private final Iterable<MethodDescription> constructorsDescriptions;
   private final Iterable<String> equalityConstructorArgumentNames;

   public EquatabilityTest(
         Class clazz,
         Iterable<MethodDescription> constructorsDescriptions,
         Iterable<String> equalityConstructorArgumentNames)
   {
      Contract.requiresNotNull(clazz);
      Contract.requiresNotNull(constructorsDescriptions);
      Contract.requiresNotNull(equalityConstructorArgumentNames);
      this.clazz = clazz;
      this.constructorsDescriptions = constructorsDescriptions;
      this.equalityConstructorArgumentNames = equalityConstructorArgumentNames;
   }

   @Override
   public void execute(StringBuilder output)
   {
      for (MethodDescription constructorsDescription : constructorsDescriptions)
      {
         runEquatabilityTest(clazz, constructorsDescription, equalityConstructorArgumentNames, output);
      }
   }

   private void runEquatabilityTest(
         Class clazz,
         MethodDescription constructorsDescription,
         Iterable<String> equalityConstructorArgumentNames,
         StringBuilder output)
   {
      checkEqualityConstructorArgumentNames(constructorsDescription,equalityConstructorArgumentNames, output);

      List<ArgumentDescription> argumentDescriptions = constructorsDescription.getArgumentDescriptions();
      Object[][] validValueCortages;
      int numberOfUniqueValueCortages = 2;
      try
      {
         validValueCortages = TestUtils.getUniqueValidValueCorteges(argumentDescriptions, numberOfUniqueValueCortages, equalityConstructorArgumentNames);
      }
      catch (IllegalValueProviderException e)
      {
         output
               .append(String.format("Unable to perform equatability test for class '%1$s'", clazz))
               .append(String.format(" Reason: %1$s", e.getMessage()))
               .append(LINE_SEPARATOR);
         return;
      }

      Object[] referenceCortage = validValueCortages[0];
      Object[] sourceCortage = validValueCortages[1];

      Object referenceInstance = ReflectionUtils.getInstance(clazz, referenceCortage, argumentDescriptions, output);
      Object equalReferenceInstance = ReflectionUtils.getInstance(clazz, referenceCortage, argumentDescriptions, output);
      Object otherDifferentObject = new Object();

      boolean equatabilityBasicTestPassed = referenceInstance != null &&
            equalReferenceInstance != null &&
            !referenceInstance.equals(otherDifferentObject) &&
            !equalReferenceInstance.equals(otherDifferentObject) &&
            referenceInstance.equals(referenceInstance) &&
            referenceInstance.hashCode() == referenceInstance.hashCode() &&
            referenceInstance.equals(equalReferenceInstance) &&
            equalReferenceInstance.equals(referenceInstance) &&
            referenceInstance.hashCode() == equalReferenceInstance.hashCode();

      if (!equatabilityBasicTestPassed)
      {
         output
               .append(String.format("Class '%1$s', don't correctly define equatability: ", clazz))
               .append(LINE_SEPARATOR);
         return;
      }
      Iterable<TestStep> testPlan = getEquatabilityTestPlan(argumentDescriptions, equalityConstructorArgumentNames, referenceCortage, sourceCortage);
      for (TestStep testStep : testPlan)
      {
         Object testedInstance = ReflectionUtils.getInstance(clazz, testStep.argumentValues, argumentDescriptions, output);
         if (referenceInstance.equals(testedInstance) && testedInstance.equals(referenceInstance))
         {
            output
                  .append(String.format("Class '%1$s', doesn't correctly define equatability. ", clazz))
                  .append(String.format("Constructor argument '%1$s' has no impact on equality", testStep.testedArgumentName))
                  .append(LINE_SEPARATOR);
            return;
         }
      }
   }

   private void checkEqualityConstructorArgumentNames(MethodDescription constructorsDescription,
         Iterable<String> equalityConstructorArgumentNames, StringBuilder output)
   {
      List<ArgumentDescription> argumentDescriptions = constructorsDescription.getArgumentDescriptions();
      Collection<String> argumentNames = FluentIterable
            .from(argumentDescriptions)
            .transform(new Function<ArgumentDescription, String>()
            {
               @Override
               public String apply(ArgumentDescription argumentDescription)
               {
                  return argumentDescription.getArgumentName();
               }
            })
            .filter(Predicates.notNull())
            .toList();
      for (String equalityName : equalityConstructorArgumentNames)
      {
         if (!argumentNames.contains(equalityName))
         {
            output
                  .append(String.format("Can not find field '%1$s' in constructor description.", equalityName))
                  .append(LINE_SEPARATOR);
         }
      }
   }

   private Iterable<TestStep> getEquatabilityTestPlan(
         List<ArgumentDescription> argumentDescriptions,
         Iterable<String> equalityConstructorArgumentNames,
         Object[] referenceCortage,
         Object[] sourceCortage)
   {
      ArrayList<TestStep> testPlan = new ArrayList<TestStep>();
      for (String equalityConstructorArgumentName : equalityConstructorArgumentNames)
      {
         int argumentIndex = 0;
         for (ArgumentDescription argumentDescription : argumentDescriptions)
         {
            String argumentName = argumentDescription.getArgumentName();
            if (argumentName.equals(equalityConstructorArgumentName))
            {
               Object[] testedCortage = Arrays.copyOf(referenceCortage, referenceCortage.length);
               testedCortage[argumentIndex] = sourceCortage[argumentIndex];
               testPlan.add(new TestStep(testedCortage, argumentName));
               break;
            }
            argumentIndex++;
         }
      }
      return testPlan;
   }

   private static class TestStep
   {
      private final Object[] argumentValues;
      private final String testedArgumentName;

      private TestStep(Object[] argumentValues, String testedArgumentName)
      {
         this.argumentValues = argumentValues;
         this.testedArgumentName = testedArgumentName;
      }
   }
}
