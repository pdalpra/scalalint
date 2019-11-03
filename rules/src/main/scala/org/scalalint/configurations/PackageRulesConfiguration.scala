package org.scalalint.configurations

import metaconfig.ConfDecoder
import metaconfig.generic._

object PackageRulesConfiguration {
  implicit val surface: Surface[PackageRulesConfiguration]     = deriveSurface[PackageRulesConfiguration]
  implicit val decoder: ConfDecoder[PackageRulesConfiguration] = deriveDecoder(PackageRulesConfiguration())
}
final case class PackageRulesConfiguration(
    disableRelativePackages: Boolean = true
)
