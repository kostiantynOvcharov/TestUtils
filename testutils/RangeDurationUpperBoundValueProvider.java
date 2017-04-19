package com.scc.ptl.testutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

import java.util.ArrayList;
import java.util.Collections;

public final class RangeDurationUpperBoundValueProvider extends DurationBoundValueProvider
{
   private final Iterable<ReadableDuration> upperBoundValue;

   public RangeDurationUpperBoundValueProvider(Range<? extends ReadableDuration> range)
   {
      super(range);
      ArrayList<ReadableDuration> list = Lists.newArrayList(super.getUpperBoundValues());
      upperBoundValue = Collections.singleton(Collections.max(list));
   }

   @Override
   public Iterable<ReadableDuration> getValidValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getLowerBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getUnderUpperBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getOverLowerBoundValues()
   {
      return upperBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getUpperBoundValues()
   {
      return upperBoundValue;
   }
}
