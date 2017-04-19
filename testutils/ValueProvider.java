package com.scc.ptl.testutils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;

public class ValueProvider<T> implements IValueProvider<T>
{
   public static final IValueProvider<String> STRING = new ValueProvider<String>(StringUtils.EMPTY);
   public static final IValueProvider<Integer> INTS_PRIMITIVE = new ValueProvider<Integer>(int.class, 0, 1);
   public static final IValueProvider<Integer> POSITIVE_PRIMITIVE_INTS = new ValueProvider<Integer>(int.class, 1, 2, 3);
   public static final IValueProvider<Boolean> BOOLEANS_PRIMITIVE = new ValueProvider<Boolean>(boolean.class, false, true);
   public static final IValueProvider<Iterable> ITERABLE = new ValueProvider<Iterable>(Iterable.class, Collections.emptyList());

   private final Class clazz;
   private final Iterable<T> validValues;

   public ValueProvider(T... validValues)
   {
      this(validValues[0].getClass(), validValues);
   }

   public ValueProvider(Class clazz, T... validValues)
   {
      this.clazz = clazz;
      this.validValues = (validValues == null) ? null : Arrays.asList(validValues);
   }

   @Override
   public Iterable<T> getValidValues()
   {
      return validValues;
   }

   @Override
   public Class getClazz()
   {
      return clazz;
   }
}
