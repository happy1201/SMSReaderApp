package com.example.smsreaderapp.model

data class Transaction(
    val date: String,
    val sender: String,
    val amount: Double,
    val category: String,
    val mode: String,
    val type: String
)
