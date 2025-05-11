package com.fravega.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Lazy
import com.microsoft.playwright.options.WaitForSelectorState

@Component
@Lazy
class CreditSimulatorPage : CommonPage() {

    private val log = LoggerFactory.getLogger(CreditSimulatorPage::class.java)

    private val startBtn       = "button:has-text(\"Empezar\")"
    private val dniInput       = "#dni"
    private val genderSelect   = "#select-example_input"
    private val validateBtn    = "[data-testid='bumeran-button'] >> text=Validar"
    private val nextBtn        = "[data-testid='bumeran-button'] >> text=Siguiente"
    private val rejectionMsgTitle  = "div[role='heading']:has-text(\"No pudimos simular tu crédito\")"
    private val rejectionMsgBody   = "p:has-text(\"En este momento no podemos ofrecerte un crédito en Frávega.\")"
    private val errorIcon          = "svg[data-testid='iconoError']"
    private val backHomeBtn        = "[data-testid='bumeran-button']:has-text(\"Volver al inicio\")"
    private val loadingSpinner     = "svg[color='neutral']"


    override fun setPage(page: Page) = super.setPage(page) as CreditSimulatorPage

    @Step("Comenzar simulación")
    fun startSimulation(): CreditSimulatorPage = apply {
        log.info("Click en botón 'Empezar'")
        page.locator(startBtn).click()
    }

    @Step("Completar DNI: {dni}")
    fun fillDni(dni: String): CreditSimulatorPage = apply {
        log.info("Llenando DNI con valor: {}", dni)
        page.locator(dniInput).fill(dni)
    }

    @Step("Seleccionar género: {gender}")
    fun selectGender(gender: String): CreditSimulatorPage = apply {
        log.info("Abriendo dropdown de género")
        page.locator(genderSelect).click()
        log.info("Seleccionando género: {}", gender)
        page.locator("#$gender").click()
    }

    @Step("Click en Validar y esperar resultados")
    fun clickValidate(): CreditSimulatorPage = apply {
        val btn = page.locator(validateBtn)
        // Espera a que el botón sea visible
        btn.waitFor(Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE))
        log.info("Click en botón 'Validar'")
        btn.click()

        // Ahora esperamos al spinner con page.waitForSelector
        log.info("Esperando a que aparezca el spinner de carga")
        page.waitForSelector(
            loadingSpinner,
            Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE)
        )
        log.info("Spinner visible, esperando a que desaparezca")
        page.waitForSelector(
            loadingSpinner,
            Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED)
        )
        log.info("Spinner desapareció, simulación procesada")
    }

    @Step("Click en Siguiente")
    fun clickNext(): CreditSimulatorPage = apply {
        log.info("Click en botón Siguiente")
        page.locator(nextBtn).click()
    }

    @Step("Verificar que aparezca el nombre «{name}»")
    fun expectName(name: String): CreditSimulatorPage = apply {
        log.info("Verificando que el nombre '{}' esté visible", name)
        require(
            page.locator("p:has-text(\"$name\")").isVisible
        ) { "Se esperaba ver el nombre '$name', pero no apareció." }
        log.info("Nombre '{}' verificado correctamente", name)
    }

    @Step("Verificar mensaje de crédito rechazado completo")
    fun expectGenericRejection(): CreditSimulatorPage = apply {
        // Esperamos a que aparezca el icono de error
        log.info("Esperando al icono de error")
        val icon = page.locator(errorIcon)
        icon.waitFor(Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(10_000.0))
        require(icon.isVisible) {
            "No se encontró el icono de error (iconoError)."
        }

        // Título principal
        log.info("Verificando título de rechazo")
        val title = page.locator(rejectionMsgTitle)
        title.waitFor(Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE))
        require(title.isVisible) {
            "No apareció el título de rechazo “No pudimos simular tu crédito”."
        }

        // Párrafo descriptivo
        log.info("Verificando texto descriptivo")
        val body = page.locator(rejectionMsgBody)
        body.waitFor(Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE))
        require(body.isVisible) {
            "No apareció el texto “En este momento no podemos ofrecerte un crédito en Frávega.”."
        }

        // Botón Volver al inicio
        log.info("Verificando botón 'Volver al inicio'")
        val back = page.locator(backHomeBtn)
        back.waitFor(Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE))
        require(back.isVisible) {
            "No se encontró el botón “Volver al inicio”."
        }
    }

}
