package com.scc.ptl.testutils;

import com.google.common.collect.BoundType;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.scc.ptl.contracts.Contract;

import java.util.Collections;

public class LongBoundValueProvider implements IBoundValueProvider<Long>
{
   final private Class clazz;
   final private Range<Long> range;

   public LongBoundValueProvider(Class clazz, Range<Long> range)
   {
      Contract.requiresNotNull(range, "range");
      if(!range.hasLowerBound() && !range.hasUpperBound())
      {
         throw new IllegalArgumentException("The range shall have at least one bound");
      }
      this.clazz = clazz;
      this.range = range;
   }

   @Override
   public Iterable<Long> getValidValues()
   {
      return
            Iterables.unmodifiableIterable(Iterables.concat(
                  getOverLowerBoundValues(),
                  getUnderUpperBoundValues(),
                  getUpperBoundValues(),
                  getLowerBoundValues()));
   }

   @Override
   public Class getClazz()
   {
      return clazz;
   }

   private Long getLowerInteger()
   {
      switch (range.lowerBoundType())
      {
         case OPEN:
            return range.lowerEndpoint() + 1;
         case CLOSED:
            return range.lowerEndpoint();
         default:
            throw new EnumConstantNotPresentException(BoundType.class, range.lowerBoundType().toString());
      }
   }

   private Long getUpperInteger()
   {
      switch (range.upperBoundType())
      {
         case OPEN:
            return range.upperEndpoint() - 1;
         case CLOSED:
            return range.upperEndpoint();
         default:
            throw new EnumConstantNotPresentException(BoundType.class, range.upperBoundType().toString());
      }
   }

   @Override
   public Iterable<Long> getLowerBoundValues()
   {
      if (!range.hasLowerBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getLowerInteger());
   }

   @Override
   public Iterable<Long> getOverLowerBoundValues()
   {
      if (!range.hasLowerBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getLowerInteger() + 1);
   }

   @Override
   public Iterable<Long> getUnderLowerBoundValues()
   {
      if (!range.hasLowerBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getLowerInteger() - 1);
   }

   @Override
   public Iterable<Long> getUpperBoundValues()
   {
      if (!range.hasUpperBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getUpperInteger());
   }

   @Override
   public Iterable<Long> getOverUpperBoundValues()
   {
      if (!range.hasUpperBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getUpperInteger() + 1);
   }

   @Override
   public Iterable<Long> getUnderUpperBoundValues()
   {
      if (!range.hasUpperBound())
      {
         return Collections.emptyList();
      }
      return Collections.singleton(getUpperInteger() - 1);
   }
}
