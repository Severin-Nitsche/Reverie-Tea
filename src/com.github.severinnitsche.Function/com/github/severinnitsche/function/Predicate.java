package com.github.severinnitsche.function;

@FunctionalInterface
public interface Predicate<I> {

  boolean test(I t);

  default Predicate<I> negate() {
    return t -> !test(t);
  }

  default Predicate<I> or(Predicate<I> other) {
    return t -> test(t) || other.test(t);
  }

  default Predicate<I> and(Predicate<I> other) {
    return t -> test(t) && other.test(t);
  }

}
