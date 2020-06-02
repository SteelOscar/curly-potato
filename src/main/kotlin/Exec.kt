import java.util.*

fun main() {

    val liveDataSet = LiveDataSet<Int>()

    val subscriber = object: Subscriber<Int> {

        override fun onChanged(set: TreeSet<Int>) {

            println("change ${set.size}")
        }
    }

    val treeSet = TreeSet<Int>()

    treeSet.add(1)
    treeSet.add(2)
    treeSet.add(3)
    treeSet.add(4)

    liveDataSet.subscribe(subscriber)

    liveDataSet.postValue(treeSet)
}