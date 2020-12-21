package com.github.severinnitsche.function;

@FunctionalInterface
public interface BiFunction<I1,I2,O> {

  O apply(I1 v1, I2 v2);

}
