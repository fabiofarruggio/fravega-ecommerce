package com.fravega.conf

import com.fravega.pages.CommonPage
import com.fravega.utils.AllureCustomListener
import com.fravega.utils.RetryListener
import com.microsoft.playwright.*
import com.microsoft.playwright.options.Cookie
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.ITest
import org.testng.annotations.*
import org.testng.asserts.SoftAssert
import java.lang.reflect.Method

@ContextConfiguration(loader = AnnotationConfigContextLoader::class, classes = [BaseConf::class])
@Listeners(AllureCustomListener::class, RetryListener::class)
open class TestBasePlaywright : AbstractTestNGSpringContextTests(), ITest {

    @Autowired
    protected lateinit var environment: Environment

    @Autowired
    protected lateinit var commonPage: CommonPage

    protected lateinit var softAssert: SoftAssert

    protected var context: BrowserContext? = null

    lateinit var page: Page

    lateinit var auxPage: Page

    // This is for change testName when we have some test with dataProvider data
    private var testName = ThreadLocal<String>()

    companion object {
        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"
        private val LOGGER = LoggerFactory.getLogger(TestBasePlaywright::class.java)

        // Shared between all tests in this class.
        var playwright: Playwright? = null
        var browser: Browser? = null
    }

    @Parameters("headless", "customParams") // -Dheadless=true
    @BeforeClass(alwaysRun = true)
    @Throws(Exception::class)
    fun setUp(@Optional("true") headless: String?, @Optional("") customParams: String?) {
        super.springTestContextPrepareTestInstance()
        playwright = Playwright.create()
        browser = if (headless!! == "false") {
            playwright!!.chromium().launch(BrowserType.LaunchOptions().setHeadless(false))
        } // playwright!!.chromium().launch(BrowserType.LaunchOptions().setChannel("safari").setHeadless(false))
        else {
            playwright!!.chromium().launch()
        }
    }

    @BeforeMethod(alwaysRun = true)
    fun createContextAndPage(method: Method, testData: Array<Any>) {
        // This is for add dataProvider parameter to testName if apply
        val parameters = if (testData.isNotEmpty()) "[${testData[0]}]" else ""
        logInfo("Preparing test context ...")
        testName.set(method.name + parameters)
        softAssert = SoftAssert()
        context = browser!!.newContext(Browser.NewContextOptions().setUserAgent(USER_AGENT).setViewportSize(1440, 720))

        // Save videos is helpfull for debbuging purposes
        // context = browser!!.newContext(Browser.NewContextOptions().setRecordVideoDir(Paths.get("reports/videos/")).setRecordVideoSize(1280, 720))
        // Activate for tracing
        // context!!.tracing().start(Tracing.StartOptions().setScreenshots(true).setSnapshots(true))
        page = context!!.newPage()
        abortRoutesFor(page)
        commonPage.setPage(page)
    }

    private fun abortRoutesFor(page: Page) {
        page.route("https://analytics.google.com/**") { route -> route.abort() }
        page.route("https://www.google-analytics.com/**") { route -> route.abort() }
    }

    @AfterMethod(alwaysRun = true)
    fun closeContext() {
        // Activate for tracing
        // context!!.tracing().stop(Tracing.StopOptions().setPath(Paths.get("trace.zip")))
        try { // Sometimes, PW close context automatically
            context!!.close()
        } catch (e: Exception) {
            logInfo("Close Context Exception - Context Already Close")
        }
    }

    @AfterClass(alwaysRun = true)
    fun tearDown() {
        playwright!!.close()
        // logInfo("Suite execution FINISHED.")
    }

    protected fun logInfo(message: String?) {
        LOGGER.info(message)
    }

    // This is for change testName when we have some test with dataProvider data
    override fun getTestName(): String {
        return if (testName.get() != null) testName.get() else "Custom sub-suite"
    }

    protected fun addCookieToContext(name: String, value: String, domain: String, path: String) {
        val cookie = Cookie(name, value)
            .setDomain(domain)
            .setPath(path)
        context?.addCookies(listOf(cookie))
    }
}
