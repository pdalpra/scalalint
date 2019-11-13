/*
rule = ScalalintClasses
 */


sealed trait Trait
sealed trait Lower extends Trait
case class Foo(i: Int) /* assert: ScalalintClasses
^^^^^^^^^^^^^^^^^^^^^^
Case classes must be final */

sealed abstract class A
sealed class X

class Quz extends Trait /* assert: ScalalintClasses
^^^^^^^^^^^^^^^^^^^^^^^
Leaking sealed trait: Quz extends a sealed trait/class, but isn't sealed itself
*/
