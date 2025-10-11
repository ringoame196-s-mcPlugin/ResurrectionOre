package com.github.ringoame196_s_mcPlugin.manager

import com.github.ringoame196_s_mcPlugin.data.RevivalData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

object RevivalSchedule {
    private val taskList = mutableMapOf<Location, BukkitTask>()

    fun addTask(data: RevivalData, plugin: Plugin) {
        val location = data.location
        val time = data.revivalTime
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