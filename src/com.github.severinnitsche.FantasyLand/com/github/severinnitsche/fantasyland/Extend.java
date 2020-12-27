package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

@require(
  fantasy = "extend :: Extend w => w a ~> (w a -> b) -> w b",
  superTypes = Functor.class
)
public @interface Extend {

}
