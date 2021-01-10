package com.github.severinnitsche.util;

import com.github.severinnitsche.function.BiFunction;
import com.github.severinnitsche.algebraic_data_types.List;

public interface Functions {
  static <A> A identity(A a) {
    return a;
  }
  static <A,B,C> List<C> zipWith(BiFunction<A,B,C> combine, List<A> a, List<B> b) {
    return a instanceof List.Cons<A> ac && b instanceof List.Cons<B> bc ?
        zipWith(combine, ac.tail(), bc.tail()).prepend(combine.apply(ac.head(),bc.head())) :
        List.empty();
  }
}
