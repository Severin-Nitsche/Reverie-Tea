package com.github.severinnitsche.function_generator.util;

public enum Types {
  BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, GENERIC;
  public String type() {
    return switch (this) {
      case BOOLEAN -> "boolean";
      case BYTE -> "byte";
      case CHAR -> "char";
      case SHORT -> "short";
      case INT -> "int";
      case LONG -> "long";
      case FLOAT -> "float";
      case DOUBLE -> "double";
      case GENERIC -> "";
    };
  }
  public String capitalized() {
    return switch (this) {
      case BOOLEAN -> "Boolean";
      case BYTE -> "Byte";
      case CHAR -> "Char";
      case SHORT -> "Short";
      case INT -> "Int";
      case LONG -> "Long";
      case FLOAT -> "Float";
      case DOUBLE -> "Double";
      case GENERIC -> "Obj";
    };
  }
}
