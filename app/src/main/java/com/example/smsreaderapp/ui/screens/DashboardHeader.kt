package com.example.smsreaderapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DashboardHeader(totalSpent: Double) {
    Column {
        Text(
            text = "Hello Khushal 👋",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your total spending this month",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = "₹%.2f".format(totalSpent),
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFFe53935),
            fontWeight = FontWeight.Bold
        )
    }
}
