import org.junit.*
import org.junit.Assert.assertTrue
import java.util.*

class PostgresqlFailoverTests {

    private lateinit var master: PostgresqlHelper
    private lateinit var slave: PostgresqlHelper

    private var transactionSet = TreeSet<TestEntity>()

    private val subscriber = object: Subscriber<TestEntity> {

        override fun onChanged(set: TreeSet<TestEntity>) {

            transactionSet = set
        }
    }

    @Before
    fun setUp() {

        master = PostgresqlHelper("192.168.200.133", 5432)

        master.liveDataSet.subscribe(subscriber)
        master.deleteAll()
        master.writeTests(DataSource.getTestEntities())
    }

    @Test
    fun `should be the same amount`() {

        slave=  PostgresqlHelper("192.168.200.134", 5432)

        val realSet = slave.selectAll()

        println("transaction count = ${transactionSet.size}")
        println("real count = ${realSet.size}")

        assertTrue(realSet.containsAll(transactionSet))
    }

    @After
    fun tearDown() {

        master.liveDataSet.unsubscribe(subscriber)
    }
}