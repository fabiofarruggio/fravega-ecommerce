package com.fravega.conf

import com.fravega.utils.AllureCustomListener
import com.fravega.utils.RetryListener
import com.fravega.utils.loggerFor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.ITestContext
import org.testng.annotations.*
import org.testng.asserts.SoftAssert

@ContextConfiguration(loader = AnnotationConfigContextLoader::class, classes = [BaseConf::class])
@Listeners(AllureCustomListener::class, RetryListener::class)
open class TestBase : AbstractTestNGSpringContextTests() {

    companion object {
        private val LOGGER = loggerFor<TestBase>()
    }

    @Autowired
    protected lateinit var environment: Environment

    protected lateinit var softAssert: SoftAssert

    @Parameters("customParams")
    @BeforeSuite(alwaysRun = true)
    @Throws(Exception::class)
    open fun setUpSuite(@Optional("") customParams: String?) {
        System.setProperty("customParams", customParams)
        super.springTestContextPrepareTestInstance()
    }

    @BeforeMethod(alwaysRun = true)
    protected fun testMethodSetup() {
        softAssert = SoftAssert()
    }

    @AfterMethod(alwaysRun = true)
    protected fun testMethodsTearDown() {
        // Nothing to do so far ...
    }

    @Parameters("submitReport")
    @AfterSuite(alwaysRun = true)
    protected fun submitReport(@Optional("false") submitReport: String?, context: ITestContext) {
        LOGGER.info("Suite execution FINISHED.")
    }

    private fun isAnyFailedTest(context: ITestContext): Boolean {
        return context.failedTests.size() > 0
    }
}
