package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Alt
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@reuire(fantasy = "zero :: Plus f => () -> f a")
public @interface Plus {

}
