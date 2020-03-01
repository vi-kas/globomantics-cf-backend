package com.globomantics.persistance

import slick.jdbc.JdbcProfile

trait DatabaseService {

  val dbConfig: String
  val driver: JdbcProfile

  lazy val connection =
    driver.api.Database.forConfig(dbConfig)
}

trait PostgresService extends DatabaseService {

  val dbConfig: String = "conferencedb"
  val driver = slick.jdbc.PostgresProfile
}