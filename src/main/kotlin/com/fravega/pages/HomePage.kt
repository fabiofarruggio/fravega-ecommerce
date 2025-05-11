package com.fravega.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.Page.WaitForSelectorOptions
import com.microsoft.playwright.options.WaitForSelectorState
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
@Lazy
class HomePage : CommonPage() {

    private val log = LoggerFactory.getLogger(HomePage::class.java)

    private val modalWrapperSelector    = "[data-test-id='modal-wrapper']"
    private val modalCloseButton        = "[data-test-id='close-modal-button']"
    private val searchInput             = "fieldset input[name='keyword']"
    private val suggestionItems         = "[data-test-id='autosuggest'] a"
    private val creditSimulatorButton   = "[data-test-id='n1-category']:has-text(\"Simulá tu Crédito\")"

    override fun setPage(page: Page) = super.setPage(page) as HomePage

    @Step("Abrir Frávega y cerrar modal postal si aparece")
    fun open(): HomePage = apply {
        log.info("Navegando a la URL base: {}", getBaseUrl())
        page.navigate(getBaseUrl())
        log.info("Página cargada, comprobando posible modal postal")
        closeGeoModalIfPresent()
    }

    @Step("Buscar «{term}»")
    fun search(term: String): HomePage = apply {
        log.info("Buscando término: '{}'", term)
        log.info("Rellenando campo de búsqueda")
        page.locator(searchInput).fill(term)
        log.info("Esperando que la página termine de cargar sugerencias")
        page.waitForLoadState()
    }

    @Step("Seleccionar segunda sugerencia")
    fun pickSuggestion() = productPage().apply {
        log.info("Seleccionando la segunda sugerencia")
        page.locator(suggestionItems).nth(1).click()
    }

    @Step("Ir a simular un crédito")
    fun goToCreditSimulation() = productPage().apply {
        log.info("Haciendo click en 'Simulá tu Crédito' con selector: {}", creditSimulatorButton)
        page.locator(creditSimulatorButton).click()
    }

    /**
     * Cierra el modal de código postal si está visible.
     * Espera hasta que el modal aparezca o expire el timeout.
     */
    private fun closeGeoModalIfPresent() {
        try {
            log.info("Esperando aparición de modal postal ({}). Timeout 10s", modalWrapperSelector)
            page.waitForSelector(
                modalWrapperSelector,
                WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10_000.0)
            )
            log.info("Modal postal visible, procediendo a cerrar con selector: {}", modalCloseButton)
            page.locator(modalCloseButton).click()
        } catch (e: Exception) {
            log.info("No apareció modal postal en 10s, continuamos sin cerrarlo")
        }
    }
}
