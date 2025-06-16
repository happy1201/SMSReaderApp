//package com.example.smsreaderapp
//
//import android.content.Context
//import android.util.Log
//import org.apache.poi.ss.usermodel.*
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ExcelManager(private val context: Context) {
//    private val TAG = "ExcelManager"
//    private val EXCEL_FILE_NAME = "expense_tracker.xlsx"
//    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
//
//    init {
//        createExcelIfNotExists()
//    }
//
//    private fun createExcelIfNotExists() {
//        val file = File(context.getExternalFilesDir(null), EXCEL_FILE_NAME)
//        if (!file.exists()) {
//            try {
//                val workbook = XSSFWorkbook()
//
//                // Create Daily Expenses Sheet
//                val dailySheet = workbook.createSheet("Daily Expenses")
//                createDailyExpensesHeaders(dailySheet)
//
//                // Create Investments Sheet
//                val investmentsSheet = workbook.createSheet("Investments")
//                createInvestmentsHeaders(investmentsSheet)
//
//                // Create Monthly Summary Sheet
//                val monthlySheet = workbook.createSheet("Monthly Summary")
//                createMonthlySummaryHeaders(monthlySheet)
//
//                // Save the workbook
//                val outputStream = FileOutputStream(file)
//                workbook.write(outputStream)
//                workbook.close()
//                outputStream.close()
//
//                Log.i(TAG, "Excel file created successfully")
//            } catch (e: Exception) {
//                Log.e(TAG, "Error creating Excel file", e)
//            }
//        }
//    }
//
//    private fun createDailyExpensesHeaders(sheet: Sheet) {
//        val headerRow = sheet.createRow(0)
//        val headers = arrayOf(
//            "Date", "Amount", "Merchant", "Category", "Description", "Payment Method"
//        )
//        headers.forEachIndexed { index, header ->
//            headerRow.createCell(index).setCellValue(header)
//        }
//    }
//
//    private fun createInvestmentsHeaders(sheet: Sheet) {
//        val headerRow = sheet.createRow(0)
//        val headers = arrayOf(
//            "Date", "Amount", "Investment Type", "Platform", "Description", "Status"
//        )
//        headers.forEachIndexed { index, header ->
//            headerRow.createCell(index).setCellValue(header)
//        }
//    }
//
//    private fun createMonthlySummaryHeaders(sheet: Sheet) {
//        val headerRow = sheet.createRow(0)
//        val headers = arrayOf(
//            "Month", "Total Expenses", "Total Investments", "Savings", "Top Categories"
//        )
//        headers.forEachIndexed { index, header ->
//            headerRow.createCell(index).setCellValue(header)
//        }
//    }
//
//    fun addTransaction(amount: String, merchant: String, date: Long) {
//        try {
//            val file = File(context.getExternalFilesDir(null), EXCEL_FILE_NAME)
//            val inputStream = FileInputStream(file)
//            val workbook = WorkbookFactory.create(inputStream)
//            val sheet = workbook.getSheet("Daily Expenses")
//
//            val row = sheet.createRow(sheet.lastRowNum + 1)
//            row.createCell(0).setCellValue(dateFormat.format(Date(date)))
//            row.createCell(1).setCellValue(amount.toDoubleOrNull() ?: 0.0)
//            row.createCell(2).setCellValue(merchant)
//            row.createCell(3).setCellValue(determineCategory(merchant, amount))
//
//            val outputStream = FileOutputStream(file)
//            workbook.write(outputStream)
//            workbook.close()
//            outputStream.close()
//
//            Log.i(TAG, "Transaction added successfully")
//        } catch (e: Exception) {
//            Log.e(TAG, "Error adding transaction", e)
//        }
//    }
//
//    private fun determineCategory(merchant: String, amount: String): String {
//        // Basic category determination logic
//        return when {
//            merchant.contains("GROCERY", ignoreCase = true) -> "Groceries"
//            merchant.contains("RESTAURANT", ignoreCase = true) -> "Dining"
//            merchant.contains("FUEL", ignoreCase = true) -> "Transportation"
//            merchant.contains("MEDICAL", ignoreCase = true) -> "Healthcare"
//            else -> "Other"
//        }
//    }
//}