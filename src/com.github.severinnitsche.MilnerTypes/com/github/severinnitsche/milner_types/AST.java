package com.github.severinnitsche.milner_types;

//import com.github.severinnitsche.function.algebra.structure.Sum;
import com.github.severinnitsche.algebraic_data_types.*;

import static com.github.severinnitsche.algebraic_data_types.List.from;
import static com.github.severinnitsche.algebraic_data_types.Optional.*;

//@Sum(sum = {AST.TypeCons, AST.Type, AST.Complex})
public sealed interface AST permits AST.TypeCons, AST.Type, AST.Complex {
  record TypeCons(Type base, Type type) implements AST {
  }

  sealed interface Type extends AST permits Type.ConsType, Type.Void, Type.Tuple, Type.Function, Type.Method {
    record ConsType(String name, List<Type> args) implements Type {
    }

    record Void() implements Type {
    }

    record Tuple(List<Type> types) implements Type {

    }

    record Function(Type in, Type out) implements Type {
    }

    record Method(Type in, Function out) implements Type {
    }
  }

  sealed interface Complex extends AST permits Complex.TypeDef, Complex.NamedType {
    record TypeDef(List<TypeCons> constraints, Type type) implements Complex {
    }

    record NamedType(String name, TypeDef type) implements Complex {
    }
  }

  static Either<Throwable, Complex.NamedType> of(String fantasy) {
    return Lexer.tokenize(fantasy).chain(AST::of);
  }

