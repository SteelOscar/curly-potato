import java.util.*

object DataSource {

    fun getTestEntities() = TreeSet<TestEntity>().apply {

        for (i in 1..1000000)
            add(TestEntity(id = i))
    }
}