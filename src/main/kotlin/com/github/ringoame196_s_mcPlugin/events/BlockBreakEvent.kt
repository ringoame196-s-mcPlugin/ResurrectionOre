package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.ResurrectionTimeManager
import com.github.ringoame196_s_mcPlugin.RevivalData
import com.github.ringoame196_s_mcPlugin.RevivalManager
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakEvent : Listener {
    val breakBlockBlock = Material.BEDROCK

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        if (player.gameMode == GameMode.CREATIVE) return

        val block = e.block
        val location = block.location

        // 復活時間が設定されていなければ終了
        val resurrectionTime = ResurrectionTimeManager.getResurrectionTime(block.type) ?: return
        block.type = breakBlockBlock
        val revivalData = RevivalData(
            block.type,
            location
        )
        RevivalManager.add(revivalData, resurrectionTime)
    }
}
