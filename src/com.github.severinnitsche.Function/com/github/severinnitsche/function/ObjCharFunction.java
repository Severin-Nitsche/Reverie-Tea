package com.github.severinnitsche.function;

@FunctionalInterface
public interface ObjCharFunction<I1,O> {

  O apply(I1 v1, char v2);

}
