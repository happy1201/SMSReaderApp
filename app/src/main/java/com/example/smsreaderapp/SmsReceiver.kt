package com.example.smsreaderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.smsreaderapp.excel.ExcelManager
import com.example.smsreaderapp.model.Transaction
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

                        val isTxnMessage = TransactionalSmsFilter.isTransactionMessage(sender, body)
                        Log.d("SmsReceiver", "Is Transactional Message: $isTxnMessage")
                        if (isTxnMessage && body.contains("Rs", ignoreCase = true)) {
                            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(timestamp))
                            val amountRegex = Regex("(?i)Rs\\.?\\s?(\\d+[,.]?\\d*)")
                            val match = amountRegex.find(body)
                            val amount = match?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull() ?: continue

                            // Category & Mode classification
                            val type = if (
                                body.contains("credited", ignoreCase = true) ||
                                body.contains("received", ignoreCase = true)
                            ) "Investment" else "Expenditure"

                            val category = when {
                                body.contains("grocery", true) -> "Grocery"
                                body.contains("food", true) || body.contains("cafe", true) -> "Food"
                                body.contains("zomato", true) || body.contains("swiggy", true) -> "Food"
                                body.contains("shopping", true) || body.contains("myntra", true) -> "Shopping"
                                body.contains("fun", true) || body.contains("movie", true) -> "Fun Activities"
                                else -> "Uncategorized"
                            }

                            val mode = when {
                                body.contains("upi", true) -> "UPI"
                                body.contains("ICICI", true) || body.contains("x1234", true) -> "ICICI Credit Card"
                                body.contains("HDFC", true) || body.contains("x2882", true) -> "HDFC Credit Card"
                                else -> "Unknown"
                            }

                            val transaction = Transaction(
                                date = dateStr,
                                sender = sender,
                                amount = amount,
                                category = category,
                                mode = mode,
                                type = type
                            )

                            ExcelManager.createExcelFileIfNotExists(context)
                            ExcelManager.appendTransaction(context, transaction)

                            Log.d("SmsReceiver", "Transaction written: $transaction")
                        }
                    }

                } catch (e: Exception) {
                    Log.e("SmsReceiver", "Error reading SMS", e)
                }
            }
        }
    }
}
