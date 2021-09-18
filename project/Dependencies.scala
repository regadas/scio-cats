import sbt._

object Dependencies {
  val ScalaTestVersion = "3.2.10"
  val CatsVersion = "2.6.1"
  val KindProjectorVersion = "0.13.0"
  val ScioVersion = "0.10.4"

  lazy val ScalaTest = "org.scalatest" %% "scalatest" % ScalaTestVersion
  lazy val CatsCore = "org.typelevel" %% "cats-core" % CatsVersion
  lazy val ScioCore = "com.spotify" %% "scio-core" % ScioVersion
  lazy val ScioTest = "com.spotify" %% "scio-test" % ScioVersion
  lazy val KindProjector = compilerPlugin(
    "org.typelevel" % "kind-projector" % KindProjectorVersion cross CrossVersion.full
  )
}
