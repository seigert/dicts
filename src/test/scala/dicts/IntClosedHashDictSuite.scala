package dicts

object IntClosedHashDictSuite extends IntDictSpec("IntClosedHashDict") {
  protected def empty: IntDict[Int] = IntClosedHashDict.empty
}
