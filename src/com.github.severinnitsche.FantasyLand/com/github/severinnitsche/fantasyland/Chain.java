package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

@Apply
@require(fantasy = "chain :: Chain m ~> (a -> m b) -> m b")
public @interface Chain {

}
