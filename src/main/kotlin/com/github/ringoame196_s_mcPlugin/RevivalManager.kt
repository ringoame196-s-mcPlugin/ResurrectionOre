package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin

object RevivalManager {
    val breakBlockBlock = Material.BEDROCK

    fun add(data: RevivalData, time: Int, plugin: Plugin) {
        RevivalSchedule.addTask(data, time, plugin)
        RevivalDatabaseManager.save(data, time) // db保存

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
        location.block.type = breakBlockBlock
    }

    fun resurrectionBlock(data: RevivalData) {
        val location = data.location
        val blockType = data.blockType
        location.block.type = blockType
        RevivalDatabaseManager.delete(data)
    }
}
