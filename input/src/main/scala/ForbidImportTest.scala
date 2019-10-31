/*
rule = ScalalintImports
ScalalintImports.forbiddenImports = ["sun.misc.Unsafe"]
ScalalintImports.disableWildcardImports = true
 */
import sun.misc.Unsafe /* assert: ScalalintImports
       ^^^^^^^^^^^^^^^
Importing 'sun.misc.Unsafe' is disallowed
*/

import scala.concurrent.duration._ /* assert: ScalalintImports
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^
Wildcard imports are disallowed
*/

object ForbidImportTest {

}
