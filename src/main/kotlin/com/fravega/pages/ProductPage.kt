package com.fravega.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.WaitForSelectorState
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
@Lazy
class ProductPage : CommonPage() {

    private val log = LoggerFactory.getLogger(ProductPage::class.java)

    private val addToCartBtn        = "button:has-text(\"Agregar al carrito\")"
    private val headerCartButton    = "button[data-test-id='button-cart']"
    private val cartContainer = "div[class*='user-cart-btn-container']"

    override fun setPage(page: Page) = super.setPage(page) as ProductPage

    @Step("Agregar producto al carrito")
    fun addToCart(): ProductPage = apply {
        log.info("Buscando botón 'Agregar al carrito' con selector: {}", addToCartBtn)
        val addBtn = page.locator(addToCartBtn).nth(1)
        log.info("Número de botones encontrados: {}", page.locator(addToCartBtn).count())
        log.info("¿Visible? ${addBtn.isVisible}")
        log.info("Haciendo click en el botón 'Agregar al carrito'")
        addBtn.click()

        log.info("Esperando que aparezca el toast de confirmación")
        page.waitForSelector(
            ".Toastify__toast",
            Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE)
        )
        log.info("Toast visible, ahora esperando a que desaparezca")
        page.waitForSelector(
            ".Toastify__toast",
            Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED)
        )
        log.info("Toast desapareció, back-end debería haber procesado la acción")
        log.info("Esperando 1.5s extra antes de abrir el carrito")
        page.waitForTimeout(1500.0)
    }

    @Step("Abrir modal del carrito")
    fun openCartModal(): ProductPage = apply {
        log.info("Click en el botón del carrito")
        page.locator(headerCartButton).first().click()

        // Ahora esperamos a que el atributo data-tooltip-open cambie a "true"
        log.info("Esperando que data-tooltip-open=\"true\" en el contenedor del carrito")
        page.waitForSelector(
            "$cartContainer[data-tooltip-open=\"true\"]",
            Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED)
        )

        log.info("Modal del carrito abierto correctamente")
    }

    @Step("Verificar que el producto esté en el carrito")
    fun verifyProductInCart(): Boolean {
        log.info("Contando productos dentro del modal del carrito")
        // Ya podemos acceder a los productos, porque el contenedor ya tiene data-tooltip-open="true"
        val count = page.locator("$cartContainer[data-tooltip-open=\"true\"] [data-test-id='cart-product']").count()
        log.info("Productos en el carrito: {}", count)
        return count > 0
    }
}
