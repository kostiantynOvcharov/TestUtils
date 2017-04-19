package com.scc.ptl.testutils;

interface IBoundValueProvider<T> extends IValueProvider<T>
{
   Iterable<? extends T> getLowerBoundValues();

   Iterable<? extends T> getOverLowerBoundValues();

   Iterable<? extends T> getUnderLowerBoundValues();

   Iterable<? extends T> getUpperBoundValues();

   Iterable<? extends T> getOverUpperBoundValues();

   Iterable<? extends T> getUnderUpperBoundValues();
}
