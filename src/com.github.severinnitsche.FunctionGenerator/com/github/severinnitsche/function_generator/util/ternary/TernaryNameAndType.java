package com.github.severinnitsche.function_generator.util.ternary;

import com.github.severinnitsche.function_generator.Generate;
import com.github.severinnitsche.function_generator.util.InterfaceType;
import com.github.severinnitsche.function_generator.util.NameAndType;
import com.github.severinnitsche.function_generator.util.Types;

import java.io.IOException;

public record TernaryNameAndType(Types out, Types in1, Types in2, Types in3,
                                 InterfaceType type) implements NameAndType {

  private static final String CONSUMER;
  private static final String FUNCTION;
  private static final String PREDICATE;

  static {
    String consumer, function, predicate;
    consumer = function = predicate = "";
    try {
      consumer = new String(TernaryNameAndType.class.getResourceAsStream("blueprint/Consumer").readAllBytes());
      function = new String(TernaryNameAndType.class.getResourceAsStream("blueprint/Function").readAllBytes());
      predicate = new String(TernaryNameAndType.class.getResourceAsStream("blueprint/Predicate").readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    CONSUMER = consumer;
    FUNCTION = function;
    PREDICATE = predicate;
  }

  public TernaryNameAndType {
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

  public String i3TypeName() {
    if (in3 == Types.GENERIC) return "I3";
    return in3.type();
  }

  public String oTypeName() {
    if (out == Types.GENERIC) return "O";
    return out.type();
  }

  @Override
  public String typeName() {
    return switch (type) {
      case CONSUMER -> {
        if (in1 == in2 && in2 == in3 && in3 == Types.GENERIC) yield "TriConsumer<I1,I2,I3>";
        if (in1 == in2 && in2 == in3) yield in1.capitalized() + "TriConsumer";
        if (in1 == in2 && in2 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I1,I2>";
        if (in1 == in3 && in3 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I1,I3>";
        if (in2 == in3 && in3 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I2,I3>";
        if (in1 == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I1>";
        if (in2 == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I2>";
        if (in3 == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer<I3>";
        yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Consumer";
      }
      case FUNCTION -> {
        if (in1 == in2 && in2 == in3 && in3 == Types.GENERIC && out == Types.GENERIC) yield "TriFunction<I1,I2,I3,O>";
        if (in1 == in2 && in2 == in3 && in1 == Types.GENERIC && predicate()) yield "TriPredicate<I1,I2,I3>";
        if (in1 == in2 && in2 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I1,I2>";
        if (in1 == in3 && in3 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I1,I3>";
        if (in2 == in3 && in3 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I2,I3>";
        if (in1 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I1>";
        if (in2 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I2>";
        if (in3 == Types.GENERIC && predicate())
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate<I3>";
        if (in1 == in2 && in2 == in3 && in1 == Types.GENERIC) yield "To" + out.capitalized() + "TriFunction<I1,I2,I3>";
        if (in1 == Types.GENERIC && in2 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I1,I2,O>";
        if (in1 == Types.GENERIC && in3 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I1,I3,O>";
        if (in2 == Types.GENERIC && in3 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I2,I3,O>";
        if (in1 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I1,O>";
        if (in2 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I2,O>";
        if (in3 == Types.GENERIC && out == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<I3,O>";
        if (in1 == in2 && in2 == in3 && out == Types.GENERIC) yield in1.capitalized() + "TriFunction<O>";
        if (out == Types.GENERIC) yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Function<O>";
        if (in1 == Types.GENERIC && in2 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I1,I2>";
        if (in1 == Types.GENERIC && in3 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I1,I3>";
        if (in2 == Types.GENERIC && in3 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I2,I3>";
        if (in1 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I1>";
        if (in2 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I2>";
        if (in3 == Types.GENERIC)
          yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function<I3>";
        if (in1 == in2 && in2 == in3 && in1 == out) yield in1.capitalized() + "TernaryOperator";
        if (in1 == in2 && in2 == in3 && predicate()) yield in1.capitalized() + "TriPredicate";
        if (predicate()) yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "Predicate";
        if (in1 == in2 && in2 == in3) yield in1.capitalized() + "TriTo" + out.capitalized() + "Function";
        yield in1.capitalized() + in2.capitalized() + in3.capitalized() + "To" + out.capitalized() + "Function";
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
          yield String.format(PREDICATE, typeName, i1TypeName(), i2TypeName(), i3TypeName(), typeName, typeName, typeName, typeName, typeName);
        yield String.format(FUNCTION, typeName, oTypeName(), i1TypeName(), i2TypeName(), i3TypeName());
      }
      case CONSUMER -> String.format(CONSUMER, typeName, i1TypeName(), i2TypeName(), i3TypeName(), typeName, typeName);
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
    if (in1 == Types.GENERIC && in2 == Types.GENERIC && in3 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,I2,I3,O>";
    if (in1 == Types.GENERIC && in2 == Types.GENERIC && in3 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1,I2,I3>";
    if (in1 == Types.GENERIC && in2 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,I2,O>";
    if (in1 == Types.GENERIC && in3 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,I3,O>";
    if (in2 == Types.GENERIC && in3 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I2,I3,O>";
    if (in1 == Types.GENERIC && in2 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1,I2>";
    if (in1 == Types.GENERIC && in3 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1,I3>";
    if (in1 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I1,O>";
    if (in2 == Types.GENERIC && in3 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I2,I3>";
    if (in2 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I2,O>";
    if (in3 == Types.GENERIC && out == Types.GENERIC &&
        type == InterfaceType.FUNCTION) return "<I3,O>";
    if (in1 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I1>";
    if (in2 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I2>";
    if (in3 == Types.GENERIC &&
        (type == InterfaceType.FUNCTION || type == InterfaceType.CONSUMER)) return "<I3>";
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
    return type == InterfaceType.SUPPLIER ? "" : "i1, i2, i3";
    //return type == InterfaceType.SUPPLIER ? new String[]{} : new String[]{i1TypeName(), i2TypeName(), i3TypeName()};
  }

}
