package dicts

final class IntClosedHashDict[V](initialCapacity: Int, loadFactor: Double) extends IntDict[V] {
  require(initialCapacity >= 1)
  require(loadFactor > 0)

  import IntClosedHashDict.Node

  private var size0: Int             = 0
  private var (capacity0, mask, max) = IntDict.numbers(initialCapacity, loadFactor)

  private var nodes: Array[Node[V]] = new Array[Node[V]](capacity0)

  def capacity: Int = capacity0
  def size: Int     = size0

  def get(key: Int): Option[V] = Option(get0(key)).map(_.value)

  def put(key: Int, value: V): Option[V] = {
    ensureCapacity()
    put0(key, value)
  }

  private def ensureCapacity(): Unit = if (size0 >= max) {
    val (cpc, msk, mx) = IntDict.numbers(capacity0, loadFactor)
    capacity0 = cpc
    mask = msk
    max = mx

    val oldNodes = nodes

    nodes = new Array(capacity0)

    oldNodes.foreach { head =>
      var node = head
      while (node ne null) {
        put0(node.key, node.value)
        node = node.next
      }
    }
  }

  private def get0(key: Int): Node[V] = {
    var node = nodes(IntDict.mix(key) & mask)
    while ((node ne null) && node.key != key) {
      node = node.next
    }
    node
  }

  private def put0(key: Int, value: V): Option[V] = {
    val index = IntDict.mix(key) & mask
    var node  = nodes(index)
    if (node eq null) {
      node = new Node(key, value)
      nodes(index) = node
      size0 += 1
    } else {
      var prev: Node[V] = null
      while ((node ne null) && node.key != key) {
        prev = node
        node = node.next
      }
      if (node eq null) {
        node = new Node(key, value)
        prev.next = node
        size0 += 1
      }
    }

    val oldValue: Option[V] = Option(node.value)
    node.value = value
    oldValue
  }

}

object IntClosedHashDict {
  final private class Node[V](val key: Int, var value: V, var next: Node[V] = null)

  def empty[V]: IntClosedHashDict[V] = apply(IntDict.DefaultCapacity)

  def apply[V](capacity: Int): IntClosedHashDict[V] = apply(capacity, IntDict.DefaultLoadFactor)
  def apply[V](capacity: Int, loadFactor: Double): IntClosedHashDict[V] =
    new IntClosedHashDict[V](capacity, loadFactor)
}
