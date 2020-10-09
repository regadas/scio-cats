package io.regadas.scio.cats.syntax

import cats.syntax.all._
import com.spotify.scio.testing._

import scala.util.Try

class SCollectionSyntaxTest extends PipelineSpec {

  "SCollection" should "support tupleLeft" in {
    runWithContext { sc =>
      val data = Seq(Option(1))
      val expected = Seq(Option((2, 1)))

      val result = sc.parallelize(data).tupleLeft(2)

      result should containInAnyOrder(expected)
    }
  }

  it should "support tupleRight" in {
    runWithContext { sc =>
      val data = Seq(Option(1))
      val expected = Seq(Option((1, 2)))

      val result = sc.parallelize(data).tupleRight(2)

      result should containInAnyOrder(expected)
    }
  }

  it should "support as" in {
    runWithContext { sc =>
      val data = Seq(List(1, 2, 3))
      val expected = Seq(List("foobar", "foobar", "foobar"))

      val result = sc.parallelize(data).as("foobar")

      result should containInAnyOrder(expected)
    }
  }

  it should "support map_" in {
    runWithContext { sc =>
      val data = Seq(Option(1))
      val expected = Seq(Option(2))

      val result = sc.parallelize(data).map_(_ + 1)
      result should containInAnyOrder(expected)

      val listData = Seq(List(1, 2, 3), List(4, 5, 6))
      val expectedList = Seq(List(2, 3, 4), List(5, 6, 7))

      val resultList = sc.parallelize(listData).map_(_ + 1)
      resultList should containInAnyOrder(expectedList)
    }
  }

  it should "support flatMap_" in {
    runWithContext { sc =>
      val m: Map[Int, String] = Map(1 -> "one", 3 -> "three")
      val data = Seq(Option(1), Option(2), None)
      val expected = Seq(Some("one"), None, None)

      val result = sc.parallelize(data).flatMap_(m.get)
      result should containInAnyOrder(expected)
    }
  }

  it should "support mapFilter" in {
    runWithContext { sc =>
      val m: Map[Int, String] = Map(1 -> "one", 3 -> "three")
      val data = Seq(Option(1), Option(2), None)
      val expected = Seq(Some("one"), None, None)

      val result = sc.parallelize(data).mapFilter(m.get)
      result should containInAnyOrder(expected)

      val listData = Seq(List(1, 2, 3), List(4, 5, 6))
      val expectedList = Seq(List("one", "three"), List())

      val resultList = sc.parallelize(listData).mapFilter(m.get)
      resultList should containInAnyOrder(expectedList)
    }
  }

  it should "support ffilter" in {
    runWithContext { sc =>
      val data = Seq(Option(1), Option(2), None)
      val expected = Seq(Option(1), None, None)

      val result = sc.parallelize(data).ffilter(_ <= 1)
      result should containInAnyOrder(expected)

      val listData = Seq(List(1, 2, 3), List(4, 5, 6))
      val expectedList = Seq(List(1), List())

      val resultList = sc.parallelize(listData).ffilter(_ <= 1)
      resultList should containInAnyOrder(expectedList)
    }
  }

  it should "support nonEmpty" in {
    runWithContext { sc =>
      val data = Seq(Option(1), Option(2), None)
      val expected = Seq(Option(1))

      val result = sc.parallelize(data).nonEmpty(_ <= 1)
      result should containInAnyOrder(expected)
    }
  }

  it should "support empty" in {
    runWithContext { sc =>
      val data = Seq(List(1, 2, 3), List(2, 2))
      val expected = Seq(List[Int]())

      val result = sc.parallelize(data).empty(_ <= 1)
      result should containInAnyOrder(expected)
    }
  }

  it should "support collect_" in {
    runWithContext { sc =>
      val data = Seq(List(1, 2, 3, 4))
      val expected = Seq(List.empty[String])

      val result = sc.parallelize(data).collect_ { case 0 =>
        "one"
      }

      result should containInAnyOrder(expected)
    }
  }

  it should "support fproduct" in {
    runWithContext { sc =>
      val data = Seq(Option(1))
      val expected = Seq(Option((1, 2)))

      val result = sc.parallelize(data).fproduct(_ + 1)

      result should containInAnyOrder(expected)
    }
  }

  it should "support mproduct" in {
    runWithContext { sc =>
      val data = Seq(List("12", "34", "56"))
      val expected =
        Seq(
          List(
            ("12", '1'),
            ("12", '2'),
            ("34", '3'),
            ("34", '4'),
            ("56", '5'),
            ("56", '6')
          )
        )

      val result = sc.parallelize(data).mproduct(_.toList)
      result should containInAnyOrder(expected)
    }
  }

  it should "support map_ nested" in {
    runWithContext { sc =>
      val data = Seq(List(Some(1), None, Some(2)))
      val expected = Seq(List(Some(2), None, Some(3)))
      val result = sc.parallelize(data).map_(_ + 1)

      result should containInAnyOrder(expected)
    }
  }

  it should "support traverse" in {
    runWithContext { sc =>
      val data =
        Seq(List(Some(1), Some(2), None), List(Some(1), Some(2), Some(3)))
      val expected = Seq(None, Some(List(1, 2, 3)))

      val result = sc.parallelize(data).traverse(identity)

      result should containInAnyOrder(expected)
    }
  }

  it should "support flatTraverse" in {
    runWithContext { sc =>
      val data = Seq(Option(List("1", "2", "3", "four")))
      val expected = Seq(List(Some(1), Some(2), Some(3), None))
      val parseInt: String => Option[Int] = s => Try(s.toInt).toOption

      val result = sc.parallelize(data).flatTraverse(_.map(parseInt))

      result should containInAnyOrder(expected)
    }
  }

  it should "support sequence" in {
    runWithContext { sc =>
      val data = Seq(List(Option(1), Option(2)))
      val expected: Seq[Option[List[Int]]] = Seq(Some(List(1, 2)))
      val result = sc.parallelize(data).sequence

      result should containInAnyOrder(expected)
    }
  }

  it should "support combine_" in {
    runWithContext { sc =>
      import cats._

      val data: Seq[Id[Int]] = Seq(1, 2)
      val expected = Seq(3)
      val result = sc.parallelize(data).combine_

      result should containInAnyOrder(expected)
    }
  }

}
