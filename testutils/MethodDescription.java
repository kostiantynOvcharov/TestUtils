package com.scc.ptl.testutils;

import org.apache.commons.lang.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodDescription<TChain> implements IArgumentDescriber<TChain>, IArgumentDescriberFinisher<TChain>
{
   private final TChain chain;
   private final String methodName;
   private List<ArgumentDescription> argumentDescriptions = new ArrayList<ArgumentDescription>();

   public MethodDescription(TChain chain, String methodName)
   {
      this.chain = chain;
      this.methodName = methodName;
   }

   @Override
   public <T> IArgumentDescriberFinisher<TChain> withArg(IValueProvider<T> value)
   {
      argumentDescriptions.add(new ArgumentDescription(value, null));
      return this;
   }

   @Override
   public <T> IArgumentDescriberFinisher<TChain> withArg(IValueProvider<T> value, String argumentName)
   {
      argumentDescriptions.add(new ArgumentDescription(value, argumentName));
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> IArgumentDescriberFinisher<TChain> withArg(T value)
   {
      argumentDescriptions.add(new ArgumentDescription(new ValueProvider<T>(value), null));
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> IArgumentDescriberFinisher<TChain> withArg(T value, String argumentName)
   {
      argumentDescriptions.add(new ArgumentDescription(new ValueProvider<T>(value), argumentName));
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> IArgumentDescriberFinisher<TChain> withPrimitiveArg(T value)
   {
      Class valueType = value.getClass();
      Class primitiveType = !valueType.isPrimitive() ? ClassUtils.wrapperToPrimitive(valueType) : valueType;
      argumentDescriptions.add(new ArgumentDescription(new ValueProvider<T>(primitiveType, value), null));
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> IArgumentDescriberFinisher<TChain> withPrimitiveArg(T value, String argumentName)
   {
      Class valueType = value.getClass();
      Class primitiveType = !valueType.isPrimitive() ? ClassUtils.wrapperToPrimitive(valueType) : valueType;
      argumentDescriptions.add(new ArgumentDescription(new ValueProvider<T>(primitiveType, value), argumentName));
      return this;
   }

   @Override
   public <T> IArgumentDescriberFinisher<TChain> withNullArg(Class<T> clazz, String argumentName)
   {
      argumentDescriptions.add(new ArgumentDescription(new ValueProvider<T>(clazz, null), argumentName));
      return this;
   }

   @Override
   public TChain endMethod()
   {
      argumentDescriptions = Collections.unmodifiableList(argumentDescriptions);
      return chain;
   }

   public String getMethodName()
   {
      return methodName;
   }

   public List<ArgumentDescription> getArgumentDescriptions()
   {
      return argumentDescriptions;
   }
}
