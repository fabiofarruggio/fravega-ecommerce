package com.fravega.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson

class MapperUtils {
    fun <T> getBodyAsObject(body: Object, clazz: Class<T>): T? {
        val gson = Gson()
        return gson.fromJson(getBodyAsString(body), clazz) as T
    }

    fun getBodyAsString(body: Object): String {
        return ObjectMapper().writeValueAsString(body)
    }
}
