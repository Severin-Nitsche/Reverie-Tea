package com.github.severinnitsche.algebraic_data_types;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.fantasyland.*;
import com.github.severinnitsche.dreamer.*;
//import com.github.severinnitsche.function.algebra.structure.Sum;

@Apply
@Alt
@StrictMode
//@Sum(sum = {Optional.Nil.class, Optional.Cons.class})
public sealed interface Optional<O> extends Functor<O> permits Optional.Nil, Optional.Cons {

  //-------------------------------------------------Constructor
  static <T> Nil<T> Nil() {
    return new Nil<>();
  }

  static <T> Cons<T> Cons(T value) {
    return new Cons<>(value);
  }

  @SuppressWarnings("MethodNameSameAsClassName")
  static <T> Optional<T> Optional(T value) {
    if(value == null)
      return Nil();
    else
      return Cons(value);
  }

  //----------------------------------Functor
  @Override
  <U> Optional<U> map(Function<O, U> mapper);

  //--------------------------------------------Apply
  <U> Optional<U> ap(Optional<Function<O, U>> mapper);

  //-------------------------------------------------Other
  <U> Optional<U> flatmap(Function<O,Optional<U>> mapper);

  //--------Alt
  Optional<O> alt(Optional<O> alt);

  //-----------------------------------------------Sum
  record Nil<T>() implements Optional<T> {
    @Override
    public <U> Optional<U> map(Function<T, U> mapper) {
      return Nil();
    }

    @Override
    public <U> Optional<U> ap(Optional<Function<T, U>> mapper) {
      return Nil();
    }

    @Override
    public <U> Optional<U> flatmap(Function<T, Optional<U>> mapper) {
      return Nil();
    }

    @Override
    public Optional<T> alt(Optional<T> alt) {
      return alt;
    }
  }

  record Cons<T>(T value) implements Optional<T> {
    @Override
    public <U> Optional<U> map(Function<T, U> mapper) {
      return Cons(mapper.apply(value));
    }

    @Override
    public <U> Optional<U> ap(Optional<Function<T, U>> mapper) {
      if (mapper instanceof Cons<Function<T, U>> cons)
        return Cons(cons.value.apply(value));
      else
        return Nil();
    }

    @Override
    public <U> Optional<U> flatmap(Function<T, Optional<U>> mapper) {
      return mapper.apply(value);
    }

    @Override
    public Optional<T> alt(Optional<T> alt) {
      return this;
    }
  }
}
