package com.github.ringoame196_s_mcPlugin.update

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.net.URL

class VersionManager(private val plugin: Plugin, private val versionUrl: String, private val notification: Boolean) {
    private val currentVersion = plugin.description.version
    private var latestVersion: String? = null

    fun checkAsync(onComplete: () -> Unit = {}) {
        if (!notification) return
        Bukkit.getScheduler().runTaskAsynchronously(
            plugin,
            Runnable {
                latestVersion = fetchLatestVersion()
                Bukkit.getScheduler().runTask(
                    plugin,
                    Runnable {
                        onComplete()
                    }
                )
            }
        )
    }

    fun hasUpdate(): Boolean {
        if (!notification) return false
        return latestVersion != null && latestVersion != currentVersion
    }

    fun sendConsoleMessage() {
        if (!notification) return
        if (!hasUpdate()) return
        plugin.logger.info("[Update] New version available: $latestVersion (current: $currentVersion)")
    }

    fun sendPlayerMessage(player: Player) {
        if (!notification) return
        if (!hasUpdate()) return
        player.sendMessage(
            "${ChatColor.GOLD}[Update] ${plugin.name} の新しいバージョン: ${ChatColor.YELLOW}$latestVersion"
        )
    }

    private fun fetchLatestVersion(): String? {
        return try {
            URL(versionUrl).readText().trim()
        } catch (e: Exception) {
            plugin.logger.warning("Failed to fetch latest version: ${e.message}")
            null
        }
    }
}
