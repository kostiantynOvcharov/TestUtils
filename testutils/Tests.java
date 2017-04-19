package com.scc.ptl.testutils;

import com.google.common.collect.Iterables;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

class Tests
{
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");

   private Tests()
   {
   }

   static void runClassTest(final Class clazz, final MethodArgumentsTest test, final Iterable<MethodDescription> constructorDescriptions,
         final Iterable<MethodDescription> methodDescriptions, final StringBuilder testErrors)
   {
      Constructor constructor = null;
      Iterable<IValueProvider<?>> arguments = null;

      for (MethodDescription constructorDescription : constructorDescriptions)
      {
         arguments = constructorDescription.getArguments();
         try
         {
            constructor = getConstructor(clazz, arguments);
            test.run(new ConstructorAction(constructor), arguments, testErrors);
         }
         catch (NoSuchMethodException e)
         {
            testErrors
                  .append("Constructor doesn't exist. Constructor name: ")
                  .append(constructorDescription.getMethodName())
                  .append(", arguments: ")
                  .append(Arrays.asList(getArgumentTypes(arguments)))
                  .append(LINE_SEPARATOR);
         }
      }

      Object instance = null;
      Object[] validValues = getValidValues(arguments);
      try
      {
         instance = constructor.newInstance(validValues);
      }
      catch (Exception e)
      {
         testErrors
               .append("Unable to perform valid constructor call. Constructor name: ")
               .append(constructor.getName())
               .append(", arguments: ")
               .append(Arrays.asList(validValues))
               .append(LINE_SEPARATOR);
      }

      runInstanceArgumentsTest(instance, test, methodDescriptions, testErrors);
   }

   static <T> void runConstructorTest(final Class clazz, final MethodArgumentsTest test,
         final Iterable<MethodDescription> constructorDescriptions, final StringBuilder testErrors)
   {
      MethodDescription constructorDescription = constructorDescriptions.iterator().next();
      Iterable arguments = null;

      for (MethodDescription description : constructorDescriptions)
      {
         arguments = constructorDescription.getArguments();
         Constructor constructor = null;
         try
         {
            constructor = getConstructor(clazz, arguments);
         }
         catch (NoSuchMethodException e)
         {
            testErrors
                  .append("Constructor doesn't exist. Constructor name: ")
                  .append(constructorDescription.getMethodName())
                  .append(", arguments: ")
                  .append(Arrays.asList(getArgumentTypes(arguments)))
                  .append(LINE_SEPARATOR);
         }

      }
   }

   static <T> void runStaticMethodArgumentsTest(final Class<T> clazz, final MethodArgumentsTest test,
         final Iterable<MethodDescription> descriptions, final StringBuilder testErrors)
   {
      Class exceptionClass = IllegalArgumentException.class;

      for (MethodDescription description : descriptions)
      {
         Class[] argumentTypes = getArgumentTypes(description.getArguments());
         try
         {
            final Method method = clazz.getDeclaredMethod(description.getMethodName(), argumentTypes);
            test.run(new StaticMethodAction(method), description.getArguments(), testErrors);
         }
         catch (NoSuchMethodException e)
         {
            testErrors
                  .append("Method doesn't exist. Method name: ")
                  .append(description.getMethodName())
                  .append(", arguments: ")
                  .append(Arrays.asList(argumentTypes))
                  .append(LINE_SEPARATOR);
         }
      }
   }

   static void runInstanceArgumentsTest(final Object instance, final MethodArgumentsTest test,
         final Iterable<MethodDescription> descriptions, final StringBuilder testErrors)
   {
      Class exceptionClass = IllegalArgumentException.class;

      for (MethodDescription description : descriptions)
      {
         Class[] argumentTypes = getArgumentTypes(description.getArguments());
         try
         {
            final Method method = instance.getClass().getMethod(description.getMethodName(), argumentTypes);
            test.run(new MethodAction(instance, method), description.getArguments(), testErrors);
         }
         catch (NoSuchMethodException e)
         {
            testErrors
                  .append("Method doesn't exist. Method name: ")
                  .append(description.getMethodName())
                  .append(", arguments: ")
                  .append(Arrays.asList(argumentTypes))
                  .append(LINE_SEPARATOR);
         }
      }
   }

   private static Class[] getArgumentTypes(final Iterable<IValueProvider<?>> arguments)
   {
      Class[] parameterTypes = new Class[Iterables.size(arguments)];
      int count = 0;

      for (IValueProvider<?> argument : arguments)
      {
         Iterator<?> argumentIterator = argument.getValidValues().iterator();
         Class argumentType = argumentIterator.next().getClass();
         parameterTypes[count] = argument.isPrimitive() ? ClassUtils.wrapperToPrimitive(argumentType) : argumentType;
         count++;
      }
      return parameterTypes;
   }

   private static Object[] getValidValues(final Iterable<IValueProvider<?>> arguments)
   {
      Object[] values = new Object[Iterables.size(arguments)];
      int counter = 0;
      for (IValueProvider<?> argument : arguments)
      {
         values[counter] = argument.getValidValues().iterator().next();
         counter++;
      }
      return values;
   }

   private static Constructor getConstructor(Class clazz, Iterable<IValueProvider<?>> arguments) throws NoSuchMethodException
   {
      Class[] argumentTypes = getArgumentTypes(arguments);
      return clazz.getConstructor(argumentTypes);
   }
}
