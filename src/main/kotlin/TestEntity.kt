

data class TestEntity(val id: Int) : Comparable<TestEntity> {

    override fun compareTo(other: TestEntity): Int {

        return id.compareTo(other.id)
    }
}