package com.fravega.helpers

import com.fravega.conf.ApiConfig
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured.oauth2
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification

object RestAssuredHelper {
    fun specWithToken(token: String): RequestSpecification =
        RequestSpecBuilder()
            .setBaseUri(ApiConfig.baseUrl)
            .addFilter(AllureRestAssured())
            .setAuth(oauth2(token))
            .setContentType("application/json")
            .build()
}
