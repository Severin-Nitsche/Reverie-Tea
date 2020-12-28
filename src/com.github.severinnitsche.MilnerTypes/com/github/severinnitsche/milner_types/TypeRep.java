package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.algebraic_data_structures.List;

public sealed interface TypeRep permits TypeRep.Algebraic, TypeRep.Func, TypeRep.TypeVariable {
  public record TypeVariable(String identifier, List<Algebraic> constraints) implements TypeRep {

  }
  public sealed interface Algebraic extends TypeRep permits Algebraic.Sum, Algebraic.Product, Algebraic.Unit {
    List<Algebraic> directSuperTypes();

    private static List<Algebraic> _superTypes(List<Algebraic> types) {
      return types.concat(_superTypes(types.chain(Algebraic::directSuperTypes).uniq())).uniq();
    }

    default List<Algebraic> superTypes() {
      return _superTypes(directSuperTypes());
    }
    public record Unit(List<Algebraic> directSuperTypes, List<TypeVariable> typeVariable) implements Algebraic {

    }
    public record Sum(List<Algebraic> directSuperTypes, List<TypeVariable> typeVariable, List<TypeRep> sum) implements Algebraic {

    }
    public record Product(List<Algebraic> directSuperTypes, List<TypeVariable> typeVariable, List<TypeRep> product) implements Algebraic {

    }
  }
  public sealed interface Func extends TypeRep permits Func.Function, Func.Method {
    public record Function(List<TypeRep> in, List<TypeRep> out, List<TypeVariable> typeVariable) implements Func {

    }
    public record Method(Algebraic in, Function out, List<TypeVariable> typeVariable) implements Func {

    }
  }
}
