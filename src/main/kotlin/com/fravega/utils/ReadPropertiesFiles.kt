package com.fravega.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

/**
 * Reads properties files located under classpath:properties/environments/{env}.properties
 */
class ReadPropertiesFiles {
    fun getValueOfPropertyFile(env: String, key: String): String {
        val resource = ClassPathResource("properties/environments/$env.properties")
        val props = PropertiesLoaderUtils.loadProperties(resource)
        return props.getProperty(key)
    }
}