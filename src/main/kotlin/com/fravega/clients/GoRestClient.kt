package com.fravega.clients

import com.fravega.model.api.UserRequest
import com.fravega.model.api.UserResponse
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification

class GoRestClient(private val spec: RequestSpecification) {

    fun createUser(request: UserRequest): UserResponse =
        RestAssured.given()
            .spec(spec)
            .body(request)
            .post("/users")
            .then()
            .statusCode(201)
            .extract()
            .`as`(UserResponse::class.java)

    fun listUsers(page: Int = 1, perPage: Int = 20): List<UserResponse> =
        RestAssured.given()
            .spec(spec)
            .param("page", page)
            .param("per_page", perPage)
            .get("/users")
            .then()
            .statusCode(200)
            .extract()
            .`as`(Array<UserResponse>::class.java)
            .toList()

    fun getUser(id: Int): UserResponse =
        RestAssured.given()
            .spec(spec)
            .pathParam("id", id)
            .get("/users/{id}")
            .then()
            .statusCode(200)
            .extract()
            .`as`(UserResponse::class.java)
}
