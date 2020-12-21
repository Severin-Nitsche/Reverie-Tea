package com.github.severinnitsche.util;

import com.github.severinnitsche.function.Function;

public record Tuple2<A,B>(A a, B b) {
  public <C,D> Tuple2<C,D> map(Function<A,C> m1, Function<B,D> m2) {
    return new Tuple2<>(m1.apply(a),m2.apply(b));
  }
}
