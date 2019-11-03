package org.scalalint.rules

import org.scalalint.configurations.PackageRulesConfiguration
import org.scalalint.{ ConfiguredSyntacticRule, Violation }

import scala.meta._
import scalafix.v1._

object PackageRules {
  final case class ChainedPackage(pkg: Pkg) extends Violation(pkg, "Chained package clauses are disallowed")
}
class PackageRules(config: PackageRulesConfiguration)
    extends ConfiguredSyntacticRule("ScalalintPackages", PackageRulesConfiguration()) {
  import PackageRules._

  def this() = this(PackageRulesConfiguration())

  override def configured(config: PackageRulesConfiguration): PackageRules =
    new PackageRules(config)

  override def fix(implicit doc: SyntacticDocument): Patch =
    doc.tree.collect {
      case pkg: Pkg =>
        if (config.disableRelativePackages)
          pkg.stats.collect { case innerPackage: Pkg => Patch.lint(ChainedPackage(innerPackage)) }.asPatch
        else
          Patch.empty
    }.asPatch
}
