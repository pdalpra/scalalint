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

import scala.meta._
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

  implicit class RichDefn(private val defn: Defn) extends AnyVal {
    def inferredTypes(implicit doc: SemanticDocument): List[Symbol] = {
      val returnType = defn.symbol.info.map(_.signature).collect { case sig: MethodSignature => sig.returnType }
      returnType.toList.flatMap(inferTypes)
    }

    private def inferTypes(semanticType: SemanticType): List[Symbol] = {
      @tailrec
      def helper(symbols: List[Symbol], types: List[SemanticType]): List[Symbol] =
        types match {
          case Nil => symbols
          case tpe :: tail =>
            val (newSymbols, newTypes) = extractTypes(tpe)
            helper(newSymbols ::: symbols, newTypes.filterNot(types.contains) ::: tail)
        }

      val (initSymbols, initTypes) = extractTypes(semanticType)
      helper(initSymbols, initTypes)
    }

    private def extractTypes(semanticType: SemanticType): (List[Symbol], List[SemanticType]) =
      semanticType match {
        case TypeRef(prefix, symbol, typeArgs) => (List(symbol), prefix :: typeArgs)
        case SingleType(prefix, symbol)        => (List(symbol), List(prefix))
        case ThisType(symbol)                  => (List(symbol), Nil)
        case SuperType(prefix, symbol)         => (List(symbol), List(prefix))
        case IntersectionType(types)           => (Nil, types)
        case UnionType(types)                  => (Nil, types)
        case WithType(types)                   => (Nil, types)
        case StructuralType(tpe, decls)        => (decls.map(_.symbol), List(tpe))
        case AnnotatedType(_, tpe)             => (Nil, List(tpe))
        case ExistentialType(tpe, decls)       => (decls.map(_.symbol), List(tpe))
        case UniversalType(tparams, tpe)       => (tparams.map(_.symbol), List(tpe))
        case ByNameType(tpe)                   => (Nil, List(tpe))
        case RepeatedType(tpe)                 => (Nil, List(tpe))
        case _                                 => (Nil, Nil)
      }
  }

  implicit class RichTerm(private val term: Term) extends AnyVal {
    def inferredTypes(implicit doc: SemanticDocument): List[Symbol] =
      term.synthetics.flatMap(_.symbol)
  }
}
