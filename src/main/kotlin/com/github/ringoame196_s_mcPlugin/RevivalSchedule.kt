package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

object RevivalSchedule {
    private val taskList = mutableMapOf<Location, BukkitTask>()

    fun addTask(data: RevivalData, time: Int, plugin: Plugin) {
        val location = data.location
        if (taskList.contains(location)) return
        val task = Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable { // 実行したいコードをここに書く
                RevivalManager.resurrectionBlock(data)
                taskList.remove(data.location)
            },
            (time * 20).toLong()
        ) // 20Lは1秒を表す（1秒 = 20ticks）
        taskList[data.location] = task
    }

    fun removeTask(location: Location) {
        val task = taskList[location] ?: return
        task.cancel()
        taskList.remove(location)
    }
}
