package com.scc.ptl.testutils;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collection;

public final class InstanceAsserter implements IMethodDescriber<InstanceAsserter>
{
   private final Object instance;
   private final Collection<MethodDescription> methodDescriptions = new ArrayList<MethodDescription>();
   private final Collection<ITest> tests = new ArrayList<ITest>();
   private final StringBuilder output = new StringBuilder();

   public InstanceAsserter(Object instance)
   {
      this.instance = instance;
   }

   public InstanceAsserter protectedAgainstNullArguments()
   {
      NullArgumentsProtectionTest test = NullArgumentsProtectionTest.getInstance();
      tests.add(new InstanceArgumentTest(instance, test, methodDescriptions));
      return this;
   }

   public InstanceAsserter protectsArgumentBoundaries()
   {
      ArgumentBoundProtectionTest test = ArgumentBoundProtectionTest.getInstance();
      tests.add(new InstanceArgumentTest(instance, test, methodDescriptions));
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
   public IArgumentDescriber<InstanceAsserter> withMethod(String methodName)
   {
      MethodDescription description = new MethodDescription<InstanceAsserter>(this, methodName);
      methodDescriptions.add(description);
      return description;
   }
}
