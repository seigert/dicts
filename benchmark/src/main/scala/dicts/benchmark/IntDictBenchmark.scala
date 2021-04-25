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
  val random: Random   = new Random(IntDict.IntPhi)

  val sample: Int = 1 << 20
  val maxKey: Int = 2 * sample

  @Param(Array("127", "16535", "1048575"))
  var count: Int = 0

  private val keys: ArraySeq[Int] = ArraySeq.fill(sample)(random.nextInt(maxKey) - maxKey / 2)
  private val values: ArraySeq[String] = ArraySeq.fill(sample)(random.shuffle(alphabet).toString())
  private val except: ArraySeq[Int] = keys.map(_ + maxKey)

  private var ids: ArraySeq[Int]                    = ArraySeq.empty
  private var closedDict: IntClosedHashDict[String] = IntClosedHashDict.empty
  private var openDict: IntOpenHashDict[String]     = IntOpenHashDict.empty

  @Setup(Level.Trial)
  def doTrialSetup(): Unit = {
    closedDict = IntClosedHashDict(count)
    openDict = IntOpenHashDict(count)

    ids = ArraySeq.fill(count)(random.nextInt(keys.length))
    ids.foreach { i =>
      closedDict.put(keys(i), values(i))
      openDict.put(keys(i), values(i))
    }
  }

  @inline
  private def get(dict: IntDict[String]): Int = {
    var res = 0
    ids.foreach { i =>
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
  private def miss(dict: IntDict[String]): Int = {
    var res = 0
    ids.foreach { i =>
      val value = dict.get(except(i))
      if (value.isDefined) res += 1
    }
    res
  }

  @Benchmark
  def missClosed(): Int = miss(closedDict)

  @Benchmark
  def missOpen(): Int = miss(openDict)

  @inline
  private def put(dict: IntDict[String]): Int = {
    var res = 0
    ids.foreach { i =>
      val value = dict.put(keys(i), values(i))
      if (value.isEmpty) res += 1
    }
    res
  }

  @Benchmark
  def putClosed(): Int = put(IntClosedHashDict.empty)

  @Benchmark
  def putOpen(): Int = put(IntOpenHashDict.empty)

  @Benchmark
  def putAllocatedClosed(): Int = put(IntClosedHashDict(count))

  @Benchmark
  def putAllocatedOpen(): Int = put(IntOpenHashDict(count))

  @inline
  private def putAndGet(dict: IntDict[String]): Int = {
    var res = 0
    ids.foreach { i =>
      dict.put(keys(i), values(i))
    }
    ids.iterator.zipWithIndex.foreach { case (i, j) =>
      val key   = if (j % 2 == 0) keys(i) else except(i)
      val value = dict.get(key)
      if (value.isDefined) res += 1
    }
    res
  }

  @Benchmark
  def putAndGetClosed(): Int = putAndGet(IntClosedHashDict.empty)

  @Benchmark
  def putAndGetOpen(): Int = putAndGet(IntOpenHashDict.empty)

  @Benchmark
  def putAndGetAllocatedClosed(): Int = putAndGet(IntClosedHashDict(count))

  @Benchmark
  def putAndGetAllocatedOpen(): Int = putAndGet(IntOpenHashDict(count))

}
