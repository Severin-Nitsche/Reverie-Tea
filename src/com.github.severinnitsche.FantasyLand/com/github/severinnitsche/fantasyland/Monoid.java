package com.github.severinnitsche.fantasyland;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.severinnitsche.dreamer.require;

/**
 * <p>Fantasy Land Monoid</p>
 * <p>Ugly specification since its impossible to mandate a static method to be inherited</p>
 */
@Semigroup
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@require(fantasy = "empty :: Monoid m => () -> m")
public @interface Monoid {
  // This can not be an interface because one can not demand a static method this way
  // empty :: Monoid a => () -> a
  // Semigroup must be implemented
}
