package com.github.severinnitsche.algebraic_data_structures;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.fantasyland.*;
import com.github.severinnitsche.dreamer.*;

@Alt
@Plus
@StrictMode
public interface Map<a,b> extends Function<a,Optional<b>> {
  default Map<a,b> alt(Map<a,b> map) {
    return i -> this.apply(i).alt(map.apply(i));
  }
  static <c,d> Map<c,d> zero() {
    return i -> Optional.Nil();
  }
}
