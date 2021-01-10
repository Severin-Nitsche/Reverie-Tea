package com.github.severinnitsche.algebraic_data_types;

import com.github.severinnitsche.function.*;
import com.github.severinnitsche.fantasyland.*;
import com.github.severinnitsche.dreamer.*;
import com.github.severinnitsche.util.Arrays;

//public abstract sealed class List permits List.Nil, List.Cons {
@StrictMode
@Monoid
@Semigroup
public sealed interface CharList permits CharList.Cons, CharList.Nil {

  <U> List<U> map(CharFunction<U> mapper);

  int size();

  <R> R reduceRight(ObjCharFunction<R,R> r, R acc);

  <R> R reduce(ObjCharFunction<R,R> r, R acc);

  char elemAt(int i);

  default CharList concat(CharList other) {
    return reduceRight(Cons::new,other);
  }

  default CharList append(char val) {
    return reduceRight(Cons::new,new Cons(val));
  }

  default CharList prepend(char val) {
    return new Cons(this,val);
  }

  default char[] toArray(IntFunction<char[]> arr) {
    record Tuple(char[] val, int index) {}
    return reduce((tuple,val)->{
      tuple.val[tuple.index] = val;
      return new Tuple(tuple.val,tuple.index+1);
    },new Tuple(arr.apply(size()),0)).val;
  }

  static CharList empty() {
    return new Nil();
  }

  static CharList from(char... source) {
    return Arrays.reduceRight(source,Cons::new,empty());
  }

  @StrictMode
  record Cons(CharList tail, char head) implements CharList {

    private Cons(char head) {
      this(new Nil(),head);
    }

    @Override
    public <U> List.Cons<U> map(CharFunction<U> mapper) {
      return new List.Cons<>(tail.map(mapper),mapper.apply(head));
    }

    @Override
    public int size() {
      return 1 + tail.size();
    }

    @Override
    public <R> R reduceRight(ObjCharFunction<R,R> r, R acc) {
      return r.apply(tail.reduceRight(r,acc),head);
    }

    @Override
    public <R> R reduce(ObjCharFunction<R,R> r, R acc) {
      return tail.reduce(r,r.apply(acc,head));
    }

    @Override
    public char elemAt(int i) {
      return i == 0 ? head : tail.elemAt(i-1);
    }
  }

  @StrictMode
  record Nil() implements CharList {

    @Override
    public <U> List.Nil<U> map(CharFunction<U> mapper) {
      return new List.Nil<>();
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public <R> R reduceRight(ObjCharFunction<R,R> r, R acc) {
      return acc;
    }

    @Override
    public <R> R reduce(ObjCharFunction<R,R> r, R acc) {
      return acc;
    }

    @Override
    public char elemAt(int i) {
      return 0;
    }
  }

}
