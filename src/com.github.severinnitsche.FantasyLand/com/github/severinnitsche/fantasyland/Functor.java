package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;
import com.github.severinnitsche.function.Function;

@require(fantasy = "map :: Functor f => f a ~> (a -> b) -> f b")
public interface Functor<T> {

  <U> Functor<U> map(Function<T, U> mapper);
  // must be overridden with the correct Functor
  // map :: Functor f => f a ~> (a -> b) -> f b
  // This can be an interface because one could have for example a List of Functors and actually invoke map

}
