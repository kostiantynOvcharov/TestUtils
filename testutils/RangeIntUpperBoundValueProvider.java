package com.scc.ptl.testutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.Collections;

public final class RangeIntUpperBoundValueProvider extends IntegerBoundValueProvider
{
   private final Iterable<Integer> upperBoundValue;

   public RangeIntUpperBoundValueProvider(Class clazz, Range<Integer> range)
   {
      super(clazz, range);
      ArrayList<Integer> list = Lists.newArrayList(super.getUpperBoundValues());
      upperBoundValue = Collections.singleton(Collections.max(list));
   }

   @Override
   public Iterable<Integer> getValidValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<Integer> getLowerBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<Integer> getUnderUpperBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<Integer> getOverLowerBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<Integer> getUpperBoundValues()
   {
      return upperBoundValue;
   }
}
