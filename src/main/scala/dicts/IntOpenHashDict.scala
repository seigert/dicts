package dicts

import scala.reflect.ClassTag

final class IntOpenHashDict[V: ClassTag](initialCapacity: Int, loadFactor: Double) extends IntDict[V] {
  require(initialCapacity >= 1)
  require(loadFactor > 0)

  private var size: Int             = 0
  private var (capacity, mask, max) = IntDict.numbers(initialCapacity, loadFactor)

  private var keys: Array[Int] = new Array[Int](capacity)
  private var values: Array[V] = new Array[V](capacity)

  private var zero: Option[V] = None

  def get(key: Int): Option[V] = if (key != 0) {
    val index = getKeyIndex(key)
    if (index < 0) None
    else Some(values(index))
  } else zero

  def put(key: Int, value: V): Option[V] = {
    val result = put0(key, value)
    ensureCapacity()
    result
  }

  private def ensureCapacity(): Unit = if (size > max) {
    val (cpc, msk, mx) = IntDict.numbers(capacity, loadFactor)
    capacity = cpc
    mask = msk
    max = mx

    val oldKeys   = keys
    val oldValues = values

    keys = new Array[Int](capacity)
    values = new Array[V](capacity)

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
      size += 1
    }

    oldValue
  }

}

object IntOpenHashDict {
  def empty[V: ClassTag]: IntOpenHashDict[V] = apply(IntDict.DefaultCapacity, IntDict.DefaultLoadFactor)

  def apply[V: ClassTag](capacity: Int, loadFactor: Double): IntOpenHashDict[V] =
    new IntOpenHashDict[V](capacity, loadFactor)
}
