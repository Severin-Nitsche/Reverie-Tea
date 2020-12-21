package com.github.severinnitsche.function;

@FunctionalInterface
public interface CharPredicate {

  boolean test(char t);

  default CharPredicate negate() {
    return t -> !test(t);
  }

  default CharPredicate or(CharPredicate other) {
    return t -> test(t) || other.test(t);
  }

  default CharPredicate and(CharPredicate other) {
    return t -> test(t) && other.test(t);
  }

}
