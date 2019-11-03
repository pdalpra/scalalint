/*
rule = ScalalintImports
 */
import sun.misc /* assert: ScalalintImports
       ^^^^^^^^
Relative imports are disallowed
*/

import misc.Unsafe /* assert: ScalalintImports
       ^^^^^^^^^^^
Relative imports are disallowed
*/

import scala.concurrent.duration._

import scala.util.{ Try, Success, Failure }

object ImportsDefaultsTest {
  val foo = Unsafe.ADDRESS_SIZE
}