  static Either<Throwable, Complex.NamedType> of(List<Tokenized> tokens) {
    record ACC(Optional<ACC> next, List<Type> types, Optional<Type.Tuple> tuple, Optional<Type> right, Optional<Tokenized> hold,
               List<TypeCons> constraints, Optional<Complex> typedef) {
      ACC insertLiteral(Tokenized.Literal literal) {
        if (next instanceof Optional.Cons<ACC> cons) //forward insertion if there is another layer
          return new ACC(cons.map(acc -> acc.insertLiteral(literal)), ACC.this.types, ACC.this.tuple, ACC.this.right, ACC.this.hold,
              ACC.this.constraints, ACC.this.typedef);
        else if (ACC.this.hold instanceof Cons<Tokenized> cons && cons.value() instanceof Tokenized.Colon &&
            ACC.this.typedef instanceof Cons<Complex> c2 && c2.value() instanceof Complex.TypeDef typedef) //name this type
          return new ACC(Nil(), from(), Nil(), Nil(), Nil(), from(), Optional(new Complex.NamedType(literal.value(), typedef)));
        else //insert in this layer
          return new ACC(next, ACC.this.types.prepend(new Type.ConsType(literal.value(), from())), ACC.this.tuple,
              ACC.this.right, hold, ACC.this.constraints, ACC.this.typedef);
      }

      ACC newLayer() {
        if (next instanceof Cons<ACC> cons)
          return new ACC(Optional(cons.value().newLayer()),types,tuple,right,hold,constraints,typedef);
        else
          return new ACC(Optional(new ACC(Nil(), new List.Nil<>(), Nil(), Nil(), Nil(), from(), Nil())), ACC.this.types,
              tuple,ACC.this.right, ACC.this.hold, ACC.this.constraints, ACC.this.typedef);
      }

      Either<Throwable, Type> reduceTypes(List<Type> types) {
        if (types instanceof List.Cons<Type> cons) //not ()
          if (cons.head() instanceof Type.ConsType consType) //eg. not ((a -> b) -> c); but eg. (a b -> c)
            return Either.from(new Type.ConsType(consType.name(), cons.tail()));
          else if (cons.tail() instanceof List.Nil) //eg. ((a -> b) -> c)
            return Either.from(cons.head());
          else //eg. ((a -> b) c -> c)
            return Either.from(new Throwable("Error: No Type arguments allowed for functions"));
        else // ()
          return Either.from(new Type.Void());
      }

      Either<Throwable, Type> reduceSimple() {//TODO: add tuple support
        if (next instanceof Nil)
          if (ACC.this.hold instanceof Cons<Tokenized> held)
            return reduceTypes(ACC.this.types).chain(left -> {
              if (ACC.this.right instanceof Cons<Type> cons)
                if (held.value() instanceof Tokenized.Method)
                  if (cons.value() instanceof Type.Function func)
                    return Either.from(new Type.Method(left, func));
                  else
                    return Either.from(new Throwable("Error: expected Function after ~>"));
                else if (held.value() instanceof Tokenized.Function)
                  return Either.from(new Type.Function(left, cons.value()));
                else
                  return Either.from(new Throwable(""));
              else
                return Either.from(new Throwable("Error: missing right-side argument"));
            });
          else
            return reduceTypes(ACC.this.types);
        else
          return Either.from(new Throwable("Error: Simple reduction not possible"));
      }

      Either<Throwable, ACC> reduce2() {
        if (next instanceof Cons<ACC> l1)
          if (l1.value().next instanceof Cons<ACC>) //more than one layer => forward reduction
            return l1.value().reduce2().map(acc -> new ACC(Optional(acc), ACC.this.types, ACC.this.tuple, ACC.this.right, ACC.this.hold,
                ACC.this.constraints, ACC.this.typedef));
          else //one layer => reduction
            if(l1.value().tuple instanceof Cons<Type.Tuple> cons)
              return l1.value().reduceSimple().map(type -> new ACC(Nil(), types.prepend(new Type.Tuple(cons.value().types().prepend(type))),ACC.this.tuple,ACC.this.right,ACC.this.hold,ACC.this.constraints,ACC.this.typedef));
            else
              return l1.value().reduceSimple().map(type -> new ACC(Nil(), ACC.this.types.prepend(type), ACC.this.tuple, ACC.this.right,
                ACC.this.hold, ACC.this.constraints, ACC.this.typedef));
        else // (
          return Either.from(new Throwable("Error: Missing )"));
      }

      Either<Throwable, Type> reduceFunction() {
        return reduceTypes(ACC.this.types).chain(left -> {
          if (ACC.this.next instanceof Nil)
            if (ACC.this.right instanceof Cons<Type> to)
              if (this.hold instanceof Cons<Tokenized> held)
                if (held.value() instanceof Tokenized.Function)
                  return Either.from(new Type.Function(left, to.value()));
                else if (held.value() instanceof Tokenized.Method)
                  if (to.value() instanceof Type.Function right)
                    return Either.from(new Type.Method(left, right));
                  else
                    return Either.from(new Throwable("Error: expected Function after ~>"));
                else
                  return Either.from(new Throwable("Error: expected Function or Method in hold"));
              else
                return Either.from(new Throwable("Error: hold is empty"));
            else
              return Either.from(new Throwable("Error: no right-side argument"));
          else
            return Either.from(new Throwable("Error: pending )"));
        });
      }

      Either<Throwable, ACC> insertFunction(Tokenized func) {
        if (next instanceof Cons<ACC> l1)
          return l1.value().insertFunction(func).map(inner -> new ACC(Cons(inner), types, tuple, right, hold, constraints, typedef));
        else if (ACC.this.hold instanceof Nil)
          if(ACC.this.tuple instanceof Cons<Type.Tuple> cons)
            return reduceTypes(types).map(type -> new ACC(next, from(), Nil(), tuple.map(t -> new Type.Tuple(t.types.prepend(type))),Optional(func),constraints,typedef));
          else
            return reduceTypes(ACC.this.types).map(type -> new ACC(ACC.this.next, from(), tuple, Optional(type),
              Optional(func), ACC.this.constraints, ACC.this.typedef));
        else
          return reduceFunction().map(function -> new ACC(ACC.this.next, from(), tuple, Optional(function),
              Optional(func), ACC.this.constraints, ACC.this.typedef));
      }

      Either<Throwable, ACC> reduceToFunction(Tokenized.Arrow arrow) {
        return reduceFunction().map(func -> new ACC(Nil(), from(), Nil(), Optional(func), Optional(arrow), from(), Nil()));
      }

      Either<Throwable, ACC> addConstraint() {
        if (ACC.this.hold instanceof Cons<Tokenized> arrow && arrow.value() instanceof Tokenized.Arrow)
          if (ACC.this.types.size() == 2)
            if (ACC.this.right instanceof Cons<Type> cons)
              if (cons.value() instanceof Type.Function || cons.value() instanceof Type.Method)
                if (ACC.this.next instanceof Nil)
                  return ACC.this.types.elemAt(0).chain(type -> ACC.this.types.elemAt(1).map(
                      base -> new ACC(Nil(), from(), Nil(), ACC.this.right, ACC.this.hold,
                          ACC.this.constraints.prepend(new TypeCons(base, type)), ACC.this.typedef)
                  ));
                else
                  return Either.from(new Throwable("Error: pending )"));
              else
                return Either.from(new Throwable("Error: invalid State " + this));
            else
              return Either.from(new Throwable("Error: invalid State " + this));
          else
            return Either.from(new Throwable("Error: expected two Literals"));
        else
          return Either.from(new Throwable("Error: hold not Arrow"));
      }

      Either<Throwable, ACC> _reduceConstraints(Tokenized.Colon colon) {
        if (ACC.this.hold instanceof Cons<Tokenized> arrow && arrow.value() instanceof Tokenized.Arrow)
          if (ACC.this.right instanceof Cons<Type> r)
            return Either.from(new ACC(Nil(), from(), Nil(), Nil(), Optional(colon), from(),
                Optional(new Complex.TypeDef(ACC.this.constraints, r.value()))));
          else
            return Either.from(new Throwable("Error: no type Present"));
        else
          return reduceFunction().map(func -> new ACC(Nil(), from(), Nil(), Nil(), Optional(colon), from(),
              Optional(new Complex.TypeDef(ACC.this.constraints, func))));
      }

      Either<Throwable, ACC> reduceConstraints(Tokenized.Colon colon) {
        if (ACC.this.hold instanceof Cons<Tokenized> cons && cons.value() instanceof Tokenized.Arrow)
          return addConstraint().chain(ac -> ac._reduceConstraints(colon));
        else
          return _reduceConstraints(colon);
      }

      Either<Throwable, ACC> expandTuple() {
        if(next instanceof Cons<ACC> accCons)
          return accCons.value().expandTuple().map(inner -> new ACC(Optional(inner),types,tuple,right,hold,constraints,typedef));
        else
          if(tuple instanceof Cons<Type.Tuple>)
            return reduceTypes(types).map(type -> new ACC(next,from(),tuple.map(tuple1 -> new Type.Tuple(tuple1.types.prepend(type))),right,hold,constraints,typedef));
          else
            return reduceTypes(types).map(type -> new ACC(next,from(),Optional(new Type.Tuple(from(type))),right,hold,constraints,typedef));
      }

      Either<Throwable, ACC> handleComma() {
        if(ACC.this.next instanceof Nil) //we are near the end -> add a type constraint
          return addConstraint();
        else //We are inside a bracket -> reduce types and create a tuple
          return expandTuple();
      }
    }

    return tokens.reduceRight(
        (either, token) -> either.chain(acc -> {
          if (token instanceof Tokenized.Literal literal)
            return Either.from(acc.insertLiteral(literal));
          else if (token instanceof Tokenized.RightBracket)
            return Either.from(acc.newLayer());
          else if (token instanceof Tokenized.LeftBracket)
            return acc.reduce2();
          else if (token instanceof Tokenized.Function || token instanceof Tokenized.Method)
            return acc.insertFunction(token);
          else if (token instanceof Tokenized.Arrow arrow)
            return acc.reduceToFunction(arrow);
          else if (token instanceof Tokenized.Comma)
            return acc.handleComma();
            //return acc.addConstraint();
          else if (token instanceof Tokenized.Colon colon)
            return acc.reduceConstraints(colon);
          else
            return Either.from(new Throwable("not implemented yet"));
        }),
        Either.from(new ACC(Nil(), from(), Nil(), Nil(), Nil(), from(), Nil()))
    ).chain(acc -> {
      if (acc.typedef instanceof Cons<Complex> cons && cons.value() instanceof Complex.NamedType type)
        return Either.from(type);
      else
        return Either.from(new Throwable("Error: could not create NamedType from sources"));
    });
  }
}
