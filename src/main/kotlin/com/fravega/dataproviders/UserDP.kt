package com.fravega.dataproviders

import org.testng.annotations.DataProvider

object UserDP {
    @JvmStatic
    @DataProvider(name = "newUsers")
    fun newUsers(): Array<Array<Any>> = arrayOf(
        arrayOf(
            "Test User",
            "testuser_${System.currentTimeMillis()}@example.com",
            "male",
            "active"
        )
    )
}
