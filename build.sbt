lazy val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    organization := "org.scalalint",
    homepage := Some(url("https://www.scalalint.org")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    startYear := Some(2019),
    developers := List(
      Developer(
        "pdalpra",
        "Pierre Dal-Pra",
        "dalpra.pierre@gmail.com",
        url("https://www.scalalint.org")
      )
    ),
    scalaVersion := V.scala212,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-P:semanticdb:synthetics:on"
    )
  )
)

skip in publish := true

lazy val rules = project
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    moduleName := "rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
    headerLicense := Some(HeaderLicense.ALv2("2019", "Pierre Dal-Pra")),
    scalafmtOnCompile := true
  )

lazy val input  = project.configure(scalafixTestModule)
lazy val output = project.configure(scalafixTestModule)

lazy val tests = project
  .dependsOn(rules)
  .configure(scalafixTestModule)
  .enablePlugins(ScalafixTestkitPlugin)
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    Compile / compile := (Compile / compile).dependsOn(input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories := (output / Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories := (input / Compile / sourceDirectories).value,
    scalafixTestkitInputClasspath := (input / Compile / fullClasspath).value
  )

def scalafixTestModule(project: Project) =
  project.disablePlugins(ScalafmtPlugin).settings(skip in publish := true)
