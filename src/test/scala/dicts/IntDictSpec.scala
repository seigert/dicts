package dicts

import weaver.SimpleIOSuite
import weaver.scalacheck.{ CheckConfig, Checkers }

abstract class IntDictSpec(name: String) extends SimpleIOSuite with Checkers {
  protected def empty: IntDict[Int]

  override def checkConfig: CheckConfig = CheckConfig.default.copy(maximumGeneratorSize = 10)

  test(s"$name should put single value into dict") {
    forall { i: Int =>
      val dict = empty
      dict.put(i, i)

      expect(dict.get(i).contains(i))
    }
  }

  test(s"$name should put all given values into dict") {
    forall { xs: List[Int] =>
      val dict = empty
      xs.foreach(i => dict.put(i, i))

      forEach(xs) { i =>
        expect(dict.get(i).contains(i))
      }
    }
  }

  test(s"$name should contain only put values") {
    forall { xs: List[Int] =>
      val dict         = empty
      val (init, tail) = xs.splitAt(xs.size / 2)
      init.foreach(i => dict.put(i, i))

      forEach(tail) { i =>
        expect(dict.get(i).isEmpty) ||
        expect(init.contains(i))
      }
    }
  }

  test(s"$name should replace put values") {
    forall { xs: List[Int] =>
      val dict = empty
      xs.foreach(i => dict.put(i, i))

      forEach(xs) { i =>
        val old = dict.put(i, i + 1)
        (expect(old.contains(i)) || expect(old.contains(i + 1))) &&
        expect(dict.get(i).contains(i + 1))
      }
    }
  }

}
