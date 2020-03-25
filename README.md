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

### Examples

These are just few examples that show some of nicities of the added functions. Most of these functions have scaladoc with an example. Have a [look](https://gh.regadas.io/scio-cats/latest/api/io/regadas/scio/cats/syntax/SCollectionOps.html).

#### Maping and filtering of `F[A]`

```scala
// before

scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  .map(_.map(_ + 1)) // Some(2), None, Some(3)
  .filter(_.exists(_ > 2)) // Some(3)

// after

import cats.implicits._
import io.regadas.scio.cats.syntax._

scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  .map_(_ + 1) // Some(2), None, Some(3)
  .filter_(_ > 2) // Some(3)
```

#### Creating tuples over `F[A]`

```scala
// before
scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  // here you could also use `cats` but it doesn't look as nice
  // .map(_.tupleRight(1))
  .map(_.map(_ -> 1)) // Some((1, 1)), Some((2, 1))

// after

import cats.implicits._
import io.regadas.scio.cats.syntax._

scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  // tupleLeft is also available
  .tupleRight(1) // Some((1, 1)), Some((2, 1))
```

#### Output to the console (similar to `debug`)

```scala
// before
scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  .debug() // it will use toString()

// after

import cats.implicits._
import io.regadas.scio.cats.syntax._

scioContext
  .parallelize(Seq(Some(1), None, Some(2)))
  .showStdOut // leverages Show type class
```
