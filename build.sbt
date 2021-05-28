import Dependencies._

ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "io.regadas"
ThisBuild / organizationName := "regadas"
ThisBuild / licenses := Seq(
  "APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / homepage := Some(url("https://github.com/regadas/scio-cats"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/regadas/scio-cats"),
    "scm:git@github.com:regadas/scio-cats.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "regadas",
    name = "Filipe Regadas",
    email = "filiperegadas@gmail.com",
    url = url("https://twitter.com/regadas")
  )
)
ThisBuild / publishMavenStyle := true
ThisBuild / pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray)
ThisBuild / credentials += (for {
  username <- sys.env.get("SONATYPE_USERNAME")
  password <- sys.env.get("SONATYPE_PASSWORD")
} yield Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  username,
  password
))
ThisBuild / dynverSonatypeSnapshots := true
ThisBuild / publishTo := sonatypePublishToBundle.value

lazy val `scio-cats` = project
  .in(file("."))
  .settings(
    name := "scio-cats",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-explaintypes",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Xfatal-warnings",
      "-deprecation",
      "-Xlint"
    ) ++ {
      if (scalaVersion.value.startsWith("2.12")) {
        Seq("-Ypartial-unification")
      } else {
        Seq()
      }
    },
    crossScalaVersions := Seq("2.12.14", scalaVersion.value),
    libraryDependencies ++= Seq(
      CatsCore,
      KindProjector,
      ScioCore % Provided,
      ScioTest % Test,
      ScalaTest % Test
    ),
    git.remoteRepo := "git@github.com:regadas/scio-cats.git"
  )
  .enablePlugins(
    SiteScaladocPlugin,
    GhpagesPlugin
  )
