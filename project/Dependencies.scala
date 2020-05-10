import sbt._

object Dependencies {
  val ScalaTestVersion = "3.1.2"
  val KindProjectorVersion = "0.11.0"
  val CatsVersion = "2.1.1"
  val ScioVersion = "0.9.0"

  lazy val ScalaTest = "org.scalatest" %% "scalatest" % ScalaTestVersion
  lazy val CatsCore = "org.typelevel" %% "cats-core" % CatsVersion
  lazy val ScioCore = "com.spotify" %% "scio-core" % ScioVersion
  lazy val ScioTest = "com.spotify" %% "scio-test" % ScioVersion
  lazy val KindProjector = compilerPlugin(
    "org.typelevel" % "kind-projector" % KindProjectorVersion cross CrossVersion.full
  )
}
