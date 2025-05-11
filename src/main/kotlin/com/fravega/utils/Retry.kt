package com.fravega.utils

import org.slf4j.LoggerFactory
import org.testng.IRetryAnalyzer
import org.testng.ITestResult
import org.testng.ITestNGMethod
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Retry : IRetryAnalyzer {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Retry::class.java)
        private const val MAX_RETRY_COUNT = 1

        // Mapa global: cuenta de reintentos por método
        private val retryCounts = ConcurrentHashMap<ITestNGMethod, AtomicInteger>()
    }

    override fun retry(result: ITestResult): Boolean {
        // 1) Solo retry si falló
        if (result.status != ITestResult.FAILURE) {
            LOGGER.debug("No retry for {} because status is {}", result.method.methodName, result.status)
            return false
        }

        // 2) Incrementa contador atómico por método
        val counter = retryCounts.computeIfAbsent(result.method) { AtomicInteger(0) }
        val attempt = counter.incrementAndGet()
        return if (attempt <= MAX_RETRY_COUNT) {
            LOGGER.info("RETRY #{} for test: {} on thread: {}", attempt, result.method.methodName, Thread.currentThread().name)
            true
        } else {
            LOGGER.info("Max retries ({}) reached for {}", MAX_RETRY_COUNT, result.method.methodName)
            false
        }
    }
}
