package utill

import java.sql.{SQLException, Statement}

import config.ConfigManager
import entity.{Movie, Schedule}

import scala.collection.mutable.ArrayBuffer

object DatabaseUtil {
  private val insertMovie = "INSERT INTO Movie (name, genre) VALUE (?, ?)"
  private val insertSchedule = "INSERT INTO Schedule (time, movieId, price, hallId) VALUE (?, ?, ?, ?)"
  private val selectAllScheduleMovie = "SELECT s.movieId, m.name, m.genre, s.time, s.price, s.hallId FROM Schedule as s, Movie as m WHERE s.movieId = m.id"
  private val deleteMovieWithoutSchedule = "DELETE FROM Movie WHERE NOT EXISTS (SELECT movieID FROM Schedule)"
  private val deleteSchedule = "DELETE FROM Schedule WHERE id = ?"
  private val selectScheduleByGenre = "SELECT s.id, s.time, m.name, m.genre, s.time, s.price, s.hallId FROM Schedule as s JOIN Movie as m ON (s.movieId = m.id) WHERE m.genre = ?"
  private val selectMovieById = "SELECT name FROM Movie WHERE Id = ?"

  def createMovie(name: String, genre: String): Option[Int] = {
    try{
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(insertMovie, Statement.RETURN_GENERATED_KEYS)

      statement.setString(1, name)
      statement.setString(2, genre)

      if (statement.executeUpdate() == 0){
        return null
      }

      val res = statement.getGeneratedKeys
      if (!res.next()){
        return null
      }

      val id = res.getInt(1)

      statement.close()
      connection.close()

      Some(id)
    }catch {
      case exception: Throwable =>
        throw new SQLException(exception)
    }
  }

  def createSchedule(time: String, movieId: Int, price: Double, hallId: Int): Option[Int] = {
    try{
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(insertSchedule, Statement.RETURN_GENERATED_KEYS)

      statement.setString(1, time)
      statement.setInt(2, movieId)
      statement.setDouble(3, price)
      statement.setInt(4, hallId)

      if (statement.executeUpdate() == 0){
        return null
      }
      val res = statement.getGeneratedKeys
      if (!res.next()){
        return null
      }

      val id =res.getInt(1)

      statement.close()
      connection.close()

      Some(id)
    }catch{
      case exception: Throwable =>
        throw new SQLException(exception)
    }
  }

  def getAllScheduleMovie: ArrayBuffer[String] = {
    try {
      val connection = ConfigManager.getSqlConnection
      val statement = connection.createStatement()

      val scheduleMovie: ArrayBuffer[String] = ArrayBuffer[String]()
      val resultSet = statement.executeQuery(selectAllScheduleMovie)

      while (resultSet.next()){
        val movieId = resultSet.getInt("movieId")
        val name = resultSet.getString("name")
        val genre = resultSet.getString("genre")
        val time = resultSet.getString("time")
        val price = resultSet.getDouble("price")
        val hall = resultSet.getInt("hallId")

        scheduleMovie.append(s"$movieId, $name, $genre, $time, $price, $hall")
      }
      connection.close()
      statement.close()

      scheduleMovie
    }catch {
      case exception: Throwable =>
        throw new SQLException(exception)
    }
  }

  val delMovieWithoutSchedule: Boolean = {
    try {
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(deleteMovieWithoutSchedule)

      statement.execute()

      statement.close()
      connection.close()

      true
    } catch {
      case exception: Throwable =>
        throw new SQLException(exception)
    }
  }

  def delSchedule(id: Int): Boolean = {
    try {
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(deleteSchedule)

      statement.setInt(1, id)
      statement.execute()

      connection.close()
      statement.close()

      true
    } catch {
      case exception: Throwable =>
        throw new SQLException(exception)
    }
  }

  def getScheduleByGenre(genre: String): ArrayBuffer[Schedule] = {
    try{
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(selectScheduleByGenre)

      val scheduleList = ArrayBuffer[Schedule]()

      statement.setString(1, genre)
      val res = statement.executeQuery()

      while (res.next()){
        val id = res.getInt("id")
        val time = res.getString("time")
        val movieId = res.getInt("movieId")
        val price = res.getDouble("price")
        val hallId = res.getInt("hallId")

        scheduleList.append(Schedule(id, time, movieId, price, hallId))
      }
      connection.close()
      statement.close()

      scheduleList
    } catch {
      case ex: Throwable =>
        throw new SQLException(ex)
    }
  }

  def getMovieById(id: Int): ArrayBuffer[Movie] = {
    try {
      val connection = ConfigManager.getSqlConnection
      val statement = connection.prepareStatement(selectMovieById)

      val movieList = ArrayBuffer[Movie]()
      statement.setInt(1, id)
      val res = statement.executeQuery()

      while (res.next()){
        val id = res.getInt("id")
        val name = res.getString("name")
        val genre = res.getString("genre")

        movieList.append(Movie(id, name, genre))
      }

      connection.close()
      statement.close()

      movieList
    } catch {
      case ex: Throwable =>
        throw new SQLException(ex)
    }
  }
}
