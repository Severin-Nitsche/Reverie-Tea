package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.function.BiFunction;

//No require needed! Once the Java compiler is sufficient!
public interface Foldable<a> {
  <b> b reduce(BiFunction<b,a,b> func, b id);
}
