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

import org.scalalint.configurations.ClassRulesConfiguration
import org.scalalint.{ ConfiguredSemanticRule, RichSymbol, Violation }

import scala.meta._
import scalafix.v1._

object ClassRules {
  type ClassOrTrait = Defn with Member.Type

  final case class NonFinalCaseClass(defn: ClassOrTrait) extends Violation(defn, "Case classes must be final")
  final case class LeakingSealed(defn: ClassOrTrait)
      extends Violation(
        defn,
        s"Leaking sealed trait: ${defn.name} extends a sealed trait/class, but isn't sealed itself"
      )
}

class ClassRules(config: ClassRulesConfiguration)
    extends ConfiguredSemanticRule("ScalalintClasses", ClassRulesConfiguration()) {
  import ClassRules._

  def this() = this(ClassRulesConfiguration())

  override def configured(ruleConfig: ClassRulesConfiguration): Rule =
    new ClassRules(ruleConfig)

  override def fix(implicit doc: SemanticDocument): Patch = {
    val sealedDefs = doc.tree.collect { case defn: Defn if defn.symbol.info.exists(_.isSealed) => defn.symbol }.toSet

    doc.tree.collect {
      case defn: ClassOrTrait =>
        List(
          checkNonFinalCaseClass(defn),
          checkLeakingSealed(defn, sealedDefs)
        ).asPatch
    }.asPatch
  }

  private def checkNonFinalCaseClass(defn: ClassOrTrait)(implicit doc: SemanticDocument): Patch =
    if (config.finalCaseClass && defn.symbol.info.exists(info => info.isCase && !info.isFinal))
      Patch.lint(NonFinalCaseClass(defn))
    else
      Patch.empty

  private def checkLeakingSealed(defn: ClassOrTrait, sealedDefs: Set[Symbol])(implicit doc: SemanticDocument): Patch =
    if (config.leakingSealed && defn.symbol.info.exists(info => !(info.isFinal || info.isSealed)) && defn.symbol.classHierarchy
          .intersect(sealedDefs)
          .nonEmpty)
      Patch.lint(LeakingSealed(defn))
    else
      Patch.empty

}
