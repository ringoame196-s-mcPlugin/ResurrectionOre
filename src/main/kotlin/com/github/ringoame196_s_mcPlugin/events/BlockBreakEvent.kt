package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.ResurrectionTimeManager
import com.github.ringoame196_s_mcPlugin.RevivalData
import com.github.ringoame196_s_mcPlugin.RevivalManager
import com.github.ringoame196_s_mcPlugin.RevivalSchedule
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.Plugin

class BlockBreakEvent(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val block = e.block
        val location = block.location

        if (player.gameMode == GameMode.CREATIVE) {
            RevivalSchedule.removeTask(location)
            return
        }

        // 復活時間が設定されていなければ終了
        val resurrectionTime = ResurrectionTimeManager.getResurrectionTime(block.type) ?: return
        val revivalData = RevivalData(
            block.type,
            location
        )
        RevivalManager.add(revivalData, resurrectionTime, plugin)
    }
}
