/*
 * Copyright 2019 Pierre Dal-Pra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
