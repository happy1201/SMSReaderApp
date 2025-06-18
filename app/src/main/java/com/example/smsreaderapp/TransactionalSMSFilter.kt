package com.example.smsreaderapp

object TransactionalSmsFilter {

    private val knownBankSenders = listOf(
        "HDFCBK", "ICICIB", "SBIINB", "AXISBNK", "KOTAKB", "PNBSMS", "IDFCSR",
        "YESBNK", "BOISMS", "IDFCFB", "CITIBK", "PAYTMB", "BHIMUPI", "GOOGPAY", "PHONEPE"
    )

    private val transactionKeywords = listOf(
        "debited", "credited", "txn", "transaction", "transfer", "spent",
        "paid", "received", "withdrawn", "deducted", "A/C", "account", "Ref", "utr"
    )

    private val spamIndicators = listOf(
        "offer", "discount", "sale", "deal", "limited time", "cashback", "coupon",
        "save", "buy now", "only today", "promo", "code"
    )

    private val transactionRegex = Regex("(?i)(debited|credited|withdrawn|spent|paid).*?Rs\\.?\\s?([0-9,]+(?:\\.\\d{1,2})?)")

    fun isFromTrustedSender(sender: String): Boolean {
        return knownBankSenders.any { sender.uppercase().contains(it) }
    }

    fun containsTransactionKeywords(message: String): Boolean {
        val msg = message.lowercase()
        return transactionKeywords.any { msg.contains(it) }
    }

    fun containsSpamIndicators(message: String): Boolean {
        val msg = message.lowercase()
        return spamIndicators.any { msg.contains(it) }
    }

    fun matchesTransactionPattern(message: String): Boolean {
        return transactionRegex.containsMatchIn(message)
    }

    fun calculateTransactionScore(sender: String, message: String): Int {
        var score = 0
        val msg = message.lowercase()

        if (isFromTrustedSender(sender)) score += 3
        if (containsTransactionKeywords(msg)) score += 2
        if (matchesTransactionPattern(msg)) score += 3
        if (containsSpamIndicators(msg)) score -= 2
        if (msg.contains("ref") || msg.contains("utr")) score += 1

        return score
    }

    fun isTransactionMessage(sender: String, message: String): Boolean {
        return isFromTrustedSender(sender) &&
                containsTransactionKeywords(message) &&
                matchesTransactionPattern(message) &&
                calculateTransactionScore(sender, message) >= 4
    }
}
