/*
rule = ScalalintImports
ScalalintImports.ensureImportsAreGrouped = true
 */
package imports

import java.util.ArrayList

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Future

object GroupedImportsTest {
  import java.io.OutputStream /* assert: ScalalintImports
  ^^^^^^^^^^^^^^^^^^^^^^^^^^^
Imports must be grouped together
*/

  def foo = {
    import scala.util.Properties /* assert: ScalalintImports
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Imports must be grouped together
*/
  }
}
