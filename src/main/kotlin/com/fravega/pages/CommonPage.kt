package com.fravega.pages

import com.microsoft.playwright.Page
import io.qameta.allure.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@Primary
open class CommonPage {

    @Autowired protected lateinit var environment: Environment
    @Autowired protected lateinit var homePage: HomePage
    @Autowired protected lateinit var productPage: ProductPage
    @Autowired protected lateinit var creditSimulatorPage: CreditSimulatorPage

    protected lateinit var page: Page

    open fun setPage(page: Page): CommonPage {
        this.page = page
        return this
    }

    @Step("Ir a página de inicio")
    fun homePage(): HomePage =
        homePage.setPage(page)

    @Step("Ir a página de producto")
    fun productPage(): ProductPage =
        productPage.setPage(page)

    @Step("Ir a página de simulación de crédito")
    fun creditSimulatorPage(): CreditSimulatorPage =
        creditSimulatorPage.setPage(page)

    @Step("Obtener URL base del sitio")
    fun getBaseUrl(): String =
        environment.getProperty("ui.base.url")
            ?: throw IllegalStateException("‘ui.base.url’ no definida")
}
