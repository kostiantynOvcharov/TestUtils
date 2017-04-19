package com.scc.ptl.testutils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import java.lang.annotation.Annotation;

final class ArgumentDefinition
{
   private final Annotation[] annotations;
   private final Class type;

   ArgumentDefinition(Annotation[] annotations, Class type)
   {
      this.annotations = annotations;
      this.type = type;
   }

   Class getType()
   {
      return type;
   }

   boolean hasAnnotation(Class annotationClass)
   {
      return FluentIterable
            .from(annotations)
            .transform(new Function<Annotation, Class>()
            {
               @Override
               public Class apply(Annotation annotation)
               {
                  return annotation.annotationType();
               }
            })
            .contains(annotationClass);
   }
}
