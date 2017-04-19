package com.scc.ptl.testutils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils
{
   public static final String LINE_SEPARATOR = System.getProperty("line.separator");

   private ReflectionUtils()
   {
   }

   public static Constructor getConstructor(final Class<?> clazz, final MethodDescription description, final StringBuilder output)
   {
      Class[] argumentTypes = null;
      try
      {
         argumentTypes = TestUtils.getArgumentTypes(description.getArgumentDescriptions());
      }
      catch (IllegalValueProviderException e)
      {
         String message = String.format("Constructor of class '%1$s' '%2$s'", clazz.getName(), e.getMessage());
         if (!output.toString().contains(message))
         {
            output
                  .append(message)
                  .append(LINE_SEPARATOR);
         }
      }
      return getConstructor(clazz, argumentTypes, output);
   }

   public static Constructor getConstructor(final Class<?> clazz, final Class[] argumentTypes, final StringBuilder output)
   {
      Constructor constructor = null;
      try
      {
         constructor = clazz.getConstructor(argumentTypes);
      }
      catch (NoSuchMethodException e)
      {
         String message = String.format("Constructor of class '%1$s' with argument types '%2$s' doesn't exist",
               clazz.getName(),
               Arrays.toString(argumentTypes));
         if (!output.toString().contains(message))
         {
            output
                  .append(message)
                  .append(LINE_SEPARATOR);
         }
      }
      return constructor;
   }

   public static Object getInstance(final Class clazz, final Iterable<MethodDescription> constructorDescriptions, final StringBuilder output)
   {
      MethodDescription constructorDescription = Iterables.getFirst(constructorDescriptions, null);
      return getInstance(clazz, constructorDescription, output);
   }

   public static Object getInstance(final Class clazz, final MethodDescription constructorDescription, final StringBuilder output)
   {
      List<ArgumentDescription> argumentDescriptions = constructorDescription.getArgumentDescriptions();
      Object[] validValues = TestUtils.getValidValues(argumentDescriptions);
      Constructor constructor = getConstructor(clazz, constructorDescription, output);
      return constructor == null ? null : getInstance(validValues, constructor, output);
   }

   public static Object getInstance(
         final Class clazz,
         final Object[] argumentValues,
         final Iterable<ArgumentDescription> argumentDescriptions,
         final StringBuilder output)
   {
      Constructor constructor = getConstructor(clazz, TestUtils.getArgumentTypes(argumentDescriptions), output);
      return getInstance(argumentValues, constructor, output);
   }

   public static Object getInstance(final Object[] validValues, final Constructor constructor, final StringBuilder output)
   {
      Object instance = null;
      if (constructor == null)
      {
         return instance;
      }

      try
      {
         instance = constructor.newInstance(validValues);
      }
      catch (Exception e)
      {
         String message = String.format("Unable to perform valid call for constructor of class '%1$s' with argument values '%2$s'",
               constructor.getName(),
               Arrays.toString(validValues));
         if (!output.toString().contains(message))
         {
            output
                  .append(message)
                  .append(String.format(
                        "Reason: %1$s",
                        e.getCause() != null ? e.getCause().getMessage() : e.getMessage()))
                  .append(LINE_SEPARATOR);
         }
      }
      return instance;
   }

   public static Method getMethod(final Class clazz, final MethodDescription description, final StringBuilder output)
   {
      String methodName = description.getMethodName();
      Class[] argumentTypes = null;
      try
      {
         argumentTypes = TestUtils.getArgumentTypes(description.getArgumentDescriptions());
      }
      catch (IllegalValueProviderException e)
      {
         String message = String.format("Method '%1$s' '%2$s'", methodName, e.getMessage());
         if (!output.toString().contains(message))
         {
            output
                  .append(message)
                  .append(LINE_SEPARATOR);
         }
      }
      return getMethod(clazz, methodName, argumentTypes, output);
   }

   public static Method getMethod(final Class clazz, final String methodName, final StringBuilder output)
   {
      return getMethod(clazz, methodName, null, output);
   }

   private static Method getMethod(final Class clazz, final String methodName, Class[] argumentTypes, final StringBuilder output)
   {
      Method method = null;
      String[] methodNames = getNames(clazz.getDeclaredMethods());

      try
      {
         if (Arrays.asList(methodNames).contains(methodName))
         {
            method = clazz.getDeclaredMethod(methodName, argumentTypes);
         }
         else
         {
            method = clazz.getSuperclass().getDeclaredMethod(methodName, argumentTypes);
         }
      }
      catch (NoSuchMethodException e)
      {
         String message = new StringBuilder()
               .append(String.format("Method '%1$s'", methodName))
               .append(
                     argumentTypes == null
                           ? " without arguments"
                           : String.format(" with argument types '%1$s'", Arrays.toString(argumentTypes)))
               .toString();
         if (!output.toString().contains(message))
         {
            output
                  .append(message)
                  .append(" doesn't exist")
                  .append(LINE_SEPARATOR);
         }
      }
      return method;
   }

   public static String[] getNames(final Method[] methods)
   {
      return FluentIterable
            .from(methods)
            .transform(new Function<Method, String>()
            {
               @Override
               public String apply(Method method)
               {
                  return method.getName();
               }
            })
            .toArray(String.class);
   }
}
