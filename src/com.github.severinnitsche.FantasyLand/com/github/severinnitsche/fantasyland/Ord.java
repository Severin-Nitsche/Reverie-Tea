package com.github.severinnitsche.fantasyland;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.severinnitsche.dreamer.require;

/**
 * <p>Fantasy Land Ord.</p>
 */
@Setoid
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@require(fantasy = "lte :: Ord a => a ~> a -> Boolean")
public @interface Ord {
  // This can not be an interface because one could never invoke lte with the truly correct type if one
  //  only has the type info Ord<?>
}
