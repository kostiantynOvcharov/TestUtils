package com.scc.ptl.testutils;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class StaticMethodAction implements IAction
{
   private final Method method;
   private final Supplier<ArgumentDefinition[]> arguments;

   public StaticMethodAction(final Method method)
   {
      this.method = method;
      arguments = Suppliers.memoize(new Supplier<ArgumentDefinition[]>()
      {
         @Override
         public ArgumentDefinition[] get()
         {

            return getArgumentDefinition(method);
         }
      });
   }

   @Override
   public String getName()
   {
      return method.getName();
   }

   @Override
   public Class<?>[] getExpectedExceptions()
   {
      return method.getExceptionTypes();
   }

   @Override
   public void invoke(Object[] argumentValues) throws Throwable
   {
      try
      {
         method.invoke(null, argumentValues);
      }
      catch (InvocationTargetException e)
      {
         throw e.getTargetException();
      }
   }

   @Override
   public ArgumentDefinition[] getArguments()
   {
      return arguments.get();
   }

   private static ArgumentDefinition[] getArgumentDefinition(Method method)
   {
      Annotation[][] annotations = method.getParameterAnnotations();
      Class[] types = method.getParameterTypes();
      ArgumentDefinition[] result = new ArgumentDefinition[types.length];

      for (int i = 0; i < types.length; i++)
      {
         result[i] = new ArgumentDefinition(annotations[i], types[i]);
      }
      return result;
   }
}
