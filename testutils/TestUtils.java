package com.scc.ptl.testutils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TestUtils
{
   private TestUtils()
   {
   }

   private final static Function<ArgumentDescription, Class> VALUE_TYPE_RESOLVER = new Function<ArgumentDescription, Class>()
   {
      @Override
      public Class apply(ArgumentDescription argumentDescription)
      {
         return getValueType(argumentDescription);
      }
   };

   private final static Function<ArgumentDescription, Object> VALID_VALUE_RESOLVER = new Function<ArgumentDescription, Object>()
   {
      @Override
      public Object apply(ArgumentDescription argumentDescription)
      {
         return getValue(argumentDescription);
      }
   };

   public static Object[][] getUniqueValidValueCorteges(List<ArgumentDescription> arguments, final int numberOfCorteges)
         throws IllegalValueProviderException
   {
      Object[][] argumentSets = FluentIterable
            .from(arguments)
            .transform(new Function<ArgumentDescription, Object[]>()
            {
               @Override
               public Object[] apply(ArgumentDescription argumentDescription)
               {
                  return getUniqueValidValues(argumentDescription, numberOfCorteges);
               }
            })
            .toArray(Object[].class);
      return transposeMatrix(argumentSets);
   }

   public static Object[][] getUniqueValidValueCorteges(List<ArgumentDescription> arguments, final int numberOfCorteges, final Iterable<String> equalityFieldNames)
         throws IllegalValueProviderException
   {
      Object[][] argumentSets = FluentIterable
            .from(arguments)
            .transform(new Function<ArgumentDescription, Object[]>()
            {
               @Override
               public Object[] apply(ArgumentDescription argumentDescription)
               {
                  if (Iterables.contains(equalityFieldNames, argumentDescription.getArgumentName()))
                  {
                     return getUniqueValidValues(argumentDescription, numberOfCorteges);
                  }
                  else
                  {
                     return FluentIterable
                           .from(ImmutableList.copyOf(argumentDescription.getValueProvider().getValidValues()))
                           .cycle()
                           .limit(numberOfCorteges)
                           .toArray(Object.class);
                  }
               }
            })
            .toArray(Object[].class);
      return transposeMatrix(argumentSets);
   }

   private static Object[][] transposeMatrix(Object[][] m)
   {
      Object[][] temp = new Object[m[0].length][m.length];
      for (int i = 0; i < m.length; i++)
         for (int j = 0; j < m[0].length; j++)
            temp[j][i] = m[i][j];
      return temp;
   }

   public static Object[] getUniqueValidValues(ArgumentDescription argumentDescription, int numberOfValues)
         throws IllegalValueProviderException
   {
      Iterable<?> allValidValues = argumentDescription.getValueProvider().getValidValues();
      Set<Object> distinctValidValues = ImmutableSet.copyOf(allValidValues);
      if (distinctValidValues.size() < numberOfValues)
      {
         throw new IllegalValueProviderException(
               String.format("ValueProvider for '%1$s' don't provide expected number of unique values, expected number: '%2$s'",
                     argumentDescription.getArgumentName(),
                     numberOfValues));
      }
      return FluentIterable
            .from(distinctValidValues)
            .limit(numberOfValues)
            .toArray(Object.class);
   }

   public static String getGetterName(final String argumentName)
   {
      return "get" + StringUtils.capitalize(argumentName);
   }

   public static Class[] getArgumentTypes(final Iterable<ArgumentDescription> argumentDescriptions)
   {
      return FluentIterable.from(argumentDescriptions).transform(VALUE_TYPE_RESOLVER).toArray(Class.class);
   }

   public static Object[] getValidValues(final List<ArgumentDescription> argumentDescriptions) throws IllegalValueProviderException
   {
      return FluentIterable
            .from(argumentDescriptions)
            .transform(VALID_VALUE_RESOLVER)
            .toArray(Object.class);
   }

   public static Class getValueType(ArgumentDescription argumentDescription) throws IllegalValueProviderException
   {
      Class valueType = null;
      IValueProvider<?> valueProvider = argumentDescription.getValueProvider();
      if (valueProvider == null)
      {
         throw new IllegalValueProviderException("must have value provider");
      }
      else
      {
         valueType = valueProvider.getClazz();
      }
      return valueType;
   }

   public static Object getValue(ArgumentDescription argumentDescription) throws IllegalValueProviderException
   {
      Object value = Iterables.getFirst(argumentDescription.getValueProvider().getValidValues(), null);
      if (value == null)
      {
         throw new IllegalValueProviderException("must have not null argument values");
      }
      return value;
   }

   public static int getRandomInt()
   {
      Random valueGenerator = new Random();
      return valueGenerator.nextInt();
   }

   public static String generateRandomAlpaNumericString(int stringLength)
   {
      class RandomString
      {

         private final char[] symbols;
         private final Random random = new Random();
         private final char[] buf;

         {
            StringBuilder tmp = new StringBuilder();
            for (char ch = '0'; ch <= '9'; ++ch)
               tmp.append(ch);
            for (char ch = 'a'; ch <= 'z'; ++ch)
               tmp.append(ch);
            symbols = tmp.toString().toCharArray();
         }

         public RandomString(int stringLength)
         {
            if (stringLength < 1)
               throw new IllegalArgumentException("length < 1: " + stringLength);
            buf = new char[stringLength];
         }

         public String nextString()
         {
            for (int i = 0; i < buf.length; ++i)
               buf[i] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
         }
      }
      return new RandomString(stringLength).nextString();
   }
}
