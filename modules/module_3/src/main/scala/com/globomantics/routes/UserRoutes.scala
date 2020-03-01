package com.globomantics.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.globomantics.persistance.Model._
import com.globomantics.services._

trait UserRoutes {
  self: UserServiceComponent =>

  val routes =
    pathPrefix("api" / "v1" / "users"){

      pathEndOrSingleSlash {
        (post & entity(as[User])) { user =>

//          val createResult = create(user)
//          respondWith(createResult)
          complete(StatusCodes.Created)
        }
      }
    }
}