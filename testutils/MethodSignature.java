package com.scc.ptl.testutils;

import java.util.Collection;
import java.util.List;

public class MethodSignature
{
   private String methodName;
   private Collection<List<Class<?>>> methodsWithParameters;

   public MethodSignature(String methodName, Collection<List<Class<?>>> methodsWithParameters)
   {
      this.methodName = methodName;
      this.methodsWithParameters = methodsWithParameters;
   }

   public String getMethodName()
   {
      return methodName;
   }

   public Collection<List<Class<?>>> getMethodsWithParameters()
   {
      return methodsWithParameters;
   }
}
