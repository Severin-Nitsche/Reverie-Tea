package com.github.severinnitsche.function_generator.util;

import java.util.regex.Pattern;

public interface NameAndType {

  String className();

  String typeName();

  default String safeTypeName() {
    return safeClassName() + safeGenerics();
  }

  default String sWTypeName() {
    return safeClassName() + sWGenerics();
  }

  String asClass();

  default String safeClassName() {
    return "Checked" + className();
  }

  default String asSafeClass() {
    //TODO: try replaceAll(typeName(),safeTypeNAme())
    return asClass().replaceAll(typeName(), sWTypeName()).
        replaceFirst(Pattern.quote(sWTypeName()), safeTypeName()).
        replaceFirst("function", "function.checked").
        replaceFirst("\\);", ") throws E;").
        replaceFirst("\\{\n","{\n\n".
        concat(!rType().equals("void") ? String.format(
            """
                  static %s com.github.severinnitsche.datastructures.function.%s unchecked(%s safe) {
                    return (%s) -> {
                      try {
                        return safe.%s(%s);
                      } catch (Throwable scammed) {
                        com.github.severinnitsche.datastructures.unsafe.Unchecked.throwsUnchecked(scammed);
                        throw new RuntimeException(scammed);
                      }
                    };
                  }

                  static %s com.github.severinnitsche.datastructures.function.%s unchecked(%s safe, %s alt) {
                    return (%s) -> {
                      try {
                        return safe.%s(%s);
                      } catch (Throwable scammed) {
                        com.github.severinnitsche.datastructures.unsafe.Unchecked.throwsUnchecked(scammed);
                        throw new RuntimeException(scammed);
                      }
                    };
                  }
                """, generics(), typeName(), sWTypeName(), params(), method(), params(),
            generics(), typeName(), sWTypeName(), rType(), params(), method(), params()) : String.format(
            """
                  static %s com.github.severinnitsche.datastructures.function.%s unchecked(%s safe) {
                     return (%s) -> {
                       try {
                         safe.%s(%s);
                       } catch (Throwable scammed) {
                         com.github.severinnitsche.datastructures.unsafe.Unchecked.throwsUnchecked(scammed);
                         throw new RuntimeException(scammed);
                       }
                     };
                   }
                """, generics(), typeName(), sWTypeName(), params(), method(), params())));
  }

  String method();

  String generics();

  default String safeGenerics() {
    if (generics().equals("")) return "<E extends Throwable>";
    return generics().replaceAll(">", ", E extends Throwable>");
  }

  default String sWGenerics() {
    if (generics().equals("")) return "<?>";
    return generics().replaceAll(">", ", ?>");
  }

  String rType();

  String params();

}
