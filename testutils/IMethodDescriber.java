package com.scc.ptl.testutils;

public interface IMethodDescriber<TChain>
{
   IArgumentDescriber<TChain> withMethod(String methodName);
}
