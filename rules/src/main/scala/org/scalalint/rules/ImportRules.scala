package org.scalalint.rules

import org.scalalint.configurations.ImportRulesConfiguration
import org.scalalint.{ ConfiguredSemanticRule, Violation }

import scala.meta._
import scalafix.v1._

object ImportRules {
  final case class Blacklisted(tree: Importer)    extends Violation(tree, s"Importing '$tree' is disallowed")
  final case class BlockImport(tree: Importer)    extends Violation(tree, "Block imports are disallowed")
  final case class WildcardImport(tree: Importer) extends Violation(tree, "Wildcard imports are disallowed")
  final case class RelativeImport(tree: Importer) extends Violation(tree, s"Relative imports are disallowed")
}
class ImportRules(config: ImportRulesConfiguration)
    extends ConfiguredSemanticRule("ScalalintImports", ImportRulesConfiguration()) {
  import ImportRules._

  def this() = this(ImportRulesConfiguration())

  override def configured(config: ImportRulesConfiguration): ImportRules =
    new ImportRules(config)

  override def fix(implicit doc: SemanticDocument): Patch = {
    val importedPackages = doc.tree.collect { case importer: Importer => importer.ref.syntax }

    doc.tree.collect {
      case importer: Importer =>
        List(
          checkBlockImport(importer),
          checkForbiddenImport(importer),
          checkWildcardImport(importer),
          checkRelativeImport(importer, importedPackages),
          rewriteToWildcardImport(importer)
        ).asPatch
    }.asPatch
  }

  private def checkForbiddenImport(importer: Importer): Patch =
    if (config.forbiddenImports.contains(importer.syntax))
      Patch.lint(Blacklisted(importer))
    else
      Patch.empty

  private def checkWildcardImport(importer: Importer): Patch =
    if (config.disableWildcardImports)
      importer.importees.collect { case _: Importee.Wildcard => Patch.lint(WildcardImport(importer)) }.asPatch
    else Patch.empty

  private def checkBlockImport(importer: Importer): Patch =
    // Count only imported names: unimports and renames require block imports
    if (config.disableBlockImports && importer.importees.collect { case i: Importee.Name => i }.size > 1)
      Patch.lint(BlockImport(importer))
    else
      Patch.empty

  private def checkRelativeImport(importer: Importer, importedPackages: List[String])(
      implicit doc: SemanticDocument
  ): Patch =
    (for {
      importee   <- importer.importees
      symbol     <- importee.symbol.asNonEmpty.toList
      symbolInfo <- symbol.info
    } yield {
      val importedPackage = if (symbolInfo.isPackage) symbol else symbol.owner
      val packageName     = importedPackage.normalized.value.init // drop the final '.' from normalized package symbol
      if (!importedPackages.contains(packageName))
        Patch.lint(RelativeImport(importer))
      else
        Patch.empty
    }).asPatch

  private def rewriteToWildcardImport(importer: Importer): Patch =
    if (config.rewriteWildcardThreshold.exists(importer.importees.size >= _))
      Patch.replaceTree(importer, s"${importer.ref.syntax}._")
    else
      Patch.empty
}
