package com.github.severinnitsche.function_generator.util.unary;

import com.github.severinnitsche.function_generator.util.InterfaceType;
import com.github.severinnitsche.function_generator.util.NameAndType;
import com.github.severinnitsche.function_generator.util.Types;

import java.io.IOException;

public record UnaryNameAndType(Types out, Types in, InterfaceType type) implements NameAndType {

  private static final String CONSUMER;
  private static final String SUPPLIER;
  private static final String FUNCTION;
  private static final String PREDICATE;

  static {
    String consumer, supplier, function, predicate;
    consumer = supplier = function = predicate = "";
    try {
      consumer = new String(UnaryNameAndType.class.getResourceAsStream("blueprint/Consumer").readAllBytes());
      supplier = new String(UnaryNameAndType.class.getResourceAsStream("blueprint/Supplier").readAllBytes());
      function = new String(UnaryNameAndType.class.getResourceAsStream("blueprint/Function").readAllBytes());
      predicate = new String(UnaryNameAndType.class.getResourceAsStream("blueprint/Predicate").readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    CONSUMER = consumer;
    SUPPLIER = supplier;
    FUNCTION = function;
    PREDICATE = predicate;
  }

  public String iTypeName() {
    if (in == Types.GENERIC) return "I";
    return in.type();
  }

  public String oTypeName() {
    if (out == Types.GENERIC) return "O";
    return out.type();
  }

  @Override
  public String typeName() {
    return switch (type) {
      case CONSUMER -> {
        if (in == Types.GENERIC) yield "Consumer<I>";
        yield in.capitalized() + "Consumer";
      }
      case FUNCTION -> {
        if (in == Types.GENERIC && out == Types.GENERIC) yield "Function<I,O>";
        if (in == Types.GENERIC && predicate()) yield "Predicate<I>";
        if (in == Types.GENERIC) yield "To" + out.capitalized() + "Function<I>";
        if (out == Types.GENERIC) yield in.capitalized() + "Function<O>";
        if (in == out) yield in.capitalized() + "UnaryOperator";
        if (predicate()) yield in.capitalized() + "Predicate";
        yield in.capitalized() + "To" + out.capitalized() + "Function";
      }
      case SUPPLIER -> {
        if (out == Types.GENERIC) yield "Supplier<O>";
        yield out.capitalized() + "Supplier";
      }
    };
  }

  public String className() {
    return switch (type) {
      case CONSUMER -> {
        if (in == Types.GENERIC) yield "Consumer";
        yield in.capitalized() + "Consumer";
      }
      case FUNCTION -> {
        if (in == Types.GENERIC && out == Types.GENERIC) yield "Function";
        else if (in == Types.GENERIC && predicate()) yield "Predicate";
        else if (in == Types.GENERIC) yield "To" + out.capitalized() + "Function";
        else if (out == Types.GENERIC) yield in.capitalized() + "Function";
        else if (in == out) yield in.capitalized() + "UnaryOperator";
        else if (predicate()) yield in.capitalized() + "Predicate";
        yield in.capitalized() + "To" + out.capitalized() + "Function";
      }
      case SUPPLIER -> {
        if (out == Types.GENERIC) yield "Supplier";
        yield out.capitalized() + "Supplier";
      }
    };
  }

  private boolean predicate() {
    return out == Types.BOOLEAN;
  }

  public String asClass() {
    String typeName = typeName();
    return switch (type) {
      case FUNCTION -> {
        if (predicate())
          yield String.format(PREDICATE, typeName, iTypeName(), typeName, typeName, typeName, typeName, typeName);
        yield String.format(FUNCTION, typeName, oTypeName(), iTypeName());
      }
      case CONSUMER -> String.format(CONSUMER, typeName, iTypeName(), typeName, typeName);
      case SUPPLIER -> String.format(SUPPLIER, typeName, oTypeName());
    };
  }

  @Override
  public String generics() {
    if (in == Types.GENERIC && out == Types.GENERIC && type == InterfaceType.FUNCTION) return "<" + iTypeName() + "," + oTypeName() + ">";
    if (in == Types.GENERIC && (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<" + iTypeName() + ">";
    if (out == Types.GENERIC && (type == InterfaceType.FUNCTION || type == InterfaceType.SUPPLIER)) return "<" + oTypeName() + ">";
    return "";
  }

  @Override
  public String rType() {
    return type == InterfaceType.CONSUMER ? "void" : oTypeName();
  }

  @Override
  public String params() {
    return type == InterfaceType.SUPPLIER ? "" : "i1";
    //return type == InterfaceType.SUPPLIER ? new String[]{} : new String[]{iTypeName()};
  }

  @Override
  public String method() {
    return switch (type) {
      case FUNCTION -> {
        if(predicate()) yield "test";
        yield "apply";
      }
      case CONSUMER -> "accept";
      case SUPPLIER -> "get";
    };
  }

}
