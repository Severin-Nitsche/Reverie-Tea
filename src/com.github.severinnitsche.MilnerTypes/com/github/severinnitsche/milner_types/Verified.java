package com.github.severinnitsche.milner_types;

public sealed interface Verified permits Verified.Success, Verified.Failure {
  record Success() implements Verified {

  }

  record Failure(String reason) implements Verified {

  }

  static Verified verify(AST.Complex.NamedType type, TypeRep rep) {
    return new Failure("not yet implemented");
  }
}
