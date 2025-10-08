package com.github.ringoame196_s_mcPlugin

import org.yaml.snakeyaml.Yaml
import java.io.File

class YamlFileManager {
    fun loadYAsMap(file: File): Map<String, String> {
        val yaml = Yaml()
        val inputStream = file.inputStream()
        return yaml.load(inputStream) as Map<String, String>
    }
}
