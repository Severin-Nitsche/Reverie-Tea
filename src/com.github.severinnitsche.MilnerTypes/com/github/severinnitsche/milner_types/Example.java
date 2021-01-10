package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.util.*;
import com.github.severinnitsche.algebraic_data_types.*;

public class Example {
  public static void main(String[] args) {
    System.out.println(Lexer.tokenize("equals :: Setoid a => a ~> a -> Boolean"));
    System.out.println(Lexer.tokenize("map :: Functor f => f a ~> (a -> b) -> f b"));
    System.out.println(AST.of("equals :: Setoid a => a ~> a -> Boolean"));
    System.out.println(AST.of("map :: Functor f => f a ~> (a -> b) -> f b"));
    Object[] a = {1,2,3};
    a[1] = a;
    Object[] b = {1,2,3};
    b[1] = b;
    final List<Object> c = List.from(a);
    final List<Object> d = List.from(b);
    System.out.println(Objects.deepEquals(a,b));
    System.out.println(a.equals(b));
    System.out.println(Objects.deepEquals(c,d));
    System.out.println(c.equals(d));
    System.out.println(c);
    System.out.println(d);
  }
}
