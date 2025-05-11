package com.fravega.dataproviders

import org.testng.annotations.DataProvider

object CreditDP {

    @JvmStatic
    @DataProvider(name = "creditData")
    fun provideCreditData(): Array<Array<Any>> {
        return arrayOf(
            arrayOf("35381125", "Masculino", "FARRUGGIO FABIO EZEQUIEL"),    // DNI no elegible => rechazo
            // se podría agregar más DNIs para otros escenarios:
            // arrayOf("40123456", "Femenino", "MESSI LEONEL ANDRES"),
            // arrayOf("50123456", "No Binario", "GINOBILLI EMANUEL")
        )
    }
}
