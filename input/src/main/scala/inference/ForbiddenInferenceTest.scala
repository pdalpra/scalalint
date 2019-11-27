/*
rule = ScalalintInference
*/
package inference

import scala.language.experimental.macros

object ForbiddenInferenceTest {
  sealed trait A
  case class B() extends A
  case class C() extends A

  def foo = List(1,"a") /* assert: ScalalintInference
  ^^^^^^^
Inferred type contains Any */

  val a = List("", Nil) /* assert: ScalalintInference
  ^^^^^^^
Inferred type contains Object */

  var x = List(B(), C()) /* assert: ScalalintInference
  ^^^^^^^
Inferred type contains Product,Serializable */

  val y = List(1, true) /* assert: ScalalintInference
  ^^^^^^^
Inferred type contains AnyVal */

  private val xxx = Left("x")
}
