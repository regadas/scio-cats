# scio-cats

![build](https://github.com/regadas/scio-cats/workflows/main/badge.svg)
[![GitHub license](https://img.shields.io/github/license/regadas/scio-cats.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.regadas/scio-cats_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/io.regadas/scio-cats_2.12)

`scio-cats` is a collection of additional functions for [scio](https://github.com/spotify/scio) [SCollection](https://javadoc.io/static/com.spotify/scio-core_2.12/0.8.3/com/spotify/scio/values/SCollection.html) that leverage [cats](https://typelevel.org/cats) type classes and data types.

## Quick Start

To use `scio-cats` add the following dependencies to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "io.regadas" %% "scio-cats" % "<version>"
)
```

### Example

```scala
import cats.implicits._
import io.regadas.scio.cats.syntax._

scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  .mapF(_ + 1) // Some(2), None, Some(3)
  .filterF(_ > 2) // None, None, Some(3)
  .nonEmptyF // Some(3)
```
