package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object RevivalSchedule {
    private val taskList = mutableListOf<RevivalData>()

    fun addTask(data: RevivalData, time: Int, plugin: Plugin) {
        if (taskList.contains(data)) return
        taskList.add(data)
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable { // 実行したいコードをここに書く
                RevivalManager.resurrectionBlock(data)
                taskList.remove(data)
            },
            (time * 20).toLong()
        ) // 20Lは1秒を表す（1秒 = 20ticks）
    }
}
