package com.globomantics.persistance

import slick.jdbc.JdbcProfile

trait DatabaseService {

  val dbConfig: String
  val driver: JdbcProfile

  def connection =
    driver.api.Database.forConfig(dbConfig)
}