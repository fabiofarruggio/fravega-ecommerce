package com.fravega.utils

import io.qameta.allure.Allure

class AllureLogger {

    companion object {
        private val LOGGER = loggerFor<AllureLogger>()
    }

    fun log(message: String) {
        LOGGER.info(message)
        Allure.step(message)
    }
}