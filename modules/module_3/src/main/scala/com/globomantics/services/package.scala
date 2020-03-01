package com.globomantics

import java.util.UUID

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import com.globomantics.persistance.Model.User
import com.globomantics.util.ZIPCodeLocator
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

package object services {

  /**
    * Method to create an entity for CRUD Service
    */
  def create[T](entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.create(entity)

  /**
    * Method to read an entity for CRUD Service
    */
  def read[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.read(id)

  /**
    * Method to update an entity for CRUD Service
    */
  def update[T](id: UUID, entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[Int]] =
    sc.update(id, entity)

  /**
    * Method to delete an entity for CRUD Service
    */
  def delete[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[Boolean]] =
    sc.delete(id)




  /**
    * Response type alias for typical Service Response
    */
  type ServiceResponse[T] = Either[ErrorResponse, T]

  case class ErrorResponse(message: String, code: Int)

  case class ApiResponse(success: Boolean,
                         code: Option[Int] = None,
                         message: Option[String] = None,
                         data: JsValue = JsString(""))

  def respondWith[A](response: Future[ServiceResponse[A]])
                    (implicit ee: JsonWriter[ErrorResponse],
                     ar: JsonWriter[ApiResponse], rr: JsonWriter[A]): StandardRoute =
    complete {
      response map {
        case Left(error) =>
          HttpResponse(
            status = toStatusCode(error.code),
            entity = HttpEntity(ContentTypes.`application/json`, ApiResponse(false, Some(error.code), Some(error.message)).toJson.toString))
        case Right(value) =>
          HttpResponse(
            status = StatusCodes.Created,
            entity = HttpEntity(ContentTypes.`application/json`, ApiResponse(true, data = value.toJson).toJson.toString))
      }
    }

  private def toStatusCode(i: Int): StatusCode =
    Try(StatusCode.int2StatusCode(i)).getOrElse(StatusCodes.InternalServerError)
}