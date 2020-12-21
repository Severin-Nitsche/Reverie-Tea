package com.github.severinnitsche.dreamer;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Required.class)
public @interface require {
  String fantasy();
  Class<?>[] superTypes() default {};
  Class<? extends java.lang.annotation.Annotation>[] annotations() default {};
}
