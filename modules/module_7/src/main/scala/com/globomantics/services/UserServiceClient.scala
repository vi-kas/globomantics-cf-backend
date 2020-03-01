package com.globomantics.services

import java.util.UUID

import com.globomantics.persistance.{Dao, UserDaoComponent}
import com.globomantics.persistance.Model.User

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceClient extends ServiceClient[User] {
  this: UserDaoComponent =>

  override def create(user: User): Future[ServiceResponse[User]] = {

    val result: Future[User] = for {
      userWithAddressInsured <- user.ensureAddressDetails
      userInserted <- userDao.insert(userWithAddressInsured)
    } yield {
      userInserted
    }

    result
      .map(Right(_))
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }
  }

  override def read(id: UUID): Future[ServiceResponse[User]] = {
    userDao
      .byId(id)
      .map {
        case None => Left(ErrorResponse(s"Could not read User with Id: $id", 0))
        case Some(user) => Right(user)
      }
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }
  }

  override def update(id: UUID, entity: User): Future[ServiceResponse[Int]] = {
    userDao
      .update(id, entity)
      .map {
        case 1 => Right(1)
        case _ => Left(ErrorResponse("Could not update User with given ID", 0))
      }
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }
  }

  override def delete(id: UUID): Future[ServiceResponse[Boolean]] = {
    userDao
      .remove(id)
      .map {
        case true => Right(true)
        case false => Left(ErrorResponse("Could not delete User with Given ID", 0))
      }
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }
  }

}
