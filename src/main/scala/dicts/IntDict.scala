package dicts

trait IntDict[V] {

  /** Returns value by key if available. */
  def get(key: Int): Option[V]

  /** Inserts or updates value associated with given key.
   *
   *  @return previously associated value if available
   */
  def put(key: Int, value: V): Option[V]

}

object IntDict {
  val IntPhi: Int = 0x9e3779b9

  val DefaultCapacity: Int      = 16
  val DefaultLoadFactor: Double = 0.95

  def mix(i: Int): Int = {
    val h = i * IntPhi
    h ^ (h >>> 16)
  }

  /** Returns next capacity, mask and max number of dictionary elements. */
  def numbers(initialCapacity: Int, loadFactor: Double): (Int, Int, Int) = {
    val capacity = 1 << (32 - Integer.numberOfLeadingZeros(initialCapacity))
    val mask     = capacity - 1
    val max      = math.min(math.ceil(loadFactor * capacity).toInt, mask)

    (capacity, mask, max)
  }
}
