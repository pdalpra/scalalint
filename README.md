## Scalalint

![Travis](https://img.shields.io/travis/pdalpra/scalalint) ![Maven metadata URL](https://img.shields.io/maven-metadata/v?label=maven%20central&metadataUrl=http%3A%2F%2Fcentral.maven.org%2Fmaven2%2Forg%2Fscalalint%2Frules_2.12%2Fmaven-metadata.xml) ![Gitter](https://img.shields.io/gitter/room/scalalint/scalalint)

Scalalint is a Scala linter tool, built using [Scalafix](https://scalacenter.github.io/scalafix/).

### Setup

Setup the `scalafix` plugin in your sbt build. In `project/plugins.sbt`, add:
```sbt
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.8")
```

Then enable Scalalint rules in your build, in `build.sbt`:

```sbt
scalafixDependencies in ThisBuild += "org.scalalint" %% "rules" % "0.1.2"
```

For more information, head to the [microsite](https://scalalint.org) !