package com.scc.ptl.testutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.Collections;

public final class RangeIntLowerBoundValueProvider extends IntegerBoundValueProvider
{
   private final Iterable<Integer> lowerBoundValue;

   public RangeIntLowerBoundValueProvider(Class clazz, Range<Integer> range)
   {
      super(clazz, range);
      ArrayList<Integer> list = Lists.newArrayList(super.getLowerBoundValues());
      lowerBoundValue = Collections.singleton(Collections.max(list));
   }

   @Override
   public Iterable<Integer> getValidValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<Integer> getLowerBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<Integer> getUnderUpperBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<Integer> getOverLowerBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<Integer> getUpperBoundValues()
   {
      return lowerBoundValue;
   }
}
