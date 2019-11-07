---
layout: docs
title: Getting Started
---

# Getting Started

## Setup

Setup the `scalafix` plugin in your sbt build.In `project/plugins.sbt`, add:
```scala
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.8")
```

Then add Scalalint rules library in your build, in `build.sbt`:

```scala
scalafixDependencies in ThisBuild += "org.scalalint" %% "rules" % "0.1.2"
```

## Configuration

Now that Scalalint rules have been imported into your build, you can configure in `.scalafix.conf` [which rules](rules.html) to
enable in your build:

```scala

rules = [
  ScalalintImports,
  ScalalintPackages,
  ...
]

Scalalint.disableRelativeImports = true

```

