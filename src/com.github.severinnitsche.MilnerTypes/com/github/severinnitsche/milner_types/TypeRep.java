package com.github.severinnitsche.milner_types;

import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.algebraic_data_structures.List;

public sealed interface TypeRep permits TypeRep.Sum, TypeRep.Product {
  List<TypeRep> directSuperTypes();

  private static List<TypeRep> _superTypes(List<TypeRep> types) {
    return types.concat(_superTypes(types.chain(TypeRep::directSuperTypes).uniq())).uniq();
  }

  default List<TypeRep> superTypes() {
    return _superTypes(directSuperTypes());
  }
  public record Sum(List<TypeRep> directSuperTypes, List<TypeRep> sum) implements TypeRep {

  }
  public record Product(List<TypeRep> directSuperTypes, List<TypeRep> product) implements TypeRep {

  }
}
