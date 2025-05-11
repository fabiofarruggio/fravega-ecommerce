package com.fravega.frontend

import com.fravega.dataproviders.CreditDP
import com.fravega.conf.TestBasePlaywright
import org.testng.annotations.Test

class CreditRejectionTest : TestBasePlaywright() {

    @Test(
        groups           = ["frontend", "credit"],
        dataProvider     = "creditData",
        dataProviderClass= CreditDP::class,
        description      = "Caso de Uso 2: Simulación de crédito rechazado con DNI no elegible",
        testName         = "SimulacionCreditoRechazado"
    )
    fun simulateCreditRejected(dni: String, gender: String, name:String) {
        commonPage
            .homePage()
            .open()
            .goToCreditSimulation()
            .creditSimulatorPage()
            .startSimulation()
            .fillDni(dni)
            .selectGender(gender)
            .clickValidate()
            .expectName(name)
            .clickNext()
            .expectGenericRejection()
    }
}
