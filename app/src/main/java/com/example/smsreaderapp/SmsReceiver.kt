// 📁 ExcelManager.kt (already created, no change needed)
// Ensure it exists at: com/example/smsreaderapp/excel/ExcelManager.kt

// 📁 Modified: SmsReceiver.kt
package com.example.smsreaderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.smsreaderapp.excel.ExcelManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val messages = mutableListOf<SmsMessage>()

            if (bundle != null && context != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<*>
                    val format = bundle.getString("format")

                    for (pdu in pdus) {
                        val msg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            SmsMessage.createFromPdu(pdu as ByteArray, format)
                        } else {
                            SmsMessage.createFromPdu(pdu as ByteArray)
                        }
                        messages.add(msg)
                    }

                    for (sms in messages) {
                        val sender = sms.displayOriginatingAddress
                        val body = sms.displayMessageBody
                        val timestamp = sms.timestampMillis

                        Log.d("SmsReceiver", "SMS from: $sender")
                        Log.d("SmsReceiver", "Message: $body")

                        // Parse the SMS if it contains financial transaction info
                        if (body.contains("Rs.", ignoreCase = true)) {
                            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(timestamp))
                            val amountRegex = Regex("(?i)Rs\\.?\\s?(\\d+[,.]?\\d*)")
                            val match = amountRegex.find(body)
                            val amount = match?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull() ?: 0.0

                            val category = "Transaction" // You can use NLP or keyword matching later
                            val description = sender.take(20) // You can extract better info later
                            val bank = if (body.contains("HDFC", true)) "HDFC" else if (body.contains("ICICI", true)) "ICICI" else "Unknown"

                            Log.d("SmsReceiver", "Amount: $amount")
                            Log.d("SmsReceiver", "Date: $dateStr")
                            Log.d("SmsReceiver", "Category: $category")
                            Log.d("SmsReceiver", "Description: $description")
                            Log.d("SmsReceiver", "Bank: $bank")

                            ExcelManager.createExcelFileIfNotExists(context)
                            ExcelManager.addExpense(
                                context = context,
                                date = dateStr,
                                category = category,
                                description = description,
                                amount = amount,
                                bank = bank
                            )
                        }
                    }

                } catch (e: Exception) {
                    Log.e("SmsReceiver", "Error reading SMS", e)
                }
            }
        }
    }
}
