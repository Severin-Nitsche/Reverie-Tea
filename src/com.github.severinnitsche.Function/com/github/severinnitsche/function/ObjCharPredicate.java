package com.github.severinnitsche.function;

@FunctionalInterface
public interface ObjCharPredicate<I1> {

  boolean test(I1 t1, char t2);

  default ObjCharPredicate<I1> negate() {
    return (t1, t2) -> !test(t1, t2);
  }

  default ObjCharPredicate<I1> or(ObjCharPredicate<I1> other) {
    return (t1, t2) -> test(t1,t2) || other.test(t1,t2);
  }

  default ObjCharPredicate<I1> and(ObjCharPredicate<I1> other) {
    return (t1,t2) -> test(t1,t2) && other.test(t1,t2);
  }

}
