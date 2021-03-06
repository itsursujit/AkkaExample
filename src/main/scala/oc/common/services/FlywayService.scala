package oc.common.services

import oc.interfaces.ConfigInterface
import org.flywaydb.core.Flyway

class FlywayService extends ConfigInterface {

  private val flyway = new Flyway()
  flyway.setDataSource(config.getString("oc.db.url"), config.getString("oc.db.user"), config.getString("oc.db.password"))

  def migrateDatabaseSchema: FlywayService = {
    flyway.migrate()
    this
  }

  def dropDatabase: FlywayService = {
    flyway.clean()
    this
  }

  def initiateDatabase: FlywayService = {
    flyway.baseline()
    this
  }
}
