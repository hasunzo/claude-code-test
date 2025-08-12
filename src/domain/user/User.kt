package com.example.domain.user

data class User(
    val id: Long,
    val name: String,
    val email: String
) {
    fun isValid(): Boolean {
        return name.isNotBlank() && email.contains("@")
    }
}
