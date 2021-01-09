package com.github.severinnitsche.util;

import com.github.severinnitsche.algebraic_data_structures.List;
import com.github.severinnitsche.algebraic_data_structures.Either;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.function.CheckedFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Array;

public final class Objects {
  private Objects() {}

  private static Function<Object,Function<Object,CheckedFunction<List<Object>,Boolean,IllegalAccessException>>> verifyCurried(Field field) {
    return a -> b -> noFollow -> verify(field,a,b,noFollow);
  }

  private static boolean verify(Field field, Object a, Object b, List<Object> noFollow) throws IllegalAccessException {
    field.setAccessible(true);
    //System.out.printf("checking:%n%s%n%s%n",field.get(a),field.get(b));
    if(field.getType().isPrimitive()) {
      if(field.getType() == boolean.class)
        return field.getBoolean(a) == field.getBoolean(b);
      else if(field.getType() == byte.class)
        return field.getByte(a) == field.getByte(b);
      else if(field.getType() == char.class)
        return field.getChar(a) == field.getChar(b);
      else if(field.getType() == short.class)
        return field.getShort(a) == field.getShort(b);
      else if(field.getType() == int.class)
        return field.getInt(a) == field.getInt(b);
      else if(field.getType() == long.class)
        return field.getLong(a) == field.getLong(b);
      else if(field.getType() == float.class)
        return field.getFloat(a) == field.getFloat(b);
      else /*if(field.getType() == double.class)*/ {
        assert field.getType() == double.class : "Unknown Primitive"+field.getType();
        return field.getDouble(a) == field.getDouble(b);
      }
    } else {
      final var av = field.get(a);
      final var bv = field.get(b);
      boolean ar = false;
      boolean br = false;
      for (List<Object> list = noFollow; list instanceof List.Cons<Object> cons; list = cons.tail()) {
        if (cons.head() == av)
          ar = true;
        if (cons.head() == bv)
          br = true;
      }
      if(ar && br)
        return true;
      if(ar != br)
        return false;
      return deepEquals(field.get(a),field.get(b),noFollow.prepend(av).prepend(bv));
    }
    //final var bv = field.get(b);
  }

  private static Either<IllegalAccessException,Boolean> _deepEquals(Object a, Object b, List<Object> noFollow)  {
    //try {
    //  System.out.printf("checking fields: %s%n",Arrays.toString(a.getClass().getDeclaredFields()));
    //} catch(Exception ignored) {}
    return Arrays.reduce(
      a.getClass().getDeclaredFields(),
      (result,field) -> result.flatmap(v1 -> wrap(verifyCurried(field).apply(a).apply(b)).apply(noFollow).map(v2 -> v1 && v2)),
      Either.<IllegalAccessException,Boolean>from(true)
    );
  }

  private static Function<Object,CheckedFunction<List<Object>,Either<IllegalAccessException,Boolean>,SecurityException>> _deepEqualsCurried(Object a) {
    return b -> noFollow -> _deepEquals(a,b,noFollow);
  }

  private static boolean verifyArray(Object a, Object b, List<Object> noFollow) throws IllegalArgumentException {
    if(Array.getLength(a) != Array.getLength(b))
      return false;
    outer:
    for(int i = 0; i < Array.getLength(a); i++) {
      if(a.getClass().componentType().isPrimitive()) {
        if(a.getClass().componentType() == boolean.class &&
           Array.getInt(a,i) != Array.getInt(b,i) ||
           a.getClass().componentType() == byte.class &&
           Array.getByte(a,i) != Array.getByte(b,i) ||
           a.getClass().componentType() == char.class &&
           Array.getChar(a,i) != Array.getChar(b,i) ||
           a.getClass().componentType() == short.class &&
           Array.getShort(a,i) != Array.getShort(b,i) ||
           a.getClass().componentType() == int.class &&
           Array.getInt(a,i) != Array.getInt(b,i) ||
           a.getClass().componentType() == long.class &&
           Array.getLong(a,i) != Array.getLong(b,i) ||
           a.getClass().componentType() == float.class &&
           Array.getFloat(a,i) != Array.getFloat(b,i) ||
           a.getClass().componentType() == double.class &&
           Array.getDouble(a,i) != Array.getDouble(b,i)
        ) {
          return false;
        }
      } else {
        final var av = Array.get(a,i);
        final var bv = Array.get(b,i);
        boolean ar = false;
        boolean br = false;
        for (List<Object> list = noFollow; list instanceof List.Cons<Object> cons; list = cons.tail()) {
          if (cons.head() == av)
            ar = true;
          if (cons.head() == bv)
            br = true;
        }
        if(ar && br)
          continue outer;
        if(ar != br)
          return false;
        if(!deepEquals(av,bv,noFollow.prepend(av).prepend(bv)))
          return false;
      }
    }
    return true;
  }

  private static Function<Object,CheckedFunction<List<Object>,Boolean,IllegalArgumentException>> verifyArrayCurried(Object a) {
    return b -> noFollow -> verifyArray(a,b,noFollow);
  }

  private static boolean isWrapper(Class<?> clazz) {
    return clazz == Boolean.class ||
           clazz == Byte.class ||
           clazz == Character.class ||
           clazz == Short.class ||
           clazz == Integer.class ||
           clazz == Long.class ||
           clazz == Float.class ||
           clazz == Double.class;
  }

  private static boolean deepEquals(Object a, Object b, List<Object> noFollow) {
    //System.out.printf("comparing:%n%s%n%s%n",a,b);
    //System.out.printf("a.getClass() -> %s%n",a.getClass());
    //System.out.printf("b.getClass() -> %s%n",b.getClass());
    if(a == b)
      return true;
    if(a.getClass() != b.getClass()) //Type inequality
      return false;
    if(a.getClass().isArray())
      return wrap(verifyArrayCurried(a).apply(b)).apply(noFollow) instanceof Either.Right<?,Boolean> r ? r.value() : a.equals(b);
    if(isWrapper(a.getClass()))
      return a.equals(b);
    return wrapEither(_deepEqualsCurried(a).apply(b)).apply(noFollow) instanceof Either.Right<?,Boolean> r ? r.value() : a.equals(b);
  }

  public static boolean deepEquals(Object a, Object b) {
    return deepEquals(a,b,List.from(a,b));
  }

  public static <I,O,E extends Either<?,O>,T extends Throwable> Function<I,Either<?,O>> wrapEither(CheckedFunction<I,E,T> checked) {
    return i -> {
      try {
        return checked.apply(i);
      } catch(Throwable t) {
        return Either.from(t);
      }
    };
  }

  public static <I,O,T extends Throwable> Function<I,Either<T,O>> wrap(CheckedFunction<I,O,T> checked) {
    return i -> {
      try {
        return Either.from(checked.apply(i));
      } catch(Throwable t) {
        return Either.from((T)t);
      }
    };
  }
}
