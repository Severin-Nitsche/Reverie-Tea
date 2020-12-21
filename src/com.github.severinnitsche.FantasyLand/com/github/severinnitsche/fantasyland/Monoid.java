package com.github.severinnitsche.fantasyland;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Fantasy Land Monoid</p>
 * <p>Ugly specification since its impossible to mandate a static method to be inherited</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monoid {
  // This can not be an interface because one can not demand a static method this way
  // empty :: Monoid a => () -> a
  // Semigroup must be implemented
}
