/*
rule = ScalalintImports
ScalalintImports.disableBlockImports = true
Scalalintimports.disableRelativeImports = true
ScalalintImports.disableWildcardImports = true
ScalalintImports.forbiddenImports = ["sun.misc.Unsafe"]
 */

import java.util /* assert: ScalalintImports
       ^^^^^^^^
Relative imports are disallowed
*/

import util.ArrayList /* assert: ScalalintImports
       ^^^^^^^^^^^
Relative imports are disallowed
*/

import sun.misc.Unsafe /* assert: ScalalintImports
       ^^^^^^^^^^^^^^^
Importing 'sun.misc.Unsafe' is disallowed
*/

import scala.concurrent.duration._ /* assert: ScalalintImports
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^
Wildcard imports are disallowed
*/

import scala.util.{ Try, Success } /* assert: ScalalintImports
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^
Block imports are disallowed
*/

import scala.util.{ Try => _, Failure => Fail }

object ForbidImportTest {

}
