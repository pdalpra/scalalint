/*
 * Copyright 2019 Pierre Dal-Pra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scalalint.configurations

import metaconfig.ConfDecoder
import metaconfig.generic._

object ImportRulesConfiguration {
  implicit val surface: Surface[ImportRulesConfiguration]     = deriveSurface[ImportRulesConfiguration]
  implicit val decoder: ConfDecoder[ImportRulesConfiguration] = deriveDecoder(ImportRulesConfiguration())
}
final case class ImportRulesConfiguration(
    disableBlockImports: Boolean = false,
    disableRelativeImports: Boolean = true,
    disableWildcardImports: Boolean = false,
    forbiddenImports: List[String] = Nil,
    rewriteWildcardThreshold: Option[Int] = None
)
