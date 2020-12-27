package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Apply
@require(fantasy = "chain :: Chain m ~> (a -> m b) -> m b")
public @interface Chain {

}
