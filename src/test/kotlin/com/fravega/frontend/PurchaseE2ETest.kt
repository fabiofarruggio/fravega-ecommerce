package com.fravega.frontend

import com.fravega.conf.TestBasePlaywright
import com.fravega.dataproviders.SearchDP
import org.testng.annotations.Test

class PurchaseE2ETest : TestBasePlaywright() {

    @Test(
        groups          = ["frontend", "purchase"],
        dataProvider    = "searchTerms",
        dataProviderClass = SearchDP::class,
        description     = "Caso de Uso 1: Compra Heladera Samsung",
        testName        = "CompraHeladeraSamsung"
    )
    fun fullPurchaseFlow(term: String) {
        val checkout = commonPage
            .homePage()
            .open()
            .search(term)
            .pickSuggestion()
            .addToCart()
            .openCartModal()
            .apply {
                assert(verifyProductInCart()) { "El producto no apareció en el carrito" }
            }
    }
}
