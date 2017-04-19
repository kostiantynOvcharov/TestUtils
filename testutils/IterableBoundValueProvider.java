package com.scc.ptl.testutils;

import com.google.common.collect.Range;

import java.util.ArrayList;

public class IterableBoundValueProvider<T> extends BoundValueProvider<Iterable<T>>
{
   private T filler;
   private Range bounds;

   public IterableBoundValueProvider(T filler, Range bounds)
   {
      this.filler = filler;
      this.bounds = bounds;
   }

   @Override
   public Iterable<Iterable<T>> getLowerBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }

   @Override
   public Iterable<Iterable<T>> getOverLowerBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }

   @Override
   public Iterable<Iterable<T>> getUnderLowerBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }

   @Override
   public Iterable<Iterable<T>> getUpperBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }

   @Override
   public Iterable<Iterable<T>> getOverUpperBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }

   @Override
   public Iterable<Iterable<T>> getUnderUpperBoundValues()
   {
      return new ArrayList<Iterable<T>>();
   }
}
