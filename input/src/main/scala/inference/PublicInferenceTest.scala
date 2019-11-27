/*
rule = ScalalintInference
ScalalintInference.noPublicInference = true
ScalalintInference.noInferNothing  = false
*/
package inference
import scala.language.experimental.macros

object PublicInferenceTest {

  val foo = 1 /* assert: ScalalintInference
  ^^^^^^^^^^^
Public members must have explicit type annotations */
  var bar = "test" /* assert: ScalalintInference
  ^^^^^^^^^^^^^^^^
Public members must have explicit type annotations */
  def quz(i: Int) = i + 1 /* assert: ScalalintInference
  ^^^^^^^^^^^^^^^
Public members must have explicit type annotations */
  def mac = macro ??? /* assert: ScalalintInference
  ^^^^^^^^^^^^^^^
Public members must have explicit type annotations */

  val fooTyped: Int = 1
  var barTyped: String = "test"
  def quzTyped(i: Int): Int = i + 1
  def macTyped: Int = macro ???
}
