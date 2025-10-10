package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.BlockBreakEvent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File

class Main : JavaPlugin() {
    private val plugin = this
    lateinit var task: BukkitTask

    override fun onEnable() {
        super.onEnable()

        // 設定ファイル読み込み
        saveResource("resurrection_time_data.yml", false)
        val resurrectionTimeDataFile = File(plugin.dataFolder, "resurrection_time_data.yml")
        ResurrectionTimeManager.load(resurrectionTimeDataFile)

        // スケジュール関係
        saveResource("data.db", false)
        val dbPath = "${plugin.dataFolder}/data.db"
        RevivalDatabaseManager.dataBaseManager = DataBaseManager(dbPath)
        RevivalDatabaseManager.load()

        task = Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                RevivalDatabaseManager.flushToDatabase()
            },
            0L, 20L * 10L
        ) // 10秒ごとにDB保存

        server.pluginManager.registerEvents(BlockBreakEvent(plugin), plugin)

        val command = getCommand("reore")
        command!!.setExecutor(Command())
    }

    override fun onDisable() {
        super.onDisable()
        task.cancel()
    }
}
