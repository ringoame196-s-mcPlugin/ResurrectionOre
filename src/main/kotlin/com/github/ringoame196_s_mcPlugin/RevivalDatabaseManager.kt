package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.Plugin

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

    fun load(plugin: Plugin) {
        val sql = "SELECT * FROM $TABLE;"
        var c = 0
        val dbDataList = dataBaseManager.acquisitionValuesList(
            sql,
            mutableListOf(),
            mutableListOf(
                WORLD_KEY,
                X_KEY,
                Y_KEY,
                Z_KEY,
                BLOCK_TYPE_KEY,
                RESURRECTION_AT_KEY
            )
        )

        for (data in dbDataList) {
            val revivalData = conversionRevivalData(data) ?: continue
            if (revivalData.revivalTime > 0) {
                RevivalManager.add(revivalData, plugin)
                c++
            } else {
                RevivalManager.resurrectionBlock(revivalData)
            }
        }
        if (c > 0) {
            Bukkit.broadcastMessage("${ChatColor.YELLOW}[${plugin.name}] ${c}件の鉱石の復活が再開されました")
        }
    }

    private fun conversionRevivalData(data: Map<String, *>): RevivalData? {
        val now = System.currentTimeMillis()

        val worldName = data[WORLD_KEY] as? String ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null
        val x = data[X_KEY].toString().toInt()
        val y = data[Y_KEY].toString().toInt()
        val z = data[Z_KEY].toString().toInt()
        val location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

        val blockTypeName = data[BLOCK_TYPE_KEY].toString()
        val blockType = Material.getMaterial(blockTypeName) ?: return null

        val resurrectionAt = data[RESURRECTION_AT_KEY].toString().toLong()

        // 復活までの残り秒数を計算（負数ならすでに復活時間を過ぎている）
        val remainingSeconds = ((resurrectionAt - now) / 1000).coerceAtLeast(0)

        val revivalData = RevivalData(
            location = location,
            blockType = blockType,
            revivalTime = remainingSeconds.toInt() // 秒単位で設定
        )
        return revivalData
    }
}
