package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class DataBaseManager(private val dbFilePath: String) {
    /**
     * SQLコマンドを実行する
     * @param command 実行するSQL文
     * @param parameters パラメータリスト
     */
    fun executeUpdate(command: String, parameters: List<Any>? = null) {
        try {
            connection.use { conn ->
                conn.prepareStatement(command).use { preparedStatement ->
                    parameters?.bindParameters(preparedStatement)
                    preparedStatement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            Bukkit.getLogger().info("SQL Error: ${e.message}")
            throw e
        }
    }

    fun bulkInsert(
        table: String,
        columns: List<String>,
        dataList: List<List<Any>>
    ) {
        if (dataList.isEmpty()) return

        val placeholders = columns.joinToString(",") { "?" }
        val sql = "INSERT OR REPLACE INTO $table (${columns.joinToString(",")}) VALUES ($placeholders);"

        try {
            connection.use { conn ->
                conn.autoCommit = false
                conn.prepareStatement(sql).use { preparedStatement ->
                    for (params in dataList) {
                        params.forEachIndexed { index, value ->
                            preparedStatement.setObject(index + 1, value)
                        }
                        preparedStatement.addBatch()
                    }
                    preparedStatement.executeBatch()
                }
                conn.commit()
            }
        } catch (e: SQLException) {
            Bukkit.getLogger().warning("SQL bulk insert error: ${e.message}")
            throw e
        }
    }

    /**
     * 単一の値を取得する
     * @param sql 実行するSQL文
     * @param parameters パラメータリスト
     * @param label カラム名
     * @return 結果の値
     */
    fun acquisitionValue(sql: String, parameters: List<Any>, label: String): Any? {
        return acquisitionValues(sql, parameters, mutableListOf(label)).getValue(label)
    }

    /**
     * 複数の行を取得する
     * @param sql 実行するSQL文
     * @param parameters パラメータリスト
     * @param mapper 結果セットの行をオブジェクトにマッピングする関数
     * @return 結果リスト
     */
    fun acquisitionValues(
        sql: String,
        parameters: List<Any>,
        keys: List<String>
    ): Map<String, Any?> {
        try {
            val values = mutableMapOf<String, Any?>()
            connection.use { conn ->
                conn.prepareStatement(sql).use { preparedStatement ->
                    parameters.bindParameters(preparedStatement)
                    preparedStatement.executeQuery().use { resultSet ->
                        if (resultSet.next()) {
                            for (key in keys) {
                                values[key] = try {
                                    resultSet.getString(key)
                                } catch (e: SQLException) {
                                    null
                                }
                            }
                        }
                    }
                }
            }
            return values
        } catch (e: SQLException) {
            Bukkit.getLogger().info("SQL Error: ${e.message}")
            throw e
            return mapOf()
        }
    }

    // SQLiteコネクションの取得
    private val connection: Connection
        get() = DriverManager.getConnection("jdbc:sqlite:$dbFilePath")

    // パラメータをPreparedStatementにバインドする拡張関数
    private fun List<Any>.bindParameters(preparedStatement: PreparedStatement) {
        this.forEachIndexed { index, param ->
            preparedStatement.setObject(index + 1, param)
        }
    }
}
