package com.globomantics

object MemoizedLearning/* extends App */{

  import com.github.t3hnar.bcrypt._

  def memo[X, Y](f: X => Y): X => Y = {

    val cache = scala.collection.mutable.Map[X,Y]()
    (x: X) => cache.getOrElseUpdate(x, f(x))
  }

  val simpleBcrypt: String => String = pwd => pwd.bcrypt

  val memoizedBcrypt: String => String = memo(pwd => pwd.bcrypt)

  def timed[T](code: => T) = {
    val start = System.nanoTime()
    val result = code
    val end = System.nanoTime()

    (result, (end - start)/100000)
  }

  val time1 = timed {
    memoizedBcrypt("foo")
  }

  val time2 = timed {
    memoizedBcrypt("foo")
  }

  println(time1)
  println(time2)
}