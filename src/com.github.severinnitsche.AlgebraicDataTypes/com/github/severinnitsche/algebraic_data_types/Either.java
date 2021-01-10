package com.github.severinnitsche.algebraic_data_types;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.fantasyland.*;
import com.github.severinnitsche.dreamer.StrictMode;

@StrictMode
@Chain
public sealed interface Either<L extends Throwable, R> extends Functor<R> permits Either.Left, Either.Right {
  @Override
  <U> Either<L,U> map(Function<R, U> mapper);

  <U> Either<L,U> chain(Function<R,Either<L,U>> mapper);

  @SuppressWarnings("unchecked")
  default <B> Either<L,B> ap(Either<L,Function<R,B>> mapper) {
    if(mapper instanceof Right<L,Function<R,B>> success) return map(success.value);
    else return (Either<L,B>)mapper; //there is no value of type B
  }

  static <L extends Throwable,R>Either<L,R> from(R value) {
    return new Right<>(value);
  }

  static <L extends Throwable,R>Either<L,R> from(L throwable) {
    return new Left<>(throwable);
  }

  @StrictMode
  record Left<L extends Throwable,R>(L throwable) implements Either<L,R>{
    @Override
    public <U> Left<L, U> map(Function<R, U> mapper) {
      return new Left<>(throwable);
    }

    @Override
    public <U> Either<L, U> chain(Function<R, Either<L, U>> mapper) {
      return Either.from(throwable);
    }
  }
  @StrictMode
  record Right<L extends Throwable,R>(R value) implements Either<L,R>{
    @Override
    public <U> Right<L, U> map(Function<R, U> mapper) {
      return new Right<>(mapper.apply(value));
    }

    @Override
    public <U> Either<L, U> chain(Function<R, Either<L, U>> mapper) {
      Right<L,Either<L,U>> mapped = map(mapper);
      if(mapped.value instanceof Right<L,U> right) return from(right.value);
      else return from(((Left<L,U>)mapped.value).throwable);
    }
  }
}
