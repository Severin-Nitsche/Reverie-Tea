# API
This Specification lists the library interface visible to an outside viewer or user *excluding reflection* as implemented by the variety of different modules of this project, which is to be consistent across all different branches.

### Notation
All *package* names are marked in `this way` and are prefixed with `com.github.severinnitsche.` which may be ommitted due to convenience reasons. *Modules* are described by a heading consisting of their pretty Name immediately followed by a short description of set module and its packages whom public class members are described after.

### Laws

| Law | Description |
| --- | ----------- |
| *transitivity* | *__∀ A,B,C ∈ X : ((A,B) ∈ R ∧ (B,C) ∈ R) => (A,C) ∈ R__* |
| *symmetry*     | *__∀ A,B ∈ X : ((A,B) ∈ R <=> (B,C) ∈ R)__* |
| *reflexivity*  | *__∀ A ∈ X : (A,A) ∈ R__* |

### Concerns

Within the following tables, describing operations of the different algebras, there are some methods marked with numbers. Those methods are, while being supported at the current time not well thought out, and raise one or more concerns described in the following table.

| ID | Concern | Proposed Change |
| -- | ------- | --------------- |
| 1 | The problem here is that the method relies on some notion of equivalence, and though technically speaking every non-primitive Type in Java conforms to the [Setoid](http://www.tomharding.me/2017/03/09/fantas-eel-and-specification-3/) Specification, we decided to explicitly declare _Setoid_ Types with an annotation. However, since many implementations do not require their members to be _Setoid_ or contain completely unbound Types, we, even though there is an accessible _equals_ method that per [Definition](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object)) conforms to the Fantasy Land specification, did not explicitly defined it as a Setoid meaning that we probably didn't include the equals method by will but by the force of Javas narrow headed type system. | Methods like this may be factored out into static functions that respect the _@Setoid_ requirement of _a_. |
| 2 | Operations marked with this ID may potentially be realized by an algebraic structure of the [fantasy-land specification](https://github.com/fantasyland/fantasy-land), though possibly under a different name (ie. *__flatmap === chain__*). | Use the correct Structure |

### Code Conventions
#### Code Style
N/A
#### Code Structure
Classes that define a *Sum-Type* are realized via **sealed interfaces**. *Product-Types* are realized as **records**. Classes that contain only utility methods shall not be instantiable, though it is yet to be decided if they are to be implemented as **final classes** with private constructor access, **interfaces** without member functions and possible implementation preventions, **empty enums**, or something different. Please inform me of any ideas regarding an realization of the *[bottom-type](https://wiki.c2.com/?BottomType)* in Java. In the same spirit I also'd gladly accept your beautiful ideas regarding elegant implementations of *Unit-Types* or *Singletons* as one might call it.

## Algebraic Data Types
As the name suggests this package contains all sorts of aggregates as well as some useful utility classes. This is taken into account regarding the package allocation which consists of `util` and `algebraic_data_types`, whereas the latter of whom contains set aggregates and the former utility classes.\
`util`
- *Arrays*
- *Functions*
- *Objects*
- *Strings*

`algebraic_data_types`
- *List*
- *Optional* (may be renamed to *Maybe*)
- *Either*

### Arrays
*Package*: `util`\
*Simple Name*: **Arrays**

*Description*: This class has provides basic *List* functions such as *reduce* for Arrays as well as some more traditionally approaches like functions for pretty printing or linear search.
### Functions
*Package*: `util`\
*Simple Name*: **Functions**

*Description*: Collection of frequently used Functions and some extra utilities to cope with features not implemented directly in their belonging class.
### Objects
*Package*: `util`\
*Simple Name*: **Objects**

*Description*: Everything to deal with Objects.

| Signature | Description | Laws |
| --------- | ----------- | ---- |
| *__deepEquals :: (Object, Object) -> boolean__* | Returns true if and only if both Objects are equivalent in structure and values | *transitivity*, *reflexivity*, *symmetry* |

### Strings
*Package*: `util`\
*Simple Name*: **Strings**

*Description*: This class allows for Strings to be reduced like Lists.

| Signature | Description | Laws |
| --------- | ----------- | ---- |
| *__reduce :: Object a => (String, (a, char) -> a, a) -> a__* | Reduction on Strings |
| *__reduceRight ∷ Object a => (String, (a, char) -> a, a) -> a__* | Reduction on Strings |

### List
*Package*: `algebraic_data_types`\
*Simple Name*: **List** \
*Type Signature*: **Cons a | Nil**\
*Algebraic Structures*: **fantasyland.Chain**, **fantasyland.Monoid**
*Direct super Types*: **java.util.Iterable**

*Description*: A functional implementation of a LinkedList as Sum-Type implementing various suitable Fantasy Land Types.

| Signature | Description | Laws |
| --------- | ----------- | ---- |
| *__reduce :: Object r => List a ~> ((r, a) => r, r) -> r__* | reduction |
| *__reduceRight :: Object r => List a ~> ((r, a) => r, r) -> r__* | reduction but backwards |
| *__uniq :: List a ~> List a__* | removes all duplicate entries _1_ |
| *__elemAt :: List a ~> int -> Either Throwable a__* | returns the element at the specified position |
| *__append :: List a ~> a -> List a__* | returns a list which got the element appended |
| *__prepend :: List a ~> a -> List a__* | return a list which got the element prepended |
| *__contains :: List a ~> a -> boolean__* | returns whether a is contained in this list _1_ |
| *__toArray :: List a ~> (int -> Array a) -> a__* | returns an Array reflecting the elements of this List |
| *__from :: Array a -> List a__* | returns a List reflecting the elements of the Array _2_ |
| *__from :: java.util.List a -> List a__* | returns a List reflecting the elements of the List _2_ |

### Optional
*Package*: `algebraic_data_types`\
*Simple Name*: **Optional**\
*Type Signature*: **Cons a | Nil**\
*Algebraic Structures*: **fantasyland.Chain**, **fantasyland.Alt**

*Description*: Wrapper for Data-Types of values that may be absent without an error condition being present.
### Either
*Package*: `algebraic_data_types`\
*Simple Name*: **Either**\
*Type Signature*: *__Throwable e => Left e | Right a__* \
*Algebraic Structures*: **fantasyland.Chain**

*Description*: Wrapper for Data-Types of values that may be absent when an error condition is met.

| Signature | Description | Laws |
| --------- | ----------- | ---- |
| *__from :: Throwable l => r -> Either l r__* | This function serves as a constructor accepting a value |
| *__from :: Throwable l => l -> Either l r__* | This function serves as a constructor accepting an error / exception |

## Dreamer
This module contains annotations used to declare the usage of advanced type checking facilities provided by _[Reverie Conceiver](#reverie-conceiver)_.

### require
*Package*: `dreamer`\
*Simple Name*: **require**\
*Type Signature*: *__TypeDef__* \

*Description*: Type Annotation used to specify constraints on a class. May be used to specify constraints on an annotation that then, if strict-mode is enabled, enforces set constraints on annotated classes (even if the constraint may not be type checkable by Javas default Type System).

| Signature | Description | Laws |
| --------- | ----------- | ---- |
| *__fantasy :: require r => r ~> () -> String__* | The constraints | Must be in Hindley-Milner-notation |
| *__superTypes :: require r => r ~> () -> [Class<?>]__* | Super Types |

### Required
### StrictMode

## FantasyLand

## Function

## Function Generator

## Milner Types

## Reverie Conceiver

<style>
@import url('https://fonts.googleapis.com/css2?family=Fira+Code:wght@300&family=Montserrat&display=swap');
p,h1,h2,h3,h4,h5,h6,h7 {
  font-family: 'Montserrat', sans-serif;
}
/*td:nth-of-type(1)*/
em strong {
  font-family: 'Fira Code', monospace;
  font-style: normal;
  font-weight: normal;
  White-space: nowrap;
}
</style>
