package com.github.severinnitsche.fantasyland;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Fantasy Land Semigroup.</p>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Semigroup {
  // This can not be an interface because one could never invoke concat with the truly correct type if one
  //  only has the type info Semigroup<?>
}
