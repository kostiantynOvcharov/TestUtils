package com.scc.ptl.testutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

import java.util.ArrayList;
import java.util.Collections;

public final class RangeDurationLowerBoundValueProvider extends DurationBoundValueProvider
{
   private final Iterable<ReadableDuration> lowerBoundValue;

   public RangeDurationLowerBoundValueProvider(Range<? extends ReadableDuration> range)
   {
      super(range);
      ArrayList<ReadableDuration> list = Lists.newArrayList(super.getLowerBoundValues());
      lowerBoundValue = Collections.singleton(Collections.max(list));
   }

   @Override
   public Iterable<ReadableDuration> getValidValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getLowerBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getUnderUpperBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getOverLowerBoundValues()
   {
      return lowerBoundValue;
   }

   @Override
   public Iterable<ReadableDuration> getUpperBoundValues()
   {
      return lowerBoundValue;
   }
}
