package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import java.io.File

object ResurrectionTimeManager {
    private val resurrectionTimeData = mutableMapOf<Material, Int>()

    fun load(file: File) {
        val yamlFileManager = YamlFileManager()
        reset()
        val fileData = yamlFileManager.loadYAsMap(file)

        for ((k, v) in fileData) {
            val blockType = Material.getMaterial(k.uppercase()) ?: continue
            val time = v.toIntOrNull() ?: continue
            resurrectionTimeData[blockType] = time
        }
    }

    fun getResurrectionTime(blockType: Material): Int? {
        return resurrectionTimeData[blockType]
    }

    private fun reset() {
        resurrectionTimeData.clear()
    }
}
