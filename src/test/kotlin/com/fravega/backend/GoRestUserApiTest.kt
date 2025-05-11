package com.fravega.backend

import com.fravega.backend.steps.GoRestUserApiSteps
import com.fravega.dataproviders.UserDP
import com.fravega.clients.GoRestClient
import com.fravega.helpers.RestAssuredHelper
import com.fravega.conf.ApiConfig
import org.slf4j.LoggerFactory
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class GoRestUserApiTest {
    private val log = LoggerFactory.getLogger(GoRestUserApiTest::class.java)
    private lateinit var steps: GoRestUserApiSteps

    @BeforeClass(alwaysRun = true)
    fun setupClass() {
        val token = System.getenv("GOREST_TOKEN")
            ?: throw IllegalStateException("GOREST_TOKEN no definido")
        log.info("Token cargado y baseUrl={}", ApiConfig.baseUrl)
        val client = GoRestClient(RestAssuredHelper.specWithToken(token))
        steps = GoRestUserApiSteps(client)
    }

    @Test(
        groups            = ["backend"],
        dataProvider      = "newUsers",
        dataProviderClass = UserDP::class,
        description       = "Backend 1: Crear un nuevo usuario en GoRest",
        testName          = "CrearUsuarioGoRest"
    )
    fun createUserTest(name: String, email: String, gender: String, status: String) {
        steps.crearUsuario(name, email, gender, status)
    }

    @Test(
        groups            = ["backend"],
        dependsOnMethods  = ["createUserTest"],
        description       = "Backend 2: Listar usuarios y verificar creación",
        testName          = "ListarUsuariosGoRest"
    )
    fun listUsersTest() {
        steps.listarYVerificarUsuarios()
    }

    @Test(
        groups            = ["backend"],
        dependsOnMethods  = ["listUsersTest"],
        description       = "Backend 3: Obtener detalle de usuario específico",
        testName          = "DetalleUsuarioGoRest"
    )
    fun getUserDetailTest() {
        steps.obtenerYVerificarDetalle()
    }
}
