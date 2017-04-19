package com.scc.ptl.testutils;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

import java.util.Collections;

public class DurationBoundValueProvider extends BoundValueProvider<ReadableDuration>
{
   public DurationBoundValueProvider(Range<? extends ReadableDuration> bounds)
   {
      super(Duration.class,
            getLowerBoundValues(bounds),
            getOverLowerBoundValues(bounds),
            getUnderLowerBoundValues(bounds),
            getUpperBoundValues(bounds),
            getOverUpperBoundValues(bounds),
            getUnderUpperBoundValues(bounds));

      //TODO update to allow usage open bounds
      if (bounds.lowerBoundType() == BoundType.OPEN || bounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
   }

   private static Iterable<? extends ReadableDuration> getLowerBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.lowerBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasLowerBound())
      {
         return Collections.singleton(bounds.lowerEndpoint());
      }
      else
      {
         return Collections.emptyList();
      }
   }

   private static Iterable<? extends ReadableDuration> getOverLowerBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.lowerBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasLowerBound())
      {
         Duration overLowerBoundValue = bounds.lowerEndpoint().toDuration().plus(1);
         return Collections.singleton(overLowerBoundValue);
      }
      else
      {
         return Collections.emptyList();
      }
   }

   private static Iterable<? extends ReadableDuration> getUnderLowerBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.lowerBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasLowerBound())
      {
         Duration underLowerBoundValue = bounds.lowerEndpoint().toDuration().minus(1);
         return Collections.singleton(underLowerBoundValue);
      }
      else
      {
         return Collections.emptyList();
      }
   }

   private static Iterable<? extends ReadableDuration> getUpperBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasUpperBound())
      {
         return Collections.singleton(bounds.upperEndpoint());
      }
      else
      {
         return Collections.emptyList();
      }
   }

   private static Iterable<? extends ReadableDuration> getOverUpperBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasUpperBound())
      {
         Duration overUpperBoundValue = bounds.upperEndpoint().toDuration().plus(1);
         return Collections.singleton(overUpperBoundValue);
      }
      else
      {
         return Collections.emptyList();
      }
   }

   private static Iterable<? extends ReadableDuration> getUnderUpperBoundValues(Range<? extends ReadableDuration> bounds)
   {
      if (bounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("Duration range should not be open");
      }
      if (bounds.hasUpperBound())
      {
         Duration underUpperBoundValue = bounds.upperEndpoint().toDuration().minus(1);
         return Collections.singleton(underUpperBoundValue);
      }
      else
      {
         return Collections.emptyList();
      }
   }
}
