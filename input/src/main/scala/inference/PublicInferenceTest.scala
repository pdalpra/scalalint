/*
rule = ScalalintInference
ScalalintInference.noPublicInference = true
ScalalintInference.noInferNothing  = false
*/
package inference
import scala.language.experimental.macros
import scala.util.matching.Regex

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

  val aRegex: Regex = "^(.*)-some-string-(.*)$".r
  val anotherRegex: Regex = "^(.*)-some-string-(.*)$".r
  val e: (String, Int) = ("", 1)

  def aMethod(aString: String): String = {
    val aRegex(left, right) = aString
    val (a, b) = e
    s"${left}_${right}"
  }
}
