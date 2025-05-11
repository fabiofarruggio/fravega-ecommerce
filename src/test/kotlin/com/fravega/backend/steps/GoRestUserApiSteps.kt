// src/main/kotlin/com/fravega/backend/steps/GoRestUserApiSteps.kt
package com.fravega.backend.steps

import com.fravega.clients.GoRestClient
import com.fravega.model.api.UserRequest
import io.qameta.allure.Step
import org.testng.Assert

class GoRestUserApiSteps(private val client: GoRestClient) {

    private var createdUserId: Int = -1

    @Step("Crear usuario: name=`{name}`, email=`{email}`, gender=`{gender}`, status=`{status}`")
    fun crearUsuario(name: String, email: String, gender: String, status: String) {
        val req = UserRequest(name, email, gender, status)
        val resp = client.createUser(req)
        createdUserId = resp.id
        Assert.assertTrue(createdUserId > 0, "El ID debería ser mayor que 0")
    }

    @Step("Listar usuarios (perPage={perPage}) y verificar que el usuario exista")
    fun listarYVerificarUsuarios(perPage: Int = 100) {
        val usuarios = client.listUsers(perPage = perPage)
        val found = usuarios.any { it.id == createdUserId }
        Assert.assertTrue(found, "Usuario $createdUserId no apareció en la lista")
    }

    @Step("Obtener detalle del usuario y validar el ID")
    fun obtenerYVerificarDetalle() {
        val detail = client.getUser(createdUserId)
        Assert.assertEquals(detail.id, createdUserId, "El ID devuelto no coincide con el esperado")
    }
}
