package com.example.aupadelapp.model

data class User(
    val userId: String,
    val userName:String,
    val email: String,
    val password: String,
    val role: String,
    val phoneNumber: String,
    val gender: String =""
)
