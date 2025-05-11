package com.fravega.utils

import com.fravega.conf.TestBasePlaywright
import com.microsoft.playwright.Page
import io.qameta.allure.Allure
import io.qameta.allure.Attachment
import io.qameta.allure.listener.TestLifecycleListener
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.testng.ITestContext
import org.testng.ITestResult
import org.testng.TestListenerAdapter

class AllureCustomListener : TestListenerAdapter(), TestLifecycleListener {

    companion object {
        private val LOGGER = loggerFor<AllureCustomListener>()
    }

    private var page: Page? = null

    @Attachment(value = "Page screenshot", type = "image/png")
    fun saveScreenshotPNG(page: Page): ByteArray {
        page.waitForTimeout(3000.0)
        return page.screenshot(Page.ScreenshotOptions().setFullPage(true))
    }

    // This block is commented since it can be done at TestBasePlaywright class ...
    override fun onTestStart(result: ITestResult?) {
        val parameters = if (result!!.parameters.isNotEmpty()) "[${result.parameters[0]}]" else ""
        LOGGER.info("TEST-START >> ${result!!.method.methodName}$parameters")
    }

    @Synchronized
    override fun onStart(testContext: ITestContext?) {
        LOGGER.info("SUITE-START.")
    }

    @Synchronized
    override fun onFinish(testContext: ITestContext?) {
        LOGGER.info("SUITE-END.")
    }

    @Synchronized
    override fun onTestSuccess(result: ITestResult?) {
        LOGGER.info("TEST-END > SUCCESSFULLY.")
    }

    @Synchronized
    override fun onTestFailure(result: ITestResult) {
        LOGGER.info("TEST-END > WITH FAILURE.")
        if ((result.instance as? TestBasePlaywright) != null) {
            page = (result.instance as TestBasePlaywright).page
            saveScreenshotPNG(page!!)
            Thread.sleep(3000)
            Allure.getLifecycle().updateTestCase { testResult ->
                testResult.status = Status.FAILED
                val stepResult = StepResult()
                stepResult.status = Status.FAILED
            }
        }
    }

    @Synchronized
    override fun onTestSkipped(result: ITestResult) {
        LOGGER.info("TEST-END > SKIPPED.")
        if ((result.instance as? TestBasePlaywright) != null) {
            page = (result.instance as TestBasePlaywright).page
            saveScreenshotPNG(page!!)
            Thread.sleep(3000)
            Allure.getLifecycle().updateTestCase { testResult ->
                testResult.status = Status.SKIPPED
                val stepResult = StepResult()
                stepResult.status = Status.SKIPPED
            }
        }
    }
}
