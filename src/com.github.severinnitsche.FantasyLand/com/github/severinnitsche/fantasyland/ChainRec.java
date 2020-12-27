package com.github.severinnitsche.fantasyland;

import com.github.severinnitsche.dreamer.require;

@Chain
@require(fantasy = "chainRec :: ChainRec m => ((a -> c, b -> c, a) -> m c, a) -> m b")
public @interface ChainRec {

}
