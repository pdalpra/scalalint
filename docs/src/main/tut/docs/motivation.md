---
layout: docs
title: Motivation
---

# Motivation

## Why another Scala linter ?!

![XKCD: Standards](https://imgs.xkcd.com/comics/standards.png)

There are several existing Scala linters available, notably:

* [Scalastyle](http://www.scalastyle.org)
* [Wartremover](https://www.wartremover.org)
* [Scapegoat](https://github.com/sksamuel/scapegoat)

They are working very well, but:

* They are 'limited' to linting, and do not allow rewrites
* Scalastyle relies and Scalariform parser, which is less standard than scala Meta's and may not survive Scala 3
* Wartremover and Scapegoat rely on compiler plugins

[Scala Meta](https://scalameta.org/) and [Scalafix](https://scalacenter.github.io/scalafix) are comparatively newer tools,
but they are already at the core of many technologies, notably [Scalafmt](https://scalameta.org/scalafmt/) and [Metals](https://scalameta.org/metals/).

Scalafix in particular offers a principled 'framework' to build lint rules and rewrites, on which Scalalint build upon.

There are currently not that many rules implemented in Scalalint compared to other linters, but I hope to change that quickly.