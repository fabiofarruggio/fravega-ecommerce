package com.fravega.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class ObjectMapperBuilder {
    private val objectMapper: ObjectMapper
    fun includeNotNull(): ObjectMapperBuilder {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return this
    }

    fun <T> withDeserializer(type: Class<T>?, deserializer: JsonDeserializer<out T>?): ObjectMapperBuilder {
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(type, deserializer)
        objectMapper.registerModule(simpleModule)
        return this
    }

    fun readEnumsUsingToString(): ObjectMapperBuilder {
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
        return this
    }

    fun writeEnumsUsingToString(): ObjectMapperBuilder {
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
        return this
    }

    fun build(): ObjectMapper {
        return objectMapper
    }

    init {
        objectMapper = ObjectMapper()
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(KotlinModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    }
}
