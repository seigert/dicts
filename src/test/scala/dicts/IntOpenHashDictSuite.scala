package dicts

object IntOpenHashDictSuite extends IntDictSpec("IntOpenHashDict") {
  protected def empty: IntDict[Int] = IntOpenHashDict.empty
}
