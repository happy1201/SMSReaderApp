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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smsreaderapp.email.EmailUtil
import com.example.smsreaderapp.ui.theme.SMSReaderAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SMSReaderAppTheme {

                // 🔐 Step 1: Manage SMS permission
                val smsPermission = Manifest.permission.RECEIVE_SMS
                val permissionGranted = remember { mutableStateOf(false) }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    permissionGranted.value = isGranted
                    Log.d("Permission", "RECEIVE_SMS granted? $isGranted")
                }

                // 🔃 Step 2: Launch permission request once on startup
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(smsPermission)
                }

                // 🧪 Step 3: Test UI to reflect state
                val latestSms = remember { mutableStateOf("Waiting for SMS...") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = if (permissionGranted.value) latestSms.value else "Permission not granted",
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                // New Button to Send Mail
                val context = LocalContext.current
                MainScreen(context)

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

