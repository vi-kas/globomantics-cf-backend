package com.globomantics

object Demo extends App {

  sealed trait TaskStatus
  case object ToDo extends TaskStatus
  case object InProgress extends TaskStatus
  case object Completed extends TaskStatus

  case class Task(name: String, status: TaskStatus)

  val task = Task("one", ToDo)

  task.status match {
    case ToDo => println("Doing this.")
  }



}
