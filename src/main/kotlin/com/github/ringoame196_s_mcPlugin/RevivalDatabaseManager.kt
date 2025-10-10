package com.github.ringoame196_s_mcPlugin

object RevivalDatabaseManager {
    // db(永久データ)を管理
    lateinit var dataBaseManager: DataBaseManager
    private val cache = mutableListOf<RevivalData>()

    private const val TABLE = "RevivalData"
    private const val WORLD_KEY = "world"
    private const val X_KEY = "x"
    private const val Y_KEY = "y"
    private const val Z_KEY = "z"
    private const val BLOCK_TYPE_KEY = "block_type"
    private const val RESURRECTION_AT_KEY = "resurrection_at"

    fun saveDB(revivalData: RevivalData) {
        cache.add(revivalData)
    }

    fun flushToDatabase() {
        if (!::dataBaseManager.isInitialized) return
        if (cache.isEmpty()) return
        val dataList = cache.map { it.toDBParams() }

        dataBaseManager.bulkInsert(
            TABLE,
            listOf(WORLD_KEY, X_KEY, Y_KEY, Z_KEY, BLOCK_TYPE_KEY, RESURRECTION_AT_KEY),
            dataList
        )
        cache.clear()
    }

    fun deleteDB(revivalData: RevivalData) {
        val sql = "DELETE FROM $TABLE WHERE $WORLD_KEY = ? AND $X_KEY = ? AND $Y_KEY = ? AND $Z_KEY = ?;"
        val params = revivalData.toDeleteParams()

        dataBaseManager.executeUpdate(
            sql,
            params
        )
    }

    fun deleteCache(revivalData: RevivalData) {
        cache.remove(revivalData)
    }

    fun load() {
    }
}
