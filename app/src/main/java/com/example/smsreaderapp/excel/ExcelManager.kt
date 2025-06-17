package com.example.smsreaderapp.excel

import android.content.Context
import android.util.Log
import com.example.smsreaderapp.model.Transaction
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

object ExcelManager {

    private const val FILE_NAME = "Expense_Tracker_Khushal.xlsx"
    private const val SHEET_NAME_EXPENSES = "Transactions"

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
            header.createCell(1).setCellValue("Sender")
            header.createCell(2).setCellValue("Amount")
            header.createCell(3).setCellValue("Category")
            header.createCell(4).setCellValue("Mode")
            header.createCell(5).setCellValue("Type")

            FileOutputStream(file).use { workbook.write(it) }
            workbook.close()
        }
    }

    fun appendTransaction(context: Context, transaction: Transaction) {
        Log.d("ExcelManager", "appendTransaction called..")

        val file = getExcelFile(context)

        if (!file.exists()) {
            createExcelFileIfNotExists(context)
        }

        val fis = FileInputStream(file)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheet(SHEET_NAME_EXPENSES) ?: workbook.createSheet(SHEET_NAME_EXPENSES)
        fis.close()

        val rowNum = sheet.lastRowNum + 1
        val row = sheet.createRow(rowNum)

        row.createCell(0).setCellValue(transaction.date)
        row.createCell(1).setCellValue(transaction.sender)
        row.createCell(2).setCellValue(transaction.amount)
        row.createCell(3).setCellValue(transaction.category)
        row.createCell(4).setCellValue(transaction.mode)
        row.createCell(5).setCellValue(transaction.type)

        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()

        Log.d("ExcelManager", "Transaction saved to Excel at row $rowNum")
    }
}
