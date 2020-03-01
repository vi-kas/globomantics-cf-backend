package com.globomantics.persistance

import java.util.UUID

import com.globomantics.persistance.Model._

import scala.concurrent.Future

trait Dao[T <: Entity] {

  def insert(entity: T): Future[T]

  def byId(id: UUID): Future[Option[T]]

  def all: Future[Seq[T]]

  def update(id: UUID, entity: T): Future[Int]

  def remove(id: UUID): Future[Boolean]
}