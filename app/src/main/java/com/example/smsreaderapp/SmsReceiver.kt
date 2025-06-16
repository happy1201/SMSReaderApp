package com.example.smsreaderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.os.Build
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val messages = mutableListOf<SmsMessage>()

            if (bundle != null) {
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

                        Log.d("SmsReceiver", "SMS received from: $sender")
                        Log.d("SmsReceiver", "Message: $body")
                        Log.d("SmsReceiver", "Timestamp: $timestamp")
                        // TODO: Pass to ViewModel or UI layer in next steps
                    }

                } catch (e: Exception) {
                    Log.e("SmsReceiver", "Error reading SMS", e)
                }
            }
        }
    }
}
