package config

import java.io.File
import java.sql.{Connection, DriverManager}
import java.util.Properties

import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.builder.fluent.Configurations

object ConfigManager {
  var sqlConnectionUrl: String = _
  var sqlUser: String = _
  var sqlPassword: String = _

  private val dataSinkProperties = new Properties()

  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")

  def configure(propertyFileLocation: String): Unit = {

    var configurations = new Configurations
    var fileProperties: File = new File(propertyFileLocation)
    var config: Configuration = configurations.properties(fileProperties)

    sqlUser = config.getString("sql.user")
    sqlPassword = config.getString("sql.password")

    var sqlUrl = config.getString("sql.server")
    var sqlDatabase = config.getString("sql.database")
    var sqlPort = config.getShort("sql.port")

    sqlConnectionUrl = s"jdbc:sqlserver://$sqlUrl:$sqlPort/$sqlDatabase;" //+ "databaseName=;user=$sqlUser;password=$sqlPassword;"
  }

  def getSqlConnection: Connection = {
    dataSinkProperties.put("user", sqlUser)
    dataSinkProperties.put("password", sqlPassword)
    dataSinkProperties.put("useSSL", "false")

    DriverManager.getConnection (sqlConnectionUrl, dataSinkProperties);
  }
}