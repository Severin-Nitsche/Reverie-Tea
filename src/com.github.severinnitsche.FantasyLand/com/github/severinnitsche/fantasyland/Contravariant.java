package com.github.severinnitsche.fantasyland;


import com.github.severinnitsche.dreamer.require;
import com.github.severinnitsche.function.Function;

@require(fantasy = "contramap :: Contravariant f => f a ~> (b -> a) -> f b")
public interface Contravariant<T> {
  <U> Contravariant<U> contramap(Function<U,T> mapper);
  //must be overridden with the correct Contravariant
  // contramap :: Contravariant f => f a ~> (b -> a) -> f b
  //This can be an interface because one could have for example a List of Contravariants and actually invoke contramap
}
