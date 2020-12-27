package com.github.severinnitsche.milner_types;

public class Example {
  public static void main(String[] args) {
    System.out.println(Lexer.tokenize("equals :: Setoid a => a ~> a -> Boolean"));
    System.out.println(Lexer.tokenize("map :: Functor f => f a ~> (a -> b) -> f b"));
    System.out.println(AST.of("equals :: Setoid a => a ~> a -> Boolean"));
    System.out.println(AST.of("map :: Functor f => f a ~> (a -> b) -> f b"));
  }
}
