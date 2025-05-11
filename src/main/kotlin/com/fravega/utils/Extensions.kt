package com.fravega.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> loggerFor(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun getCustomParam(param: String): String? {
    val customParams = System.getProperty("customParams")
    println("Custom-params: $customParams")
    val keyValuePairs = customParams.trim()
    val regex = Regex("""([^=,]+)=((?:\[[^\]]*\])|(?:[^,]*))""")
    val matches = regex.findAll(keyValuePairs)
    val map = mutableMapOf<String, String>()
    for (match in matches) {
        val key = match.groups[1]?.value
        val value = match.groups[2]?.value
        if (key != null && value != null) {
            map[key] = value
        }
    }
    val paramValue = map[param] ?: return null
    return paramValue
}