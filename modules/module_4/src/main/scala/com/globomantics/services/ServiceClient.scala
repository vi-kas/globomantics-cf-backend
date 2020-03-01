package com.globomantics.services

import java.util.UUID

import scala.concurrent.Future

trait ServiceClient[T] {

  def create(entity: T): Future[ServiceResponse[T]]

  def read(id: UUID): Future[ServiceResponse[T]]

  def update(id: UUID, entity: T): Future[ServiceResponse[Int]]

  def delete(id: UUID): Future[ServiceResponse[Boolean]]
}