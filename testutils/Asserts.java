package com.scc.ptl.testutils;

public class Asserts
{
   public static IMethodDescriber<InstanceAsserter> assertThatInstance(Object instance)
   {
      return new InstanceAsserter(instance);
   }

   public static IConstructorDescriber<ClassAsserter> assertThatClass(Class clazz)
   {
      return new ClassAsserter(clazz);
   }
}