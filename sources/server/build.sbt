import play.sbt.routes.RoutesKeys._
import sbt.Keys._

lazy val root = (project in file(".")).
  enablePlugins(PlayScala).
  settings(
    name := "taxi",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      cache,
      ws,
      filters,
      specs2 % Test,
      "com.typesafe.play"           %% "play-slick"             % "1.0.0",
      "com.typesafe.play"           %% "play-slick-evolutions"  % "1.0.0",
      "com.typesafe.slick"          %% "slick-codegen"          % "3.0.0",
      "org.postgresql"              %  "postgresql"             % "9.4-1201-jdbc41",
      "com.mohiva"                  %% "play-silhouette"        % "3.0.0-RC1",
      "net.codingwell"              %% "scala-guice"            % "4.0.0"
    ),
    resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/",
    resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    routesGenerator := InjectedRoutesGenerator
  )


