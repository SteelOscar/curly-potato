import java.util.*
import kotlin.collections.HashSet

class LiveDataSet<T> {

    private val subscribers = HashSet<Subscriber<T>>()

    private val liveData = TreeSet<T>()

    fun subscribe(subscriber: Subscriber<T>) {

        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: Subscriber<T>) = subscribers.remove(subscriber)

    fun postValue(value: TreeSet<T>) {

        liveData.clear()
        liveData.addAll(value)
        println("subscribers ${subscribers.size}")
        subscribers.forEach { it.onChanged(liveData) }
    }
}