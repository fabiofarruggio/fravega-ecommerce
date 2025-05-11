package com.fravega.dataproviders

import org.testng.annotations.DataProvider

class SearchDP {
    @DataProvider(name = "searchTerms")
    fun terms() = arrayOf(arrayOf("Heladera Samsung"))
}
