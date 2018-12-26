package config

import java.sql.{Connection, DriverManager}

object ConfigManager {

  val driver = "com.mysql.jdbc.Driver"
  val sqlUrl = "localhost"
  val sqlPort = 3306
  val sqlDatabase = "Cinema"

  var sqlUser: String = "test"
  var sqlPassword: String = "test"

  Class.forName(driver)

  var sqlConnectionUrl = s"jdbc:mysql://$sqlUrl:$sqlPort/$sqlDatabase"

  def getSqlConnection: Connection = {
     DriverManager.getConnection(sqlConnectionUrl, sqlUser, sqlPassword)
  }
}