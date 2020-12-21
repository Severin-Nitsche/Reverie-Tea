package com.github.severinnitsche.function;

@FunctionalInterface
public interface CharFunction<O> {

  O apply(char value);

}
