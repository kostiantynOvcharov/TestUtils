package com.scc.ptl.testutils;

public interface IValueProvider<T>
{
   Iterable<T> getValidValues();

   Class getClazz();
}
