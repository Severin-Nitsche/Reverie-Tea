package com.github.severinnitsche.function_generator;

import com.github.severinnitsche.function_generator.util.InterfaceType;
import com.github.severinnitsche.function_generator.util.NameAndType;
import com.github.severinnitsche.function_generator.util.binary.BinaryNameAndType;
import com.github.severinnitsche.function_generator.util.ternary.TernaryNameAndType;
import com.github.severinnitsche.function_generator.util.unary.UnaryNameAndType;
import com.github.severinnitsche.function_generator.util.Types;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.*;

@SupportedSourceVersion(value = SourceVersion.RELEASE_15)
@SupportedAnnotationTypes({"com.github.severinnitsche.datastructures.generator.Generate"})
public class Functions extends AbstractProcessor {

  private Filer filer;
  private Messager messager;

  private Map<String, NameAndType> created;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
    created = new LinkedHashMap<>();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Types out : Types.values())
      for (Types in1 : Types.values())
        for (Types in2 : Types.values())
          for (Types in3 : Types.values())
            for (InterfaceType type : InterfaceType.values())
              create(out, in1, in2, in3, type);
    //createUnsafe();
    return true;
  }

  public void createUnsafe() {
    try {
      messager.printMessage(Diagnostic.Kind.NOTE, "Populating Unsafe API");
      PrintStream unsafe = new PrintStream(filer.createSourceFile("Unsafe").openOutputStream());
      unsafe.printf("package com.github.severinnitsche.datastructures.unsafe;%n%n");
      unsafe.printf("import com.github.severinnitsche.datastructures.function.*;%n");
      unsafe.printf("import com.github.severinnitsche.datastructures.function.checked.*;%n%n");
      unsafe.printf("import static com.github.severinnitsche.datastructures.unsafe.Unchecked.throwsUnchecked;%n%n");
      unsafe.printf("public final class Unsafe {%n%n");
      for (var clazz : created.values()) {
        unsafe.printf("  public static %s %s unchecked(%s safe) {%n", clazz.generics(), clazz.typeName(), clazz.sWTypeName());
        unsafe.printf("    return (%s) -> {%n", clazz.params());
        unsafe.printf("      try {%n");
        if (!clazz.rType().equals("void"))
          unsafe.printf("        return safe.%s(%s);%n", clazz.method(), clazz.params());
        else
          unsafe.printf("        safe.%s(%s);%n", clazz.method(), clazz.params());
        unsafe.printf("      } catch (Throwable scammed) {%n");
        unsafe.printf("        throwsUnchecked(scammed);%n");
        unsafe.printf("        throw new RuntimeException(scammed);%n");
        unsafe.printf("      }%n");
        unsafe.printf("    };%n");
        unsafe.printf("  }%n%n");
        if (!clazz.rType().equals("void")) {
          unsafe.printf("  public static %s %s unchecked(%s safe, %s alt) {%n", clazz.generics(), clazz.typeName(), clazz.sWTypeName(), clazz.rType());
          unsafe.printf("    return (%s) -> {%n", clazz.params());
          unsafe.printf("      try {%n");
          unsafe.printf("        return safe.%s(%s);%n", clazz.method(), clazz.params());
          unsafe.printf("      } catch (Throwable scammed) {%n");
          unsafe.printf("        return alt;%n");
          unsafe.printf("      }%n");
          unsafe.printf("    };%n");
          unsafe.printf("  }%n%n");
        }
      }
      unsafe.printf("}%n");
      unsafe.flush();
      unsafe.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void create(Types out, Types in1, Types in2, Types in3, InterfaceType type) {
    try {
      out(new UnaryNameAndType(out, in1, type));
      out(new BinaryNameAndType(out, in1, in2, type));
      out(new TernaryNameAndType(out, in1, in2, in3, type));
    } catch (IllegalArgumentException ignored) {
    }
  }

  public void out(NameAndType current) {
    if (created.containsKey(current.className())) return;
    created.put(current.className(), current);

    try {
      messager.printMessage(Diagnostic.Kind.NOTE, "\u270eCreating " + current.className());
      var outputStream = filer.createSourceFile(current.className()).openOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
      writer.write(current.asClass());
      writer.flush();
      writer.close();
      //checked
      outputStream = filer.createSourceFile(current.safeClassName()).openOutputStream();
      writer = new BufferedWriter(new OutputStreamWriter(outputStream));
      writer.write(current.asSafeClass());
      writer.flush();
      writer.close();
      messager.printMessage(Diagnostic.Kind.NOTE, "\u2714Created " + current.className());
    } catch (IOException e) {
      messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, e.getMessage());
      messager.printMessage(Diagnostic.Kind.NOTE, created.toString());
      messager.printMessage(Diagnostic.Kind.WARNING, "\u2718Creating " + current.className() + " failed");
    }
  }

  public static void main(String[] args) {
    System.out.println("\033[0;33mCreating\033[39m ");
    Object foo = new Object[1];
    Object bar = foo;
    for (int i = 0; i < 500; i++)
      foo = ((Object[]) foo)[0] = new Object[1];
    System.out.println(Arrays.deepToString(((Object[]) bar)));
  }

}
