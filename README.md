# Reverie Tea

This project aims to order the mess of the Data-Structures-Project of mine, and outsource the FP part.

## Objective:
- [ ] Exhaustive Library containing various functional interfaces types (Tri-/Bi-/Function, etc.)
- [ ] Comprehensive fp Library containing various algebraic data structures
- [ ] Complete implementation of the FantasyLand Specification using Interfaces/Annotations
  - [x] Setoid
  - [x] Ord
  - [x] SemiGroup
  - [x] Monoid
  - [x] Functor
  - [x] Contravariant
  - [x] Apply
  - [x] Applicative
  - [x] Alt
  - [x] Plus
  - [x] Alternative
  - [x] Foldable
  - [x] Traversable
  - [x] Chain
  - [x] ChainRec
  - [x] Monad
  - [x] Extend
  - [x] Comonad
  - [ ] Bifunctor
  - [ ] Profunctor
  - [ ] Semigroupoid
  - [ ] Category

## Other Objectives:
- [ ] Annotation Processor to verify Code integrity…
  - [ ] …of Meta-Annotations
  - [ ] …annotated Types
  - [ ] …annotated Type Parameters
- [ ] Implementation of the Damas-Hindley-Milner type signatures
  - [ ] Lexer
  - [ ] Parser
  - [ ] Verifier

## Module Dependency & Naming
- The module containing functional interfaces shall be named _Function_
- The module containing the generator of the functional interfaces shall be named _Function Generator_
- The module containing algebraic data structures shall be named _Algebraic Data Structures_
- The module containing the Fantasy Land implementation shall be named _Fantasy Land_
- The module containing the Annotation Processor shall be named _Reverie Conceiver_
- The module containing the Annotations (require, Required, StrictMode) used by the Annotaion Processor shall be named _Dreamer_
- The module containing the Damas-Hindley-Milner type signatures shall be named _Milner Types_

Dependencies shall be as followed:
- [x] The module _Reverie Conceiver_ is dependent on _Milner Types_
- [x] The module _Milner Types_ is transitive dependent on _Algebraic Data Structures_
- [x] The module _Algebraic Data Structures_ is transitive dependent on _Fantasy Land_
- [x] ~~The module _Algebraic Data Structures_ is transitive dependent on _Function_~~
- [x] The module _Fantasy Land_ is transitive dependent on _Dreamer_ **it is not dependent on _Reverie Conceiver_**
- [x] The module _Fantasy Land_ is transitive dependent on _Function_
- [x] The module _Dreamer_ is **not** dependent on _Milner Types_ **Strings are used as arguments/type definitions**
- [x] The module _Function_ is independent of other modules
- [x] The module _Function Generator_ is dependent on _java.compiler_

---

## Build
To build the project simply use the included Makefile:
```shell
make project
```
To build the project with annotation processors enabled use:
```shell
make annotated
```

## Resources:
  Annotation Processor: [Java Specification](https://docs.oracle.com/en/java/javase/15/docs/api/java.compiler/javax/lang/model/type/package-summary.html)
  Fantasy Land: [Fantas, Eel, and Specification](http://www.tomharding.me/fantasy-land/ "by Tom Harding")
