package dicts

import scala.reflect.ClassTag

final class IntOpenHashDict[V: ClassTag](initialCapacity: Int, loadFactor: Double) extends IntDict[V] {
  require(initialCapacity >= 1)
  require(loadFactor > 0)

  private var size0: Int             = 0
  private var (capacity0, mask, max) = IntDict.numbers(initialCapacity, loadFactor)

  private var keys: Array[Int] = new Array[Int](capacity0)
  private var values: Array[V] = new Array[V](capacity0)

  private var zero: Option[V] = None

  def capacity: Int = capacity0
  def size: Int     = size0

  def get(key: Int): Option[V] = if (key != 0) {
    val index = getKeyIndex(key)
    if (index < 0) None
    else Some(values(index))
  } else zero

  def put(key: Int, value: V): Option[V] = {
    ensureCapacity()
    put0(key, value)
  }

  private def ensureCapacity(): Unit = if (size0 > max) {
    val (cpc, msk, mx) = IntDict.numbers(capacity0, loadFactor)
    capacity0 = cpc
    mask = msk
    max = mx

    val oldKeys   = keys
    val oldValues = values

    keys = new Array[Int](capacity0)
    values = new Array[V](capacity0)

    oldKeys.indices.foreach { i =>
      val key = oldKeys(i)
      if (key != 0) {
        put0(key, oldValues(i))
      }
    }
  }

  private def getKeyIndex(key: Int): Int = {
    var index  = IntDict.mix(key) & mask
    var stored = keys(index)

    while (stored != 0 && stored != key) {
      index = (index + 1) & mask
      stored = keys(index)
    }

    if (stored == 0) -(index + 1)
    else index
  }

  private def put0(key: Int, value: V): Option[V] = {
    var oldValue: Option[V] = None
    if (key != 0) {
      var index = getKeyIndex(key)

      if (index < 0) {
        index = -index - 1
        keys(index) = key
      } else {
        oldValue = Option(values(index))
      }

      values(index) = value
    } else {
      oldValue = zero
      zero = Some(value)
    }

    if (oldValue.isEmpty) {
      size0 += 1
    }

    oldValue
  }

}

object IntOpenHashDict {
  def empty[V: ClassTag]: IntOpenHashDict[V] = apply(IntDict.DefaultCapacity)

  def apply[V: ClassTag](capacity: Int): IntOpenHashDict[V] = apply(capacity, IntDict.DefaultLoadFactor)
  def apply[V: ClassTag](capacity: Int, loadFactor: Double): IntOpenHashDict[V] =
    new IntOpenHashDict[V](capacity, loadFactor)
}
