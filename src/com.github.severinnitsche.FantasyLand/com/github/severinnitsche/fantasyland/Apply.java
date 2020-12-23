package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@require(
  fantasy = "ap :: Apply f => f a ~> f (a -> b) -> f b",
  superTypes = {Functor.class}
)
public @interface Apply {
  //This does not make sense as interface because:
  //ap :: Apply f => f a ~> f (a -> b) -> f b
  //requires the same Type as argument and not *any* Apply

}
