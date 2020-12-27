package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

@Extend
public interface Comonad<a> extends Functor {
  a extract();
}
