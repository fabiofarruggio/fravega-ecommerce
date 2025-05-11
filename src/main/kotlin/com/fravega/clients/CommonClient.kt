package com.fravega.clients

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.DecoderConfig.decoderConfig
import io.restassured.config.HttpClientConfig
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.response.ResponseBodyExtractionOptions
import io.restassured.specification.RequestSpecification
import org.apache.http.impl.client.AbstractHttpClient
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler
import org.apache.http.impl.client.SystemDefaultHttpClient
import org.apache.http.params.CoreConnectionPNames
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment

open class CommonClient {

    @Autowired
    protected lateinit var environment: Environment

    protected fun setBaseUriAs(host: String?): RequestSpecification {
        val httpClientConfig = HttpClientConfig.httpClientConfig()
            .httpClientFactory {
                val systemDefaultHttpClient = SystemDefaultHttpClient()
                (systemDefaultHttpClient as AbstractHttpClient).httpRequestRetryHandler = DefaultHttpRequestRetryHandler(50, true)
                systemDefaultHttpClient
            }
            .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000)
            .setParam(CoreConnectionPNames.SO_TIMEOUT, 30000)

        val customConfig = RestAssured.config()
            .httpClient(httpClientConfig)
            .decoderConfig(decoderConfig().useNoWrapForInflateDecoding(true))

        return RequestSpecBuilder()
            .setBaseUri(host)
            .addFilter(ResponseLoggingFilter())
            .addFilter(RequestLoggingFilter())
            .setConfig(customConfig)
            .build()
    }

    protected fun RequestSpecification.When(): RequestSpecification {
        return this.`when`()
    }

    protected inline fun <reified T> ResponseBodyExtractionOptions.As(): T {
        return this.`as`(T::class.java)
    }
}
