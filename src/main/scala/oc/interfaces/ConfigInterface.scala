package oc.interfaces

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigInterface {

  lazy val config: Config = ConfigFactory.load()

}
