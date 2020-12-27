package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import com.github.severinnitsche.function.Function;

@require(
  fantasy = "traverse :: Applicative f, Traversable t => t a ~> (TypeRep f, a -> f b) -> f (t b)"
)
public interface Traversable<a> extends Functor, Foldable {
  <@Applicative f, a, b> f traverse(Class<f> clazz, Function<a,f> func);
}
