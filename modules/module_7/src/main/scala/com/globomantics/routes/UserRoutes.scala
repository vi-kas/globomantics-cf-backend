package com.globomantics.routes

import akka.http.scaladsl.server.Directives._
import com.globomantics.persistance.Model._
import com.globomantics.services._

trait UserRoutes {


  def routes(implicit serviceClient: ServiceClient[User]) = {

    pathPrefix("api" / "v1" / "users"){

      pathEndOrSingleSlash {
        (post & entity(as[User])) { user =>

          val createResult = create(user)
          respondWith(createResult)
        }
      } ~
        path(JavaUUID) {id =>
          get {

            val readResult = read[User](id)
            respondWith(readResult)
          }
        }
    }
  }

}
