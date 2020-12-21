package com.github.severinnitsche.algebraic_data_structures;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.fantasyland.Apply;
import com.github.severinnitsche.fantasyland.Functor;
import com.github.severinnitsche.fantasyland.Monoid;
//import com.github.severinnitsche.function.algebra.structure.Sum;

@Apply
//@Sum(sum = {Optional.Nil.class, Optional.Cons.class})
public sealed interface Optional<O> extends Functor<O>permits Optional.Nil, Optional.Cons {

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
  }
}
