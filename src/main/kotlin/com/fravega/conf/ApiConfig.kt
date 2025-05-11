package com.fravega.conf

import java.util.*

object ApiConfig {
    val baseUrl: String

    init {
        val props = Properties().apply {
            // Ajusta la ruta si tu properties está en src/main/resources/properties/…
            load(ApiConfig::class.java.classLoader.getResourceAsStream("properties/application.properties"))
        }
        baseUrl = props.getProperty("api.base.url")
            ?: throw IllegalStateException("❌ api.base.url no definida en application.properties")
    }
}
