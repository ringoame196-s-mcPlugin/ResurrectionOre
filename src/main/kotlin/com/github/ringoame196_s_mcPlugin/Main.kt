package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {
    private val plugin = this
    override fun onEnable() {
        super.onEnable()

        saveResource("resurrection_time_data.yml", false)
        val resurrectionTimeDataFile = File(plugin.dataFolder, "resurrection_time_data.yml")
        ResurrectionTimeManager.load(resurrectionTimeDataFile)

        server.pluginManager.registerEvents(BlockBreakEvent(), plugin)

        val command = getCommand("reore")
        command!!.setExecutor(Command())
    }
}
