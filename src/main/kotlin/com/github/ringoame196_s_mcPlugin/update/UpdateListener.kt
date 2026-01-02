package com.github.ringoame196_s_mcPlugin.update

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UpdateListener(private val versionManager: VersionManager) : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player = e.player
        if (!player.isOp) return
        versionManager.sendPlayerMessage(player)
    }
}
