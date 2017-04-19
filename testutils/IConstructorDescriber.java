package com.scc.ptl.testutils;

public interface IConstructorDescriber<TChain>
{
   IArgumentDescriber<TChain> withConstructor();

   IArgumentDescriber<TChain> withStaticMethod(String methodName);
}
