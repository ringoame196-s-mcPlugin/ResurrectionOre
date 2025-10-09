package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {
    private val plugin = this
    override fun onEnable() {
        super.onEnable()

        // 設定ファイル読み込み
        saveResource("resurrection_time_data.yml", false)
        val resurrectionTimeDataFile = File(plugin.dataFolder, "resurrection_time_data.yml")
        ResurrectionTimeManager.load(resurrectionTimeDataFile)

        // スケジュール関係
        RevivalDatabaseManager.dbPath = "${plugin.dataFolder}/data.db"
        RevivalDatabaseManager.load()

        server.pluginManager.registerEvents(BlockBreakEvent(plugin), plugin)

        val command = getCommand("reore")
        command!!.setExecutor(Command())
    }
}
