package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin

object RevivalManager {
    val BREAK_BLOCK_TYPE = Material.BEDROCK

    fun add(data: RevivalData, plugin: Plugin) {
        RevivalSchedule.addTask(data, plugin)
        RevivalDatabaseManager.saveDB(data) // db保存

        // 1tick後に置き換え
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                setBreakBlock(data)
            }
        )
    }

    fun setBreakBlock(data: RevivalData) {
        val location = data.location
        location.block.type = BREAK_BLOCK_TYPE
    }

    fun resurrectionBlock(data: RevivalData) {
        val location = data.location
        val blockType = data.blockType
        location.block.type = blockType

        // DB関係
        RevivalDatabaseManager.deleteCache(data)
        RevivalDatabaseManager.deleteDB(data)
    }
}
