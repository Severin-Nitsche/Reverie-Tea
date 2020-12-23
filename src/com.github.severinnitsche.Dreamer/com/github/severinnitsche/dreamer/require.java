package com.github.severinnitsche.dreamer;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Required.class)
public @interface require {
  String fantasy();
  Class<?>[] superTypes() default {};
}
