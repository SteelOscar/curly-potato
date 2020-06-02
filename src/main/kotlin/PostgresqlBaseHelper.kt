import java.util.*
import java.util.concurrent.ThreadLocalRandom

interface PostgresqlBaseHelper<T> {

    val username: String
        get() = "postgres"

    val password: String
        get() = "12345"

    val dataBase: String
        get() = "repltest"

    val random: ThreadLocalRandom
        get() = ThreadLocalRandom.current()

    fun writeTests(tests: T)

    fun deleteAll()

    fun selectAll(): T
}