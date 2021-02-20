package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.util.*;
import com.github.severinnitsche.algebraic_data_types.*;

import static com.github.severinnitsche.milner_types.Tokenized.of;

public class Example {

  public static void assertEquals(Object a, Object b, String reason) {
    final var passed = Objects.deepEquals(a,b);
    System.err.printf("%s %s:\t%s%n",passed ? "✅" : "❌",reason, passed ? "passed" : "failed");
    if(!passed) {
      System.out.printf("%s%n%s%n",a,b);
    }
  }

  public static void testLexer() {
    //equals :: Setoid a => a ~> a -> Boolean
    var expected = of("Boolean").map(v -> List.Cons(v)).
    chain(list -> of("->").map(v -> list.prepend(v))).
    chain(list -> of("a").map(v -> list.prepend(v))).
    chain(list -> of("~>").map(v -> list.prepend(v))).
    chain(list -> of("a").map(v -> list.prepend(v))).
    chain(list -> of("=>").map(v -> list.prepend(v))).
    chain(list -> of("a").map(v -> list.prepend(v))).
    chain(list -> of("Setoid").map(v -> list.prepend(v))).
    chain(list -> of("::").map(v -> list.prepend(v))).
    chain(list -> of("equals").map(v -> list.prepend(v)));
    var result = Lexer.tokenize("equals :: Setoid a => a ~> a -> Boolean");
    assertEquals(expected, result, "Lexer - equals :: Setoid a => a ~> a -> Boolean");
    //map :: Functor f => f a ~> (a -> b) -> f b
    expected = of("b").map(v -> List.Cons(v)).
    chain(list -> of("f").map(v -> list.prepend(v))).
    chain(list -> of("->").map(v -> list.prepend(v))).
    chain(list -> of(")").map(v -> list.prepend(v))).
    chain(list -> of("b").map(v -> list.prepend(v))).
    chain(list -> of("->").map(v -> list.prepend(v))).
    chain(list -> of("a").map(v -> list.prepend(v))).
    chain(list -> of("(").map(v -> list.prepend(v))).
    chain(list -> of("~>").map(v -> list.prepend(v))).
    chain(list -> of("a").map(v -> list.prepend(v))).
    chain(list -> of("f").map(v -> list.prepend(v))).
    chain(list -> of("=>").map(v -> list.prepend(v))).
    chain(list -> of("f").map(v -> list.prepend(v))).
    chain(list -> of("Functor").map(v -> list.prepend(v))).
    chain(list -> of("::").map(v -> list.prepend(v))).
    chain(list -> of("map").map(v -> list.prepend(v)));
    result = Lexer.tokenize("map :: Functor f => f a ~> (a -> b) -> f b");
    assertEquals(expected, result, "Lexer - map :: Functor f => f a ~> (a -> b) -> f b");
  }

  public static void testAST() {
    var expected = Either.<Throwable,AST.Complex.NamedType>from(
    new AST.Complex.NamedType(
    "equals",
    new AST.Complex.TypeDef(
    List.Cons(new AST.TypeCons(new AST.Type.ConsType("a",List.Nil()),new AST.Type.ConsType("Setoid",List.Nil()))),
    new AST.Type.Method(
    new AST.Type.ConsType("a",List.Nil()),
    new AST.Type.Function(
    new AST.Type.ConsType("a",List.Nil()),
    new AST.Type.ConsType("Boolean",List.Nil())
    )
    )
    )
    )
    );
    var result = AST.of("equals :: Setoid a => a ~> a -> Boolean");
    assertEquals(expected, result, "AST - equals :: Setoid a => a ~> a -> Boolean");
    expected = Either.<Throwable,AST.Complex.NamedType>from(
    new AST.Complex.NamedType(
    "map",
    new AST.Complex.TypeDef(
    List.Cons(new AST.TypeCons(new AST.Type.ConsType("f",List.Nil()),new AST.Type.ConsType("Functor",List.Nil()))),
    new AST.Type.Method(
    new AST.Type.ConsType("f",List.Cons(new AST.Type.ConsType("a",List.Nil()))),
    new AST.Type.Function(
    new AST.Type.Function(
    new AST.Type.ConsType("a",List.Nil()),
    new AST.Type.ConsType("b",List.Nil())
    ),
    new AST.Type.ConsType("f",List.Cons(new AST.Type.ConsType("b",List.Nil())))
    )
    )
    )
    )
    );
    result = AST.of("map :: Functor f => f a ~> (a -> b) -> f b");
    assertEquals(expected, result, "AST - map :: Functor f => f a ~> (a -> b) -> f b");
  }

  public static void testEquals() {
    Object[] a = {1,2,3};
    Object[] b = {1,2,3};
    a[1] = a;
    b[1] = b;
    assertEquals(a,b,"Equals - Recursion");
    final List<Object> c = List.from(a);
    final List<Object> d = List.from(b);
    assertEquals(c,d,"Equals - Recursion+Wrapped");
    a[1] = b;
    b[1] = a;
    assertEquals(a,b,"Equals - Recursion+Intertwined");
    final List<Object> e = List.from(a);
    final List<Object> f = List.from(b);
    assertEquals(c,d,"Equals - Recursion+Intertwined+Wrapped");
  }

  public static void main(String[] args) {
    testLexer();
    testAST();
    testEquals();
  }
}
