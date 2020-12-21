package com.github.severinnitsche.util;

import com.github.severinnitsche.function.ObjCharFunction;

public final class Strings {
  private Strings() {
  }

  public static <A> A reduce(String source, ObjCharFunction<A, A> r, A acc) {
    for (char c : source.toCharArray())
      acc = r.apply(acc, c);
    return acc;
  }

  public static <A> A reduceRight(String source, ObjCharFunction<A, A> r, A acc) {
    for (int i = source.length() - 1; i >= 0; i--)
      acc = r.apply(acc, source.charAt(i));
    return acc;
  }
}
