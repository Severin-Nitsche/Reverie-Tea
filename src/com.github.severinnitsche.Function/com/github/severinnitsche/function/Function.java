package com.github.severinnitsche.function;

@FunctionalInterface
public interface Function<I,O> {
  O apply(I item);
}
