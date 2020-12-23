package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Apply
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@require(fantasy = "of :: Applicative f => a -> f a")
public @interface Applicative {

}
