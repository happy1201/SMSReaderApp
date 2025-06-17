package com.example.smsreaderapp.email

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File


class EmailUtil {

    fun sendEmailWithExcelAttachment(context: Context) {
        val file = File(context.getExternalFilesDir(null), "Expense_Tracker_Khushal.xlsx")

        if (!file.exists()) {
            Log.e("SendMail", "Excel file not found!")
            return
        }

        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // Ensure it matches authority in Manifest
            file
        )

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("khushalgilra@email.com")) // ✅ Set your target email here
            putExtra(Intent.EXTRA_SUBJECT, "Khushal's Monthly Expense Sheet")
            putExtra(Intent.EXTRA_TEXT, "Please find the attached expense sheet.")
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        Log.d("SendMail", "Sending email to khushalgilra@email.com")

        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
    }
}