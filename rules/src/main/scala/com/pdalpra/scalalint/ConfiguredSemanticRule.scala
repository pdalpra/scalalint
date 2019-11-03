package com.pdalpra.scalalint

import metaconfig.{ConfDecoder, Configured}
import scalafix.v1._

abstract class ConfiguredSemanticRule[C: ConfDecoder](rule: RuleName, defaultConfig: C) extends SemanticRule(rule) {
  def configured(ruleConfig: C): Rule

  override def withConfiguration(config: Configuration): Configured[Rule] =
    config.conf.getOrElse(name.value)(defaultConfig).map(configured)
}
