package com.scc.ptl.testutils;

import com.google.common.collect.Iterables;
import com.scc.ptl.contracts.Contract;

public class BoundValueProvider<T> implements IBoundValueProvider<T>
{
   private final Class clazz;
   private final Iterable<? extends T> lowerBoundValues;
   private final Iterable<? extends T> overLowerBoundValues;
   private final Iterable<? extends T> underLowerBoundValues;
   private final Iterable<? extends T> upperBoundValues;
   private final Iterable<? extends T> overUpperBoundValues;
   private final Iterable<? extends T> underUpperBoundValues;

   public BoundValueProvider(
         Class clazz,
         Iterable<? extends T> lowerBoundValues,
         Iterable<? extends T> overLowerBoundValues,
         Iterable<? extends T> underLowerBoundValues,
         Iterable<? extends T> upperBoundValues,
         Iterable<? extends T> overUpperBoundValues,
         Iterable<? extends T> underUpperBoundValues)
   {
      Contract.requiresNotNull(clazz);
      Contract.requiresNotNull(lowerBoundValues);
      Contract.requiresNotNull(overLowerBoundValues);
      Contract.requiresNotNull(underLowerBoundValues);
      Contract.requiresNotNull(upperBoundValues);
      Contract.requiresNotNull(overUpperBoundValues);
      Contract.requiresNotNull(underUpperBoundValues);
      this.clazz = clazz;
      this.lowerBoundValues = lowerBoundValues;
      this.overLowerBoundValues = overLowerBoundValues;
      this.underLowerBoundValues = underLowerBoundValues;
      this.upperBoundValues = upperBoundValues;
      this.overUpperBoundValues = overUpperBoundValues;
      this.underUpperBoundValues = underUpperBoundValues;
   }

   @Override
   public Iterable<T> getValidValues()
   {
      return Iterables.unmodifiableIterable(
            Iterables.concat(getOverLowerBoundValues(), getUnderUpperBoundValues(), getUpperBoundValues(), getLowerBoundValues()));
   }

   @Override
   public Class getClazz()
   {
      return clazz;
   }

   @Override
   public Iterable<? extends T> getLowerBoundValues()
   {
      return lowerBoundValues;
   }

   @Override
   public Iterable<? extends T> getOverLowerBoundValues()
   {
      return overLowerBoundValues;
   }

   @Override
   public Iterable<? extends T> getUnderLowerBoundValues()
   {
      return underLowerBoundValues;
   }

   @Override
   public Iterable<? extends T> getUpperBoundValues()
   {
      return upperBoundValues;
   }

   @Override
   public Iterable<? extends T> getOverUpperBoundValues()
   {
      return overUpperBoundValues;
   }

   @Override
   public Iterable<? extends T> getUnderUpperBoundValues()
   {
      return underUpperBoundValues;
   }
}
