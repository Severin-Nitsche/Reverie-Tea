package com.github.severinnitsche.function;

@FunctionalInterface
public interface IntFunction<O> {

  O apply(int value);

}
