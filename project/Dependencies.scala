import sbt._

object Dependencies {
  val ScalaTestVersion = "3.2.9"
  val KindProjectorVersion = "0.11.3"
  val CatsVersion = "2.4.2"
  val ScioVersion = "0.10.2"

  lazy val ScalaTest = "org.scalatest" %% "scalatest" % ScalaTestVersion
  lazy val CatsCore = "org.typelevel" %% "cats-core" % CatsVersion
  lazy val ScioCore = "com.spotify" %% "scio-core" % ScioVersion
  lazy val ScioTest = "com.spotify" %% "scio-test" % ScioVersion
  lazy val KindProjector = compilerPlugin(
    "org.typelevel" % "kind-projector" % KindProjectorVersion cross CrossVersion.full
  )
}
