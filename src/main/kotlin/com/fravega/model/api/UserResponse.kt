package com.fravega.model.api

data class UserResponse(
    val id:     Int,
    val name:   String,
    val email:  String,
    val gender: String,
    val status: String
)