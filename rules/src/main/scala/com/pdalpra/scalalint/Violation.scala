package com.pdalpra.scalalint

import scala.meta._
import scalafix.v1._

abstract class Violation[T <: Tree](tree: T, msg: String) extends Diagnostic {
  override def position: Position = tree.pos
  override def message: String    = msg
}
