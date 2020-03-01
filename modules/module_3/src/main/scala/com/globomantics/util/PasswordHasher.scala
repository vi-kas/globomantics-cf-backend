package com.globomantics.util

import scalaz.Memo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PasswordHasher {

  import com.github.t3hnar.bcrypt._

  val memoizedBcrypt: String => Future[String] = Memo.immutableHashMapMemo {

    pwd => Future {
      pwd.bcrypt
    }
  }

}
