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

package org

import scala.annotation.tailrec

import scalafix.v1._

package object scalalint {

  implicit class RichSymbol(private val symbol: Symbol) extends AnyVal {

    def classHierarchy(implicit doc: SemanticDocument): Set[Symbol] = {
      @tailrec
      def helper(todo: List[Symbol], hierarchy: Set[Symbol]): Set[Symbol] =
        todo match {
          case Nil => hierarchy
          case head :: tl =>
            val parents = getParents(head)
            helper(parents.filterNot(todo.contains) ::: tl, parents.filterNot(hierarchy.contains).toSet ++ hierarchy)
        }

      helper(List(symbol), Set.empty)
    }

    private def getParents(symbol: Symbol)(implicit doc: SemanticDocument): List[Symbol] =
      symbol.info
        .map { symbolInfo =>
          val parents = symbolInfo.signature match {
            case sig: ClassSignature => sig.parents
            case _                   => Nil
          }

          parents.collect { case typeRef: TypeRef => typeRef.symbol }
        }
        .getOrElse(List.empty)
  }
}
