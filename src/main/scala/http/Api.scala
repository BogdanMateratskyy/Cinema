package http

import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import _root_.entity.{Movie, Schedule}
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization.write
import play.api.libs.json.Json
import util.DatabaseUtil

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

class Api {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit val formats: DefaultFormats.type = DefaultFormats
  var scheduleList = ArrayBuffer[Schedule]()
  var movieList = Set[Movie]()

  def routes: Route = path ("schedule") {
    pathEndOrSingleSlash {
      get {
        complete(write(DatabaseUtil.getSchedule))
      } ~
        post {
          entity(as[HttpEntity]) { entity => {
            val respString: Future[String] = Unmarshal(entity).to[String]
            respString foreach {
              case msg =>
                val json = Json.parse(msg)
                val id = json("id").validate[Int].get
                val time = json("time").validate[String].get
                val movieId = json("movieId").validate[Int].get
                val price = json("price").validate[Double].get
                val hallId = json("hallId").validate[Int].get

                scheduleList.append(Schedule(id, time, movieId, price, hallId))
            }
            complete(StatusCodes.Created)
          }
          }
        }
    }
  } ~
  path ("movie") {
    pathEndOrSingleSlash {
      get {
        complete(write(DatabaseUtil.getMovie))
      } ~
      post {
        entity(as[HttpEntity]) { entity =>
          val respString: Future[String] = Unmarshal(entity).to[String]
          respString foreach {
            case msg =>
              val json = Json.parse(msg)
              val id = json("id").validate[Int].get
              val name = json("name").validate[String].get
              val genre = json("genre").validate[String].get

              movieList+(Movie(id, name, genre))
          }
          complete(StatusCodes.Created)
        }
      }
    } ~
      path("api" / "v1" / "int" / IntNumber) { number =>
        get {
          complete(number.toString)
        }
      }
  }
}
