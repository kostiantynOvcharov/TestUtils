package com.scc.ptl.testutils;

public interface IAction
{
   String getName();

   Class<?>[] getExpectedExceptions();

   void invoke(Object[] argumentValues) throws Throwable;

   ArgumentDefinition[] getArguments();
}
