package com.scc.ptl.testutils;

import com.google.common.collect.FluentIterable;

public class IterableValueProvider extends ValueProvider<Iterable>
{
   public IterableValueProvider(Object... validValues)
   {
      super(Iterable.class, FluentIterable.from(validValues));
   }
}
