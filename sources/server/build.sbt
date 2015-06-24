import java.nio.file.{Paths, Files}
import play.sbt.routes.RoutesKeys._
import sbt.Keys._

def getConfigName(testMode: Boolean): String = {
  val suffix = "override."
  val mode = if (testMode) "test." else ""

  val searchList = Seq(
    s"application.$mode${suffix}conf",
    s"application.${mode}conf"
  ) map { name => s"conf/$name" }

  val confFile = searchList.find(path => Files.exists(Paths.get(path))).getOrElse("conf/application.conf")
  println("Loading config from " + confFile)
  confFile
}

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
    routesGenerator := InjectedRoutesGenerator,

    fork := true,
    javaOptions += "-Dconfig.file=" + getConfigName(false),
    javaOptions in Test += "-Dconfig.file=" + getConfigName(true)
  )


