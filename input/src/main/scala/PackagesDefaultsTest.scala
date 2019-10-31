/*
rule = ScalalintPackages
 */
package foo
package bar /* assert: ScalalintPackages
^^^^^^^^^^^

Chained package clauses are disallowed
*/

package aa { /* assert: ScalalintPackages
^^^^^^^^^^^

Chained package clauses are disallowed
*/
}

object PackagesDefaultsTest {}
