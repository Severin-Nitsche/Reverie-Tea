package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import com.github.severinnitsche.function.Function;

@require(fantasy = "bimap :: Bifunctor f => f a c ~> (a -> b, c -> d) -> f b d")
public interface Bifunctor<a, c> extends Functor {
  <b,d> Bifunctor<b, d> bimap(Function<a,b> f1, Function<c,d> f2);
}
