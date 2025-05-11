package com.fravega.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import io.restassured.response.ResponseBodyExtractionOptions
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.io.IOException

open class CommonHelper {

    protected val TOO_MUCH = 20000
    protected val A_LOT = 10000
    protected val NORMAL = 5000
    protected val A_LITTLE = 3000

    protected val environment: String = System.getProperty("environment")
    protected val envFile = ClassPathResource("properties/environments/$environment.properties")
    protected val environmentValues = PropertiesLoaderUtils.loadProperties(envFile)

    protected fun sleep(milliseconds: Int) {
        Thread.sleep(milliseconds.toLong())
    }

    protected inline fun <reified T> ResponseBodyExtractionOptions.As(): T {
        return this.`as`(T::class.java)
    }

    @Throws(IOException::class)
    open fun <T> jsonArrayToObjectList(json: String?, tClass: Class<T>?): List<T>? {
        val mapper = ObjectMapper()
        val listType: CollectionType = mapper.typeFactory.constructCollectionType(ArrayList::class.java, tClass)
        return mapper.readValue(json, listType)
    }
}
