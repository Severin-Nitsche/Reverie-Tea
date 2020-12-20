# Reverie Tea

This project aims to order the mess of the Data-Structures-Project of mine, and outsource the FP part.

## Objective:
- [] Comprehensive fp Library contining various algebraic data structures
- [] Complete implementation of the FantasyLand Specification using Interfaces/Annotations
  - [] Setoid
  - [] Ord
  - [] SemiGroup
  - [] Monoid
  - [] Functor
  - [] Contravariant
  - [] Apply
  - [] Applicative
  - [] Alt
  - [] Plus
  - [] Alternative
  - [] Foldable
  - [] Traversable
  - [] Chain
  - [] ChainRec
  - [] Monad
  - [] Extend
  - [] Comonad
  - [] Bifunctor
  - [] Profunctor
  - [] Semigroupoid
  - [] Category

## Other Objectives:
- [] Annotation Processor to verify Code integrity…
  - [] …of Meta-Annotations
  - [] …annotated Types
- [] Implementation of the Damas-Hindley-Milner type signatures
  - [] Lexer
  - [] Parser
  - [] Verifier

## Module Dependency & Naming
- The Module containing algebraic data structures shall be named _Algebraic Data Structures_
- The Module containing the Fantasy Land implementation shall be named _Fantasy Land_
- The Module containing the Annotation Processor shall be named _Reverie Conceiver_
- The Module containing the Damas-Hindley-Milner type signatures shall be named _Milner Types_
Dependencies shall be as followed:
_Milner Types_ <- _Reverie Conceiver_ <= _Fantasy Land_ <= _Algebraic Data Structures_

## Resources:
  Annotation Processor: [Java Specification](https://docs.oracle.com/en/java/javase/15/docs/api/java.compiler/javax/lang/model/type/package-summary.html)
  Fantasy Land: [Fantas, Eel, and Specification](http://www.tomharding.me/fantasy-land/ "by Tom Harding")
