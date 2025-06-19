package com.example.smsreaderapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    totalSpent: Double,
    categoryMap: Map<String, Double>,
    onSendEmail: () -> Unit,
    onRefresh: () -> Unit // ✅ New parameter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ✅ Header row with refresh button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Monthly Dashboard",
                modifier = Modifier.padding(start = 4.dp)
            )
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Dashboard"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DashboardHeader(totalSpent)

        Spacer(modifier = Modifier.height(16.dp))

        ChartScreen(categoryMap)

        Spacer(modifier = Modifier.height(16.dp))

        CategoryLegend(categoryMap)

        Spacer(modifier = Modifier.height(24.dp))

        ActionButton(onClick = onSendEmail)
    }
}
