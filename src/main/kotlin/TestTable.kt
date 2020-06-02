import org.jetbrains.exposed.sql.Table

object TestTable: Table("test") {

    val id = integer("id")
}