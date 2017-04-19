package com.scc.ptl.testutils;

import com.scc.ptl.contracts.Contract;

import java.lang.reflect.Method;

public final class StaticMethodArgumentsTest implements ITest
{
   private final Class clazz;
   private final MethodArgumentsTest test;
   private final Iterable<MethodDescription> methodDescriptions;

   public StaticMethodArgumentsTest(
         Class clazz,
         MethodArgumentsTest test,
         Iterable<MethodDescription> methodDescriptions)
   {
      Contract.requiresNotNull(clazz);
      Contract.requiresNotNull(test);
      Contract.requiresNotNull(methodDescriptions);
      this.clazz = clazz;
      this.test = test;
      this.methodDescriptions = methodDescriptions;
   }

   @Override
   public void execute(StringBuilder output)
   {
      for (MethodDescription description : methodDescriptions)
      {
         Method method = ReflectionUtils.getMethod(clazz, description, output);
         if (method == null)
         {
            continue;
         }
         test.run(new StaticMethodAction(method), description.getArgumentDescriptions(), output);
      }
   }
}
