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

    private val threadPool = Executors.newFixedThreadPool(100)
    private val transactionSet = TreeSet<TestEntity>()

    override fun writeTests(tests: TreeSet<TestEntity>): TreeSet<TestEntity> {

        val randomFailover = random.nextInt(300000, 700000)

        println("randomFailover = $randomFailover")

        transaction {

            SchemaUtils.create(TestTable)
        }

        tests.forEachIndexed { index, testEntity ->

            if (index >= randomFailover) {

                HelperSSH.shutdownMaster()
                threadPool.shutdownNow()
                return transactionSet
            }

            threadPool.submit {

                Thread.sleep(random.nextLong(800L))

                transaction {

                    TestTable.insert { it[id] = testEntity.id }
                    commit()
                }
                transactionSet.add(testEntity)
            }
        }

        return transactionSet
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