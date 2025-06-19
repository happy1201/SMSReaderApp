package com.example.smsreaderapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData

@Composable
fun ChartScreen(categoryMap: Map<String, Double>) {
    val colors = listOf(
        Color(0xFFEF5350), // Grocery
        Color(0xFF42A5F5), // Food
        Color(0xFFAB47BC), // Fun
        Color(0xFF66BB6A)  // Shopping
    )

    val total = categoryMap.values.sum()

    val slices = categoryMap.entries.mapIndexed { index, entry ->
        PieChartData.Slice(
            value = entry.value.toFloat(),
            color = colors[index % colors.size]
        )
    }

    val pieChartData = PieChartData(slices)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spending Breakdown",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        PieChart(
            pieChartData = pieChartData,
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp)
        )
    }
}

