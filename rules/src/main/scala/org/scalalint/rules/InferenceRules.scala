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

import org.scalalint.configurations.InferenceRulesConfiguration
import org.scalalint.rules.InferenceRules.{ ForbiddenInferredType, PublicInference }
import org.scalalint.{ ConfiguredSemanticRule, Violation }
import org.scalalint.{ RichDefn, RichTerm }

import scala.meta._
import scalafix.v1._

object InferenceRules {
  final case class PublicInference(tree: Tree)
      extends Violation(tree, "Public members must have explicit type annotations")
  final case class ForbiddenInferredType(tree: Tree, types: List[Symbol])
      extends Violation(tree, s"Inferred type contains ${types.map(_.displayName).sorted.mkString(",")}")
}
class InferenceRules(config: InferenceRulesConfiguration)
    extends ConfiguredSemanticRule("ScalalintInference", InferenceRulesConfiguration()) {

  def this() = this(InferenceRulesConfiguration())

  override def configured(ruleConfig: InferenceRulesConfiguration): Rule =
    new InferenceRules(ruleConfig)

  private val matcher =
    SymbolMatcher.normalized(config.forbiddenSymbols: _*)

  override def fix(implicit doc: SemanticDocument): Patch =
    doc.tree.collect {
      case term: Term =>
        checkInferredTypes(term)
      case decl: Defn.Val if decl.pats.forall(_.isNot[Pat.Extract]) =>
        List(checkPublicInference(decl), checkInferredTypes(decl)).asPatch
      case decl: Defn.Var =>
        List(checkPublicInference(decl), checkInferredTypes(decl)).asPatch
      case decl: Defn.Def =>
        List(checkPublicInference(decl), checkInferredTypes(decl)).asPatch
      case decl: Defn.Macro =>
        List(checkPublicInference(decl), checkInferredTypes(decl)).asPatch
    }.asPatch

  private def checkPublicInference(tree: Tree)(implicit doc: SemanticDocument): Patch =
    if (config.noPublicInference && extractDeclaredType(tree).isEmpty && tree.symbol.info.exists(_.isPublic))
      Patch.lint(PublicInference(tree))
    else
      Patch.empty

  private def checkInferredTypes(tree: Tree)(implicit doc: SemanticDocument): Patch =
    if (config.forbiddenSymbols.nonEmpty) {
      val forbiddenSymbols = extractSymbols(tree).filter(matcher.matches)
      if (forbiddenSymbols.nonEmpty) Patch.lint(ForbiddenInferredType(tree, forbiddenSymbols)) else Patch.empty
    } else Patch.empty

  private def extractDeclaredType(tree: Tree): Option[Type] =
    tree match {
      case decl: Defn.Val   => decl.decltpe
      case decl: Defn.Var   => decl.decltpe
      case decl: Defn.Def   => decl.decltpe
      case decl: Defn.Macro => decl.decltpe
      case _                => None
    }

  private def extractSymbols(tree: Tree)(implicit doc: SemanticDocument): List[Symbol] =
    tree match {
      case term: Term => term.inferredTypes
      case defn: Defn => defn.inferredTypes
    }

}
