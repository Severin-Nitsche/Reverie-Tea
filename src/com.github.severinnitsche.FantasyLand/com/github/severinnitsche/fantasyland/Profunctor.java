package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import com.github.severinnitsche.function.Function;

@require(fantasy = "promap :: Profunctor p => p b c ~> (a -> b, c -> d) -> p a d")
public interface Profunctor<b, c> extends Functor {
  <a, d> Profunctor<a,d> promap(Function<a,b> f1, Function<c,d> f2);
}
