package com.scc.ptl.testutils;

import com.scc.ptl.contracts.Contract;

import java.lang.reflect.Constructor;

public class ClassArgumentsTest implements ITest
{
   private final Class clazz;
   private final Iterable<MethodDescription> constructorDescriptions;
   private final MethodArgumentsTest test;
   private final Iterable<MethodDescription> methodDescriptions;

   public ClassArgumentsTest(Class clazz,
         Iterable<MethodDescription> constructorDescriptions,
         MethodArgumentsTest test,
         Iterable<MethodDescription> methodDescriptions)
   {
      Contract.requiresNotNull(clazz);
      Contract.requiresNotNull(constructorDescriptions);
      Contract.requiresNotNull(test);
      this.clazz = clazz;
      this.constructorDescriptions = constructorDescriptions;
      this.test = test;
      this.methodDescriptions = methodDescriptions;
   }

   @Override
   public void execute(StringBuilder output)
   {
      runConstructorArgumentsTest(clazz, test, constructorDescriptions, output);

      Object instance = ReflectionUtils.getInstance(clazz, constructorDescriptions, output);
      if (instance == null)
      {
         return;
      }
      new InstanceArgumentTest(instance, test, methodDescriptions).execute(output);
   }

   private void runConstructorArgumentsTest(
         Class clazz,
         MethodArgumentsTest test,
         Iterable<MethodDescription> constructorDescriptions,
         StringBuilder output)
   {
      for (MethodDescription description : constructorDescriptions)
      {
         Constructor constructor = ReflectionUtils.getConstructor(clazz, description, output);
         if (constructor != null)
         {
            test.run(new ConstructorAction(constructor), description.getArgumentDescriptions(), output);
         }
      }
   }
}
