package com.scc.ptl.testutils;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.scc.ptl.contracts.Contract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ConstructorAction implements IAction
{
   private final Constructor constructor;
   private final Supplier<ArgumentDefinition[]> arguments;

   public ConstructorAction(final Constructor constructor)
   {
      Contract.requiresNotNull(constructor);
      this.constructor = constructor;
      arguments = Suppliers.memoize(new Supplier<ArgumentDefinition[]>()
      {
         @Override
         public ArgumentDefinition[] get()
         {
            return getArgumentDefinition(constructor);
         }
      });
   }

   @Override
   public String getName()
   {
      return "Constructor";
   }

   @Override
   public Class<?>[] getExpectedExceptions()
   {
      return constructor.getExceptionTypes();
   }

   @Override
   public void invoke(Object[] argumentValues) throws Throwable
   {
      try
      {
         constructor.newInstance(argumentValues);
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

   private static ArgumentDefinition[] getArgumentDefinition(Constructor constructor)
   {
      Annotation[][] annotations = constructor.getParameterAnnotations();
      Class[] types = constructor.getParameterTypes();
      ArgumentDefinition[] result = new ArgumentDefinition[types.length];

      for (int i = 0; i < types.length; i++)
      {
         result[i] = new ArgumentDefinition(annotations[i], types[i]);
      }
      return result;
   }
}
