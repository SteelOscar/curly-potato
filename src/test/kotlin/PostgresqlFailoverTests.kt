import org.junit.Test
import org.junit.Assert.*

class PostgresqlFailoverTests {


    @Test
    fun `should be the same amount`() {

        val transactionSet = PostgresqlHelper("192.168.200.133", 5432).run {

            writeTests(DataSource.getTestEntities())
        }

        val realSet =  PostgresqlHelper("192.168.200.134", 5432).run {
            selectAll()
        }

        println("transaction count = ${transactionSet.size}")
        println("real count = ${realSet.size}")

        assertTrue(realSet.containsAll(transactionSet))
    }
}