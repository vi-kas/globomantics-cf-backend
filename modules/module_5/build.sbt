
lazy val databaseDependencies: Seq[ModuleID] = Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
)

lazy val akkaDependencies: Seq[ModuleID] = Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "com.typesafe.akka" %% "akka-http-core" % "10.1.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10"
)

lazy val root = (project in file("."))
  .settings(
    name := "globomantics-be",
    scalaVersion := "2.12.8",
    organization := "com.globomantics",
    description := "Globomantics Platform",
    version := "0.1",
    libraryDependencies ++= Seq(
      "com.github.t3hnar" %% "scala-bcrypt" % "4.1",
      "org.scalaz" %% "scalaz-core" % "7.2.29"
    ) ++ akkaDependencies ++ databaseDependencies
  )