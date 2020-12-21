package com.github.severinnitsche.util;

import com.github.severinnitsche.function.BiFunction;
import com.github.severinnitsche.function.ObjCharFunction;

public final class Arrays {

  private Arrays() {
  }

  public static <T> boolean contains(T[] source, T value) {
    for (T t : source)
      if (t.equals(value)) return true;
    return false;
  }

  public static boolean contains(char[] source, char value) {
    for (char t : source)
      if (t == value) return true;
    return false;
  }

  public static <T, A> A reduceRight(T[] source, BiFunction<A, T, A> r, A acc) {
    for (int i = source.length - 1; i >= 0; i--)
      acc = r.apply(acc, source[i]);
    return acc;
  }

  public static <A> A reduceRight(char[] source, ObjCharFunction<A, A> r, A acc) {
    for (int i = source.length - 1; i >= 0; i--)
      acc = r.apply(acc, source[i]);
    return acc;
  }

  public static <T, A> A reduceRight_(T[] source, BiFunction<A, T, A> r, A acc) {
    return reduceRight__(source, r, acc, 0);
  }

  private static <T, A> A reduceRight__(T[] source, BiFunction<A, T, A> r, A acc, int i) {
    if (i < source.length) return reduceRight__(source, r, r.apply(acc, source[i]), i + 1);
    else return acc;
  }

  public static <T, A> A reduce(T[] source, BiFunction<A, T, A> r, A acc) {
    for (T t : source) acc = r.apply(acc, t);
    return acc;
  }

  public static <A> A reduce(char[] source, ObjCharFunction<A, A> r, A acc) {
    for (char t : source) acc = r.apply(acc, t);
    return acc;
  }

  public static <T> String toString(T[] arr) {
    if (arr.length > 0)
      return reduceRight__(arr, (str, t) -> str + ',' + t, '[' + arr[0].toString(), 1) + ']';
    else
      return "[]";
  }
}
