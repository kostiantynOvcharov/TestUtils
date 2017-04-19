package com.scc.ptl.testutils;

import com.scc.ptl.contracts.Contract;

import java.lang.reflect.Method;

public class InstanceArgumentTest implements ITest
{
   private final Object instance;
   private final MethodArgumentsTest test;
   private final Iterable<MethodDescription> methodDescriptions;

   public InstanceArgumentTest(
         Object instance,
         MethodArgumentsTest test,
         Iterable<MethodDescription> methodDescriptions)
   {
      Contract.requiresNotNull(instance);
      Contract.requiresNotNull(test);
      Contract.requiresNotNull(methodDescriptions);
      this.instance = instance;
      this.test = test;
      this.methodDescriptions = methodDescriptions;
   }

   @Override
   public void execute(StringBuilder output)
   {
      for (MethodDescription description : methodDescriptions)
      {
         Method method = ReflectionUtils.getMethod(instance.getClass(), description, output);
         if (method == null)
         {
            continue;
         }
         test.run(new MethodAction(instance, method), description.getArgumentDescriptions(), output);
      }
   }
}
