package com.scc.ptl.testutils;

public interface IArgumentDescriberFinisher<TChain>
{
   <T> IArgumentDescriberFinisher<TChain> withArg(IValueProvider<T> value);

   <T> IArgumentDescriberFinisher<TChain> withArg(IValueProvider<T> value, String argumentName);

   <T> IArgumentDescriberFinisher<TChain> withArg(T value);

   <T> IArgumentDescriberFinisher<TChain> withArg(T value, String argumentName);

   <T> IArgumentDescriberFinisher<TChain> withPrimitiveArg(T value);

   <T> IArgumentDescriberFinisher<TChain> withPrimitiveArg(T value, String argumentName);

   <T> IArgumentDescriberFinisher<TChain> withNullArg(Class<T> clazz, String argumentName);

   TChain endMethod();
}
