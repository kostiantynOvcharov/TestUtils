package com.scc.ptl.testutils;

import java.util.Arrays;

final class TestStep
{
   private final String errorMessage;
   private final Object[] argumentValues;
   private final Class expectedException;

   TestStep(final String errorMessage, final Object[] argumentValues, final Class expectedException)
   {
      this.errorMessage = errorMessage;
      this.argumentValues = Arrays.copyOf(argumentValues, argumentValues.length);
      this.expectedException = expectedException;
   }

   String getErrorMessage()
   {
      return errorMessage;
   }

   Object[] getArgumentValues()
   {
      return argumentValues.clone();
   }

   Class getExpectedException()
   {
      return expectedException;
   }
}
