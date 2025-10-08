package com.github.ringoame196_s_mcPlugin.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class Events : Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {}
}
