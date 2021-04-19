package dicts
package benchmark

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit
import scala.collection.immutable.ArraySeq
import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 10)
class IntDictBenchmark {

  val alphabet: String = ('a' to 'z').map(c => s"$c${c.toUpper}").mkString

  @Param(Array("100", "10000", "1000000"))
  var count: Int = 0

  private var keys: ArraySeq[Int]      = ArraySeq.empty
  private var values: ArraySeq[String] = ArraySeq.empty

  private val closedDict: IntClosedHashDict[String] = IntClosedHashDict.empty
  private val openDict: IntOpenHashDict[String]     = IntOpenHashDict.empty

  @Setup(Level.Trial)
  def doSetup(): Unit = {
    val keys0   = Array.range(0, 1 << 20)
    val values0 = Array.fill(keys0.length)(Random.shuffle(alphabet).toString())

    keys0.indices.foreach { i =>
      closedDict.put(keys0(i), values0(i))
      openDict.put(keys0(i), values0(i))
    }

    keys = ArraySeq.unsafeWrapArray(keys0)
    values = ArraySeq.unsafeWrapArray(values0)
  }

  @inline
  private def get(dict: IntDict[String]): Int = {
    var res = 0
    (0 to count).foreach { i =>
      val value = dict.get(keys(i))
      if (value.isDefined) res += 1
    }
    res
  }

  @Benchmark
  def getClosed(): Int = get(closedDict)

  @Benchmark
  def getOpen(): Int = get(openDict)

  @inline
  private def put(dict: IntDict[String]): Int = {
    var res = 0
    (0 to count).foreach { i =>
      val value = dict.put(keys(i), values(i))
      if (value.isEmpty) res += 1
    }
    res
  }

  @Benchmark
  def putClosed(): Int = put(IntClosedHashDict.empty)

  @Benchmark
  def putOpen(): Int = put(IntOpenHashDict.empty)

  @inline
  private def putAndGet(dict: IntDict[String]): Int = {
    val all = keys.view.take(count)
    val put = ArraySeq.from(Random.shuffle(all).take(3 * count / 4))
    val get = ArraySeq.from(Random.shuffle(all).take(3 * count / 4))

    var res = 0
    put.indices.foreach { i =>
      dict.put(put(i), values(i))
    }
    get.foreach { key =>
      val value = dict.get(key)
      if (value.isDefined) res += 1
    }
    res
  }

  @Benchmark
  def putAndGetClosed(): Int = putAndGet(IntClosedHashDict.empty)

  @Benchmark
  def putAndGetOpen(): Int = putAndGet(IntOpenHashDict.empty)

}
