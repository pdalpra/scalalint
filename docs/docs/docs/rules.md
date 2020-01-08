---
layout: docs
title: Rules
---

# Rules

The following rules are currently available in Scalalint:

* [ScalalintClasses](#scalalintclasses)
* [ScalalintImports](#scalalintimports)
* [ScalalintInference](#scalalintinference)
* [ScalalintPackages](#scalalintpackages)

Those are only 'meta rules', head below to the rules documentation to find which lints and rewrites are available

## ScalalintClasses

### finalCaseClass

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

Extending case classes can break them in surprising ways, as they define reasonable implementations of `hashCode`, `equals` and `toString` that breaks with inheritance.

For more information and examples, you can have a look at Nicolas Rinaudo's [excellent Scala best practices article](https://nrinaudo.github.io/scala-best-practices/tricky_behaviours/final_case_classes.html).
```scala
// forbidden by default with ScalalintClasses
case class Foo(i: Int)
```
### leakingSealed

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

Sealing a trait or a class ensures that you can't extend it outside of the file it's defined in.

However, a sealed trait or class can "leak" if one of its implementing classes or trait isn't itself final or sealed.
This can lead to surprising behaviors and silently breaks pattern matching exhaustiveness checking.

```scala
sealed trait Foo

// sealed trait Foo leaks through class 'Bar', forbidden by default with ScalalintClasses 
class Bar extends Foo
// sealed trait Foo leaks through trait 'Quz', forbidden by default with ScalalintClasses
trait Quz extends Foo
```

### removeBracesOnEmptyBody

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

If a class/trait has no body, braces are redundant and can be removed.

```scala
// Redundant braces, will be removed by removeBracesOnEmptyBody = true
class Foo {}
```
### removeEmptyConstructor

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

If a class public constructor has no parameters, it is redundant and can be removed.

```scala
// Redundant empty constructor, will be removed by removeEmptyConstructor = true
class Foo()
```

## ScalalintImports

### disableBlockImports

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `false`

**Justification**:

One might want to disable block imports as a policy and prefer single imports only.

```scala
// would be forbidden with ScalalintImports.disableBlockImports = true
import scala.util.{ Try, Success }
```

### disableRelativeImports

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

Relative imports are confusing, and pollutes the imports namespace which can introduce shadowing.

```scala
import java.io        // Imports only a package and no members
import _root.io.netty // Need to use _root_ to disambiguate the import
```

### disableWildcardImports

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `false`

Conflicts with `rewriteWildcardThreshold`.

**Justification**:

One might want to disable wildcard imports as a policy and prefer explicit imports only.

```scala
// would be forbidden with ScalalintImports.disableWildcardImports = true
import scala.concurrent.duration._
```

### ensureImportsAreGrouped

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `false`

**Justification**:

If imports are not grouped and rather spread throughout the file, knowing what is in scope is difficult.

```scala
import scala.concurrent.duration._
import scala.concurrent.Future

object MyObject {
  import java.nio.file.Path    // would be forbidden with ScalalintImports.ensureImportsAreGrouped = true

  def deleteFile(path: Path): Unit = {
    import java.nio.file.Files // would be forbidden with ScalalintImports.ensureImportsAreGrouped = true
    Files.deleteIfExists(path)
  }
}
```

### forbiddenImports

* **Kind**: Lint
* **Type**: `List[String]`
* **Default**: `Nil`

**Justification**:

One might want to define a list of forbidden imports, either as their usage is deprecated or discouraged.
For example:
* `scala.collection.JavaConversions`: deprecated as of Scala 2.12
* `scala.concurrent.ExecutionContext.Implicits.global`: Usage of global ExecutionContext might be discouraged
* `sun.misc.Unsafe`: Can be dangerous if misused

### rewriteWildcardThreshold

* **Kind**: Rewrite
* **Type**: `Int`, the maximum number of imports in a block before rewriting to wildcard
* **Default**: Not set

Conflicts with `disableWildcardImports`.

**Justification**:

When importing many members of a package, it may be simpler to use a wildcard import.

```scala
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Try, Success, Failure, Random, Properties }
// with ScalaImports.rewriteWildcardThreshold = 5
import scala.util._
```

## ScalalintInference

### noPublicInference

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `false`

**Justification**:

Public members should always have explicit type annotations to ensure that public APIs have documented return types and not risk inferring over specific types.

```scala
def myOption = Some("") // Infers Some[String], not Option[String]

sealed trait ADT
case object A extends ADT
case object B extends ADT

def values = List(A, B) // Infers List[Product with Serializable with ADT], not List[ADT]
```

### noInferXXX

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`, except `noInferNothing`

**Justification**:

* `noInferAny`/`noInferAnyVal`/`noInferAnyRef`/`noInferObject`: Inferring those types is most often hint of an code issue, as this means 
  that unrelated types are mixed together and the compiler had to infer one of the root types of Scala/Java type systems 
* `noInferProduct`/`noInferSerializable`: `Product` & `Serializable` are often inferred by the compiler, eg. when ADTs values are mixed
  with generics are put in a List.
* `noInferNothing`: although inferring `Nothing` could be hint of an issue, it is however common to use it and leaves 'holes' in types
  that can filled later, eg. `Right("")` infers `Either[Nothing, String]`, with `Left` being filled later).

## ScalalintPackages

### disableRelativePackages

* **Kind**: Lint
* **Type**: `Boolean`
* **Default**: `true`

**Justification**:

Relative packages implicitly imports all the contents of each package, which can be confusing.

```scala
package foo.bar // => Implicitly imports foo.bar._
package quz     // => Implicitly imports foo.bar.quz._
```

