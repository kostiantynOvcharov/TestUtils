package com.scc.ptl.testutils;

import com.google.common.base.Strings;
import com.scc.ptl.contracts.Contract;
import org.apache.commons.lang.StringUtils;
import org.mutabilitydetector.unittesting.MutabilityAssert;
import static com.scc.ptl.testutils.ReflectionUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ClassAsserter implements IClassDescriber<ClassAsserter>
{
   private final Class clazz;
   private final Collection<MethodDescription> constructorDescriptions = new ArrayList<MethodDescription>();
   private final Collection<MethodDescription> staticMethodDescriptions = new ArrayList<MethodDescription>();
   private final Collection<MethodDescription> methodDescriptions = new ArrayList<MethodDescription>();
   private final Collection<ITest> tests = new ArrayList<ITest>();
   private final StringBuilder output = new StringBuilder();

   public ClassAsserter(Class clazz)
   {
      Contract.requiresNotNull(clazz);
      this.clazz = clazz;
   }

   public ClassAsserter isImmutable()
   {
      MutabilityAssert.assertImmutable(clazz);
      return this;
   }

   public ClassAsserter protectedAgainstNullArguments()
   {
      NullArgumentsProtectionTest test = NullArgumentsProtectionTest.getInstance();

      if (!constructorDescriptions.isEmpty())
      {
         tests.add(new ClassArgumentsTest(clazz, constructorDescriptions, test, methodDescriptions));
      }

      if (!staticMethodDescriptions.isEmpty())
      {
         tests.add(new StaticMethodArgumentsTest(clazz, test, staticMethodDescriptions));
      }
      return this;
   }

   public ClassAsserter protectsArgumentBoundaries()
   {
      ArgumentBoundProtectionTest test = ArgumentBoundProtectionTest.getInstance();

      if (!constructorDescriptions.isEmpty())
      {
         tests.add(new ClassArgumentsTest(clazz, constructorDescriptions, test, methodDescriptions));
      }

      if (!staticMethodDescriptions.isEmpty())
      {
         tests.add(new StaticMethodArgumentsTest(clazz, test, staticMethodDescriptions));
      }
      return this;
   }

   public ClassAsserter isEquatable(String... equalityFieldNames)
   {
      if (constructorDescriptions.isEmpty())
      {
         output
               .append(String.format("Unable to perform equatable test for class '%1$s', constructor descriptions must be set", clazz))
               .append(LINE_SEPARATOR);
      }
      else
      {
         tests.add(new EquatabilityTest(clazz, constructorDescriptions, Arrays.asList(equalityFieldNames)));
      }
      return this;
   }

   public ClassAsserter hasCorrectInitialization()
   {
      if (constructorDescriptions.isEmpty())
      {
         output
               .append(String.format("Unable to perform initialization test for class '%1$s', constructor descriptions must be set", clazz))
               .append(LINE_SEPARATOR);
      }
      else
      {
         tests.add(new InitializationTest(clazz, constructorDescriptions));
      }
      return this;
   }

   public void finishAssert()
   {
      for (ITest test : tests)
      {
         test.execute(output);
      }
      String message = output.toString();
      if (!Strings.isNullOrEmpty(message))
      {
         throw new IllegalStateException(message);
      }
   }

   @Override
   public IArgumentDescriber<ClassAsserter> withConstructor()
   {
      MethodDescription description = new MethodDescription<ClassAsserter>(this, StringUtils.EMPTY);
      constructorDescriptions.add(description);
      return description;
   }

   @Override
   public IArgumentDescriber<ClassAsserter> withMethod(String methodName)
   {
      Contract.requiresNotNull(methodName);
      MethodDescription description = new MethodDescription<ClassAsserter>(this, methodName);
      methodDescriptions.add(description);
      return description;
   }

   @Override
   public IArgumentDescriber<ClassAsserter> withStaticMethod(String methodName)
   {
      Contract.requiresNotNull(methodName);
      MethodDescription description = new MethodDescription<ClassAsserter>(this, methodName);
      staticMethodDescriptions.add(description);
      return description;
   }
}
