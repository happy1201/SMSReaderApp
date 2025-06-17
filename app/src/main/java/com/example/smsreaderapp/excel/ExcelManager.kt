package com.example.smsreaderapp.excel

import android.content.Context
import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*


object ExcelManager {

    private const val FILE_NAME = "Expense_Tracker_Khushal.xlsx"
    private const val SHEET_NAME_EXPENSES = "Expenses"

    fun getExcelFile(context: Context): File {
        Log.d("ExcelManager", "getExcelFile called..")
        return File(context.getExternalFilesDir(null), FILE_NAME)
    }

    fun createExcelFileIfNotExists(context: Context) {
        Log.d("ExcelManager", "createExcelFileIfNotExists called..")

        val file = getExcelFile(context)
        if (!file.exists()) {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet(SHEET_NAME_EXPENSES)
            val header = sheet.createRow(0)

            header.createCell(0).setCellValue("Date")
            header.createCell(1).setCellValue("Category")
            header.createCell(2).setCellValue("Description")
            header.createCell(3).setCellValue("Amount")
            header.createCell(4).setCellValue("Bank")

            FileOutputStream(file).use { workbook.write(it) }
            workbook.close()
        }
    }

    fun addExpense(
        context: Context,
        date: String,
        category: String,
        description: String,
        amount: Double,
        bank: String
    ) {
        val file = getExcelFile(context)

        val fis = FileInputStream(file)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheet(SHEET_NAME_EXPENSES) ?: workbook.createSheet(
            SHEET_NAME_EXPENSES
        )
        fis.close()

        val rowNum = sheet.lastRowNum + 1
        val row = sheet.createRow(rowNum)

        row.createCell(0).setCellValue(date)
        row.createCell(1).setCellValue(category)
        row.createCell(2).setCellValue(description)
        row.createCell(3).setCellValue(amount)
        row.createCell(4).setCellValue(bank)

        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()
    }
}
