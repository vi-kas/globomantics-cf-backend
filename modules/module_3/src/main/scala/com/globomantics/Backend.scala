package com.globomantics

import akka.actor.ActorSystem
import akka.event._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.globomantics.persistance.Model._
import com.globomantics.persistance._
import com.globomantics.routes._
import com.globomantics.services.{ServiceClient, UserServiceComponent}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class Backend private(config: Config, serviceRoutes: Route)
                     (implicit system: ActorSystem, materializer: Materializer) {

  implicit def executor: ExecutionContextExecutor = system.dispatcher
  val logger: LoggingAdapter = Logging(system, getClass)

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(serviceRoutes, config.getString("http.interface"), config.getInt("http.port"))

  bindingFuture.onComplete {
    case Success(bound) =>
      logger.info(s"Server Started: ${bound.localAddress.getHostString}")
    case Failure(e) =>
      logger.error(s"Server could not start: ${e.getMessage}")
      system.terminate()
  }
}

object Backend extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()

  val config: Config = ConfigFactory.load()

  object userRoutes
    extends UserRoutes
      with UserServiceComponent
      with UserDaoComponent
      with PostgresService {

    override val serviceClient: ServiceClient[User] = new UserServiceClient
    override val userDao: UserDao = new UserDao

    scala.util.Try(userDao.initSchema())
  }

  trait PostgresService extends DatabaseService {

    val dbConfig: String = "conferencedb"
    val driver = slick.jdbc.PostgresProfile
  }

  val serviceRoutes = userRoutes.routes

  new Backend(config, serviceRoutes)
}