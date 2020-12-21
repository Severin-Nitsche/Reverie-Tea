package com.github.severinnitsche.function_generator.util.binary;

import com.github.severinnitsche.function_generator.util.InterfaceType;
import com.github.severinnitsche.function_generator.util.NameAndType;
import com.github.severinnitsche.function_generator.util.Types;
import com.github.severinnitsche.function_generator.util.ternary.TernaryNameAndType;

import java.io.IOException;
import java.util.function.Function;

public record BinaryNameAndType(Types out, Types in1, Types in2, InterfaceType type) implements NameAndType {

  private static final String CONSUMER;
  private static final String FUNCTION;
  private static final String PREDICATE;

  static {
    String consumer, function, predicate;
    consumer = function = predicate = "";
    try {
      consumer = new String(BinaryNameAndType.class.getResourceAsStream("blueprint/Consumer").readAllBytes());
      function = new String(BinaryNameAndType.class.getResourceAsStream("blueprint/Function").readAllBytes());
      predicate = new String(BinaryNameAndType.class.getResourceAsStream("blueprint/Predicate").readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    CONSUMER = consumer;
    FUNCTION = function;
    PREDICATE = predicate;
  }

  public BinaryNameAndType {
    if (type == InterfaceType.SUPPLIER) throw new IllegalArgumentException();
  }

  public String i1TypeName() {
    if (in1 == Types.GENERIC) return "I1";
    return in1.type();
  }

  public String i2TypeName() {
    if (in2 == Types.GENERIC) return "I2";
    return in2.type();
  }

  public String oTypeName() {
    if (out == Types.GENERIC) return "O";
    return out.type();
  }

  @Override
  public String typeName() {
    return switch (type) {
      case CONSUMER -> {
        if (in1 == in2 && in2 == Types.GENERIC) yield "BiConsumer<I1,I2>";
        if (in1 == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + "Consumer<I1>";
        if (in2 == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + "Consumer<I2>";
        if (in1 == in2) yield in1.capitalized() + "BiConsumer";
        yield in1.capitalized() + in2.capitalized() + "Consumer";
      }
      case FUNCTION -> {
        if (in1 == in2 && in1 == Types.GENERIC && out == Types.GENERIC) yield "BiFunction<I1,I2,O>";
        if (in1 == in2 && in1 == Types.GENERIC && predicate()) yield "BiPredicate<I1,I2>";
        if (in1 == Types.GENERIC && predicate()) yield "Obj" + in2.capitalized() + "Predicate<I1>";
        if (in2 == Types.GENERIC && predicate()) yield in1.capitalized() + "Obj" + "Predicate<I2>";
        if (in1 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + "Function<I1,O>";
        if (in2 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + "Function<I2,O>";
        if (in1 == in2 && out == Types.GENERIC) yield in1.capitalized() + "BiFunction<O>";
        if (out == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + "Function<O>";
        if (in1 == in2 && in1 == Types.GENERIC) yield "To" + out.capitalized() + "BiFunction<I1,I2>";
        if (in1 == Types.GENERIC) yield "Obj" + in2.capitalized() + "To" + out.capitalized() + "Function<I1>";
        if (in2 == Types.GENERIC) yield in1.capitalized() + "ObjTo" + out.capitalized() + "Function<I2>";
        if (in1 == in2 && in1 == out) yield in1.capitalized() + "BinaryOperator";
        if (in1 == in2 && predicate()) yield in1.capitalized() + "BiPredicate";
        if (predicate()) yield in1.capitalized() + in2.capitalized() + "Predicate";
        if (in1 == in2) yield in1.capitalized() + "BiTo" + out.capitalized() + "Function";
        yield in1.capitalized() + in2.capitalized() + "To" + out.capitalized() + "Function";
      }
      case SUPPLIER -> null;
    };
  }

  public String className() {
    return typeName().replaceAll("<.*>", "");
  }

  private boolean predicate() {
    return out == Types.BOOLEAN;
  }

  public String asClass() {
    String typeName = typeName();
    return switch (type) {
      case FUNCTION -> {
        if (predicate())
          yield String.format(PREDICATE, typeName, i1TypeName(), i2TypeName(), typeName, typeName, typeName, typeName, typeName);
        yield String.format(FUNCTION, typeName, oTypeName(), i1TypeName(), i2TypeName());
      }
      case CONSUMER -> String.format(CONSUMER, typeName, i1TypeName(), i2TypeName(), typeName, typeName);
      case SUPPLIER -> null;
    };
  }

  @Override
  public String method() {
    return switch (type) {
      case FUNCTION -> {
        if (predicate()) yield "test";
        yield "apply";
      }
      case CONSUMER -> "accept";
      case SUPPLIER -> "get";
    };
  }

  @Override
  public String generics() {
    if (in1 == Types.GENERIC && in2 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,I2,O>";
    if (in1 == Types.GENERIC && in2 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1,I2>";
    if (in1 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,O>";
    if (in2 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I2,O>";
    if (in1 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1>";
    if (in2 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I2>";
    if (out == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.SUPPLIER)) return "<O>";
    return "";
  }

  @Override
  public String rType() {
    return type == InterfaceType.CONSUMER ? "void" : oTypeName();
  }

  @Override
  public String params() {
    return type == InterfaceType.SUPPLIER ? "" : "i1, i2";
    //return type == InterfaceType.SUPPLIER ? new String[]{} : new String[]{i1TypeName(), i2TypeName()};
  }

}
