package com.pdalpra.scalalint

import scala.meta._

import com.pdalpra.scalalint.configurations.ImportRulesConfiguration
import scalafix.v1._

object ImportRules {
  final case class ForbiddenImport(tree: Importer)         extends Violation(tree, s"Importing '$tree' is disallowed")
  final case class ForbiddenWildcardImport(tree: Importer) extends Violation(tree, "Wildcard imports are disallowed")
}
class ImportRules(config: ImportRulesConfiguration)
    extends ConfiguredSyntacticRule("ScalalintImports", ImportRulesConfiguration()) {
  import ImportRules._

  def this() = this(ImportRulesConfiguration())

  override def configured(config: ImportRulesConfiguration): ImportRules =
    new ImportRules(config)

  override def fix(implicit doc: SyntacticDocument): Patch =
    doc.tree.collect {
      case importer: Importer =>
        List(
          checkForbiddenImport(importer),
          checkWildcardImport(importer),
          rewriteToWildcardImport(importer)
        ).asPatch
    }.asPatch

  private def checkForbiddenImport(importer: Importer): Patch =
    if (config.forbiddenImports.contains(importer.syntax))
      Patch.lint(ForbiddenImport(importer))
    else
      Patch.empty

  private def checkWildcardImport(importer: Importer): Patch =
    if (config.disableWildcardImports)
      importer.importees.collect { case _: Importee.Wildcard => Patch.lint(ForbiddenWildcardImport(importer)) }.asPatch
    else Patch.empty

  private def rewriteToWildcardImport(importer: Importer): Patch =
    if (config.wildcardThreshold.exists(importer.importees.size >= _))
      Patch.replaceTree(importer, s"${importer.ref.syntax}._")
    else
      Patch.empty
}
