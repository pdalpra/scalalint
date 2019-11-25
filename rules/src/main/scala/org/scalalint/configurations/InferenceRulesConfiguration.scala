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

package org.scalalint.configurations

import scala.collection.mutable

import metaconfig.ConfDecoder
import metaconfig.generic._

object InferenceRulesConfiguration {
  implicit val surface: Surface[InferenceRulesConfiguration]     = deriveSurface[InferenceRulesConfiguration]
  implicit val decoder: ConfDecoder[InferenceRulesConfiguration] = deriveDecoder(InferenceRulesConfiguration())

  private val AnySymbol          = "scala.Any"
  private val AnyValSymbol       = "scala.AnyVal"
  private val AnyRefSymbol       = "scala.AnyRef"
  private val ObjectSymbol       = "java.lang.Object"
  private val NothingSymbol      = "scala.Nothing"
  private val ProductSymbol      = "scala.Product"
  private val SerializableSymbol = "scala.Serializable"
}
final case class InferenceRulesConfiguration(
    noPublicInference: Boolean = false,
    noInferAny: Boolean = true,
    noInferAnyVal: Boolean = true,
    noInferAnyRef: Boolean = true,
    noInferObject: Boolean = true,
    noInferNothing: Boolean = false,
    noInferProduct: Boolean = true,
    noInferSerializable: Boolean = true,
    noInfer: List[String] = Nil
) {
  import InferenceRulesConfiguration._

  val forbiddenSymbols: List[String] = {
    val builder = mutable.ListBuffer.empty[String]

    if (noInferAny) builder += AnySymbol
    if (noInferAnyVal) builder += AnyValSymbol
    if (noInferAnyRef) builder += AnyRefSymbol
    if (noInferObject) builder += ObjectSymbol
    if (noInferNothing) builder += NothingSymbol
    if (noInferProduct) builder += ProductSymbol
    if (noInferSerializable) builder += SerializableSymbol
    builder ++= noInfer

    builder.toList
  }
}
