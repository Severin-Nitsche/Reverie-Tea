package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.algebraic_data_types.Either;

import static com.github.severinnitsche.algebraic_data_types.Either.from;

public sealed interface Tokenized permits Tokenized.Literal, Tokenized.Colon, Tokenized.Comma, Tokenized.Arrow, Tokenized.Method, Tokenized.Function, Tokenized.LeftBracket, Tokenized.RightBracket {

  record Literal(String value) implements Tokenized {
    public static final String REGEX = "[\\w./\\[]+";
  }

  record Colon() implements Tokenized {
    public static final String REGEX = "::";
  }

  record Comma() implements Tokenized {
    public static final String REGEX = ",";
  }

  record Arrow() implements Tokenized {
    public static final String REGEX = "=>";
  }

  record Method() implements Tokenized {
    public static final String REGEX = "~>";
  }

  record Function() implements Tokenized {
    public static final String REGEX = "->";
  }

  record LeftBracket() implements Tokenized {
    public static final String REGEX = "\\(";
  }

  record RightBracket() implements Tokenized {
    public static final String REGEX = "\\)";
  }

  static Either<Throwable,Tokenized> of(String s) {
    if(s.matches(Literal.REGEX)) return from(new Literal(s));
    else if(s.matches(Colon.REGEX)) return from(new Colon());
    else if(s.matches(Comma.REGEX)) return from(new Comma());
    else if(s.matches(Arrow.REGEX)) return from(new Arrow());
    else if(s.matches(Method.REGEX)) return from(new Method());
    else if(s.matches(Function.REGEX)) return from(new Function());
    else if(s.matches(RightBracket.REGEX)) return from(new RightBracket());
    else if(s.matches(LeftBracket.REGEX)) return from(new LeftBracket());
    else return from(new Throwable("No match found for "+s));
  }

}
