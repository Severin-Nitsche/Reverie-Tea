package com.github.severinnitsche.function;

@FunctionalInterface
public interface BiPredicate<I1,I2> {

  boolean test(I1 t1, I2 t2);

  default BiPredicate<I1,I2> negate() {
    return (t1, t2) -> !test(t1, t2);
  }

  default BiPredicate<I1,I2> or(BiPredicate<I1,I2> other) {
    return (t1, t2) -> test(t1,t2) || other.test(t1,t2);
  }

  default BiPredicate<I1,I2> and(BiPredicate<I1,I2> other) {
    return (t1,t2) -> test(t1,t2) && other.test(t1,t2);
  }

}
