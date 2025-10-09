package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin

object RevivalManager {
    val breakBlockBlock = Material.BEDROCK

    fun add(revivalData: RevivalData, resurrectionTime: Int, plugin: Plugin) {
        RevivalSchedule.addTask(revivalData, resurrectionTime, plugin)
        RevivalDatabaseManager.save(revivalData, resurrectionTime) // db保存

        // 1tick後に置き換え
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                setBreakBlock(revivalData)
            }
        )
    }

    fun setBreakBlock(revivalData: RevivalData) {
        val location = revivalData.location
        location.block.type = breakBlockBlock
    }

    fun resurrectionBlock(revivalData: RevivalData) {
        val location = revivalData.location
        val blockType = revivalData.blockType
        location.block.type = blockType
        RevivalDatabaseManager.delete(revivalData)
    }
}
