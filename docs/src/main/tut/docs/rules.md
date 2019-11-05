---
layout: docs
title: Rules
---

# Rules

The following rules are currently available in Scalalint:

* [ScalalintImports](#scalalintimports)
* [ScalalintPackages](#scalalintpackages)

Those are only 'meta rules', head below to the rules documentation to find which lints and rewrites are available

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

