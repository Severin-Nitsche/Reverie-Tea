package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@require(fantasy = "ap :: Apply f => f a ~> f (a -> b) -> f b")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Apply {
  //This does not make sense as interface because:
  //ap :: Apply f => f a ~> f (a -> b) -> f b
  //requires the same Type as argument and not *any* Apply

}
