package com.github.ringoame196_s_mcPlugin

import org.bukkit.Location
import org.bukkit.Material

data class RevivalData(
    val blockType: Material,
    val location: Location,
    val revivalTime: Int
) {

    /**
     * DBに渡すパラメータリストに変換
     */
    fun toDBParams(): List<Any> {
        val resurrectionAt = System.currentTimeMillis() + (revivalTime * 1000)
        return listOf(
            location.world?.name as Any,
            location.blockX as Any,
            location.blockY as Any,
            location.blockZ as Any,
            blockType.name as Any,
            resurrectionAt as Any
        )
    }

    /**
     * DB削除用のパラメータリストに変換
     */
    fun toDeleteParams(): List<Any> {
        return listOf(
            location.world?.name as Any,
            location.blockX as Any,
            location.blockY as Any,
            location.blockZ as Any
        )
    }
}
