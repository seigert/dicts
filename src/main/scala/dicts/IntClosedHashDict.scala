package dicts

final class IntClosedHashDict[V](initialCapacity: Int, loadFactor: Double) extends IntDict[V] {
  require(initialCapacity >= 1)
  require(loadFactor > 0)

  import IntClosedHashDict.Node

  private var size: Int             = 0
  private var (capacity, mask, max) = IntDict.numbers(initialCapacity, loadFactor)

  private var nodes: Array[Node[V]] = new Array[Node[V]](capacity)

  def get(key: Int): Option[V] = Option(get0(key)).map(_.value)

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

    val oldNodes = nodes

    nodes = new Array(capacity)

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
      size += 1
    } else {
      var prev: Node[V] = null
      while ((node ne null) && node.key != key) {
        prev = node
        node = node.next
      }
      if (node eq null) {
        node = new Node(key, value)
        prev.next = node
        size += 1
      }
    }

    val oldValue: Option[V] = Option(node.value)
    node.value = value
    oldValue
  }

}

object IntClosedHashDict {
  final private class Node[V](val key: Int, var value: V, var next: Node[V] = null)

  def empty[V]: IntClosedHashDict[V] = apply(IntDict.DefaultCapacity, IntDict.DefaultLoadFactor)

  def apply[V](capacity: Int, loadFactor: Double): IntClosedHashDict[V] =
    new IntClosedHashDict[V](capacity, loadFactor)
}
