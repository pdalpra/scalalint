package com.pdalpra.scalalint.configurations

import metaconfig.ConfDecoder
import metaconfig.generic._

object ImportRulesConfiguration {
  implicit val surface: Surface[ImportRulesConfiguration]     = deriveSurface[ImportRulesConfiguration]
  implicit val decoder: ConfDecoder[ImportRulesConfiguration] = deriveDecoder(ImportRulesConfiguration())
}
final case class ImportRulesConfiguration(
    disableRelativeImports: Boolean = true,
    disableWildcardImports: Boolean = false,
    disableGroupImports: Boolean = false,
    forbiddenImports: List[String] = Nil,
    wildcardThreshold: Option[Int] = None
)
