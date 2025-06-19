package com.example.smsreaderapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smsreaderapp.email.EmailUtil
import com.example.smsreaderapp.ui.screens.DashboardScreen
import com.example.smsreaderapp.ui.theme.SMSReaderAppTheme
import com.example.smsreaderapp.excel.ExcelManager


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val excelManager = ExcelManager(applicationContext)
        var categoryMap = excelManager.getCurrentMonthSpendByCategory()
        var totalSpent = categoryMap.values.sum()

        Log.d("MainActivity", "Total Spent: $totalSpent")
        setContent {
            MaterialTheme {
                DashboardScreen(
                    totalSpent = totalSpent,
                    categoryMap = categoryMap,
                    onSendEmail = {
                        // TODO: Trigger email logic here
                        val emailUtil = EmailUtil()
                        emailUtil.sendEmailWithExcelAttachment(applicationContext)
                    },
                    onRefresh = {
                        categoryMap = excelManager.getCurrentMonthSpendByCategory()
                        totalSpent = categoryMap.values.sum()
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    SMSReaderAppTheme {
        Greeting("Android")
    }
}

@Composable
fun MainScreen(context: Context) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text("📊 Expense Tracker is Ready!")
        Spacer(modifier = Modifier.height(24.dp))
        val emailUtil = remember { EmailUtil() }

        Button(onClick = {
            emailUtil.sendEmailWithExcelAttachment(context)
        }) {
            Text("Send Mail")
        }
    }
}

