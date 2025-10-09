package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object RevivalSchedule {
    private val taskList = mutableListOf<RevivalData>()

    fun addTask(revivalData: RevivalData, resurrectionTime: Int, plugin: Plugin) {
        if (taskList.contains(revivalData)) return
        taskList.add(revivalData)
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable { // 実行したいコードをここに書く
                RevivalManager.resurrectionBlock(revivalData)
                taskList.remove(revivalData)
            },
            (resurrectionTime * 20).toLong()
        ) // 20Lは1秒を表す（1秒 = 20ticks）
    }
}
