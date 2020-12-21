package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.algebraic_data_structures.*;
import com.github.severinnitsche.util.*;

class Lexer {
  static Either<Throwable, List<Tokenized>> tokenize(String s) {
    return Strings.
        reduce(s,
            (either, c) -> either.flatmap(
                accumulator -> accumulator.accumulate(c)
            ),
            Either.from(new CharAccumulator<>(
                CharList.empty(),
                List.empty(),
                list -> Tokenized.of(list.reduce(StringBuilder::append, new StringBuilder()).toString()),
                (list, c) -> Character.isAlphabetic(c) || Character.isWhitespace(c) ||
                    c == '(' || c == ')' || c == '-' || c == '>' || c == ':' || c == ',' || c == '~' || c == '=' ||
                    c == '.' || c == '/' || c == '[',
                (list, c) -> c == '-' || c == '~' || c == '=' || c == ',' || c == '(' || c == ')' ||
                    (c == ':' && list instanceof CharList.Cons cons && cons.head() != ':'),
                (list, c) -> Character.isWhitespace(c),
                (list, c) -> c == '>' || c == ',' || c == '(' || c == ')' ||
                    (c == ':' && list instanceof CharList.Cons cons && cons.head() == ':')
            ))
        ).
        flatmap(CharAccumulator::reduce).
        map(CharAccumulator::accumulated);
  }
}
