import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.Executors

class PostgresqlHelper(

    host: String,
    port: Int

) : PostgresqlBaseHelper<TreeSet<TestEntity>> {

    val db = Database.connect(
        url = "jdbc:postgresql://$host:$port/$dataBase",
        driver = "org.postgresql.Driver",
        user = username,
        password = password
    )

    private val transactionSet = TreeSet<TestEntity>()
    private val threadPool = Executors.newFixedThreadPool(100)
    val liveDataSet = LiveDataSet<TestEntity>()

    override fun writeTests(tests: TreeSet<TestEntity>) {

        val randomFailover = random.nextInt(1000, 2000)

        println("randomFailover = $randomFailover")

        transaction {

            SchemaUtils.create(TestTable)
        }

        tests.forEachIndexed { index, testEntity ->

            threadPool.submit {

                Thread.sleep(random.nextLong(800L))

                if (index >= randomFailover) {

                    HelperSSH.shutdownMaster()
                    threadPool.shutdownNow()
                    liveDataSet.postValue(transactionSet)
                }

                transaction {

                    TestTable.insert { it[id] = testEntity.id }
                    commit()
                }
                transactionSet.add(testEntity)
            }
        }

        while (threadPool.isShutdown.not()) {}
    }

    override fun deleteAll() {

        transaction {

            TestTable.deleteAll()
        }
    }

    override fun selectAll(): TreeSet<TestEntity> = transaction {

        TestTable.selectAll().mapTo(TreeSet()) { TestEntity(it[TestTable.id]) }
    }
}