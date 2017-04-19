package com.scc.ptl.testutils;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collection;

public final class StaticMethodAsserter implements IMethodDescriber<StaticMethodAsserter>
{
   private final Class clazz;
   private final Collection<MethodDescription> methodDescriptions = new ArrayList<MethodDescription>();
   private final Collection<ITest> tests = new ArrayList<ITest>();
   private final StringBuilder output = new StringBuilder();

   public StaticMethodAsserter(Class clazz)
   {
      this.clazz = clazz;
   }

   public StaticMethodAsserter protectedAgainstNullArguments()
   {
      NullArgumentsProtectionTest test = NullArgumentsProtectionTest.getInstance();
      tests.add(new StaticMethodArgumentsTest(clazz, test, methodDescriptions));
      return this;
   }

   public StaticMethodAsserter protectsArgumentBoundaries()
   {
      ArgumentBoundProtectionTest test = ArgumentBoundProtectionTest.getInstance();
      tests.add(new StaticMethodArgumentsTest(clazz, test, methodDescriptions));
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
   public IArgumentDescriber<StaticMethodAsserter> withMethod(String methodName)
   {
      MethodDescription description = new MethodDescription<StaticMethodAsserter>(this, methodName);
      methodDescriptions.add(description);
      return description;
   }
}
