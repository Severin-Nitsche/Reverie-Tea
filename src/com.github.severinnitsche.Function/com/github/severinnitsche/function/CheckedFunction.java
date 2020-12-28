package com.github.severinnitsche.function;

@FunctionalInterface
public interface CheckedFunction<I,O,T extends Throwable> {
  O apply(I item) throws T;
}
