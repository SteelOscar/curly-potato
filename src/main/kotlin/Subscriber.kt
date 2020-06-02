import java.util.*

interface Subscriber<T> {

    fun onChanged(set: TreeSet<T>)
}