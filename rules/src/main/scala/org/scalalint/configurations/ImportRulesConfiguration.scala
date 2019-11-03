package org.scalalint.configurations

import metaconfig.ConfDecoder
import metaconfig.generic._

object ImportRulesConfiguration {
  implicit val surface: Surface[ImportRulesConfiguration]     = deriveSurface[ImportRulesConfiguration]
  implicit val decoder: ConfDecoder[ImportRulesConfiguration] = deriveDecoder(ImportRulesConfiguration())
}
final case class ImportRulesConfiguration(
    disableBlockImports: Boolean = false,
    disableRelativeImports: Boolean = false,
    disableWildcardImports: Boolean = false,
    forbiddenImports: List[String] = Nil,
    rewriteWildcardThreshold: Option[Int] = None
)
