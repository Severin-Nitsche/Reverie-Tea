package com.github.severinnitsche.algebraic_data_structures;

import com.github.severinnitsche.function.BiFunction;
import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.function.IntFunction;
import com.github.severinnitsche.fantasyland.*;
import com.github.severinnitsche.dreamer.*;
import com.github.severinnitsche.util.Arrays;

@StrictMode
@Monoid
@Semigroup
public sealed interface List<A> extends Functor<A> permits List.Cons, List.Nil {

  final class ListIndexOutOfBoundsException extends Throwable {
  }

  @Override
  <U> List<U> map(Function<A,U> mapper);

  int size();

  <R> R reduceRight(BiFunction<R,A,R> r, R acc);

  <R> R reduce(BiFunction<R,A,R> r, R acc);

  Either<Throwable,A> elemAt(int i);

  default List<A> concat(List<A> other) {
    return reduceRight(Cons::new,other);
  }

  default List<A> append(A val) {
    return reduceRight(Cons::new,new Cons<>(val));
  }

  default List<A> prepend(A val) {
    return new Cons<>(this,val);
  }

  default A[] toArray(IntFunction<A[]> arr) {
    record Tuple<R>(R[] val, int index) {}
    return reduce((tuple,val)->{
      tuple.val[tuple.index] = val;
      return new Tuple<>(tuple.val,tuple.index+1);
    },new Tuple<>(arr.apply(size()),0)).val;
  }

  static <T> List<T> empty() {
    return new Nil<>();
  }

  static <T> List<T> from(T... source) {
    return Arrays.reduceRight(source,Cons::new,empty());
  }

  static <T> List<T> from(java.util.List<T> source) {
    return source.stream().reduce(List.empty(), List::append, List::concat);
  }

  @StrictMode
  record Cons<C>(List<C> tail, C head) implements List<C> {

    private Cons(C head) {
      this(new Nil<>(),head);
    }

    @Override
    public <U> Cons<U> map(Function<C, U> mapper) {
      return new Cons<>(tail.map(mapper),mapper.apply(head));
    }

    @Override
    public int size() {
      return 1 + tail.size();
    }

    @Override
    public <R> R reduceRight(BiFunction<R, C, R> r, R acc) {
      return r.apply(tail.reduceRight(r,acc),head);
    }

    @Override
    public <R> R reduce(BiFunction<R, C, R> r, R acc) {
      return tail.reduce(r,r.apply(acc,head));
    }

    @Override
    public Either<Throwable, C> elemAt(int i) {
      return i == 0 ? Either.from(head) : tail.elemAt(i-1);
    }
  }

  @StrictMode
  record Nil<C>() implements List<C> {

    @Override
    public <U> Nil<U> map(Function<C, U> mapper) {
      return new Nil<>();
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public <R> R reduceRight(BiFunction<R, C, R> r, R acc) {
      return acc;
    }

    @Override
    public <R> R reduce(BiFunction<R, C, R> r, R acc) {
      return acc;
    }

    @Override
    public Either<Throwable, C> elemAt(int i) {
      return Either.from(new ListIndexOutOfBoundsException());
    }
  }

}
