package com.scc.ptl.testutils;

import com.scc.ptl.contracts.Contract;

public final class ArgumentDescription
{
   private final String argumentName;
   private final IValueProvider<?> valueProvider;

   public ArgumentDescription(IValueProvider<?> valueProvider, String argumentName)
   {
      Contract.requiresNotNull(valueProvider);
      this.argumentName = argumentName;
      this.valueProvider = valueProvider;
   }

   public String getArgumentName()
   {
      return argumentName;
   }

   public IValueProvider<?> getValueProvider()
   {
      return valueProvider;
   }
}
