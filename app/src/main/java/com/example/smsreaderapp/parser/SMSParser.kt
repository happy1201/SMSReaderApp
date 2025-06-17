package com.example.smsreaderapp.parser


import com.example.smsreaderapp.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

object SmsParser {
    private val amountRegex = Regex("(?i)Rs\\.?\\s?(\\d+[,.]?\\d*)")

    fun parseSms(body: String, sender: String, timestamp: Long): Transaction? {
        val match = amountRegex.find(body)
        val amount = match?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull() ?: return null

        val mode = when {
            sender.contains("ICICI", true) -> "ICICI Credit Card"
            sender.contains("HDFC", true) -> "HDFC Credit Card"
            body.contains("UPI", true) -> "UPI"
            else -> "Other"
        }

        val category = when {
            body.contains("zomato", true) || body.contains("swiggy", true) -> "Food"
            body.contains("amazon", true) || body.contains("flipkart", true) -> "Shopping"
            body.contains("more", true) || body.contains("dmart", true) -> "Grocery"
            body.contains("movie", true) || body.contains("ticket", true) -> "Fun Activities"
            else -> "Other"
        }

        val type = if (category == "Other") "Investments" else "Expenditures"
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))

        return Transaction(date, sender, amount, category, mode, type)
    }
}
