package com.example.smsreaderapp.excel

import android.content.Context
import android.util.Log
import com.example.smsreaderapp.model.Transaction
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.YearMonth

class ExcelManager(private val context: Context) {

    private val FILE_NAME = "Expense_Tracker_Khushal.xlsx"
    private val SHEET_NAME_EXPENSES = "Transactions"

    private val excelFile: File
        get() = File(context.getExternalFilesDir(null), FILE_NAME)

    fun createExcelFileIfNotExists() {
        Log.d("ExcelManager", "createExcelFileIfNotExists called..")

        if (!excelFile.exists()) {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet(SHEET_NAME_EXPENSES)
            val header = sheet.createRow(0)

            header.createCell(0).setCellValue("Date")
            header.createCell(1).setCellValue("Sender")
            header.createCell(2).setCellValue("Amount")
            header.createCell(3).setCellValue("Category")
            header.createCell(4).setCellValue("Mode")
            header.createCell(5).setCellValue("Type")

            FileOutputStream(excelFile).use { workbook.write(it) }
            workbook.close()
        }
    }

    fun appendTransaction(transaction: Transaction) {
        Log.d("ExcelManager", "appendTransaction called..")

        if (!excelFile.exists()) {
            createExcelFileIfNotExists()
        }

        val fis = FileInputStream(excelFile)
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

        FileOutputStream(excelFile).use { workbook.write(it) }
        workbook.close()

        Log.d("ExcelManager", "Transaction saved to Excel at row $rowNum")
    }

    fun getCurrentMonthSpendByCategory(): Map<String, Double> {
        Log.d("ExcelManager", "getCurrentMonthSpendByCategory called..")
        val categoryTotals = mutableMapOf<String, Double>()

        val defaultCategories = listOf("Grocery", "Food", "Fun Activities", "Shopping", "Others")
        defaultCategories.forEach { categoryTotals[it] = 0.0 }

        if (!excelFile.exists()) {
            Log.d("ExcelManager", "No Excel file found.")
            return categoryTotals
        } else {
            Log.d("ExcelManager", "Excel file found.")
        }

        val workbook = WorkbookFactory.create(FileInputStream(excelFile))
        val sheet = workbook.getSheet(SHEET_NAME_EXPENSES)

        val currentMonth = YearMonth.now()

        for (row in sheet.drop(1)) { // Skip header row
            val dateCell = row.getCell(0)
            var categoryCell = row.getCell(3)
            val amountCell = row.getCell(2)
            if (categoryCell?.stringCellValue == "Uncategorized") {
                categoryCell.setCellValue("Others")
            }

            val localDate = try {
                dateCell?.localDateTimeCellValue?.toLocalDate()
            } catch (e: Exception) {
                null
            }

            val category = categoryCell?.stringCellValue?.trim() ?: continue
            val amount = amountCell?.numericCellValue ?: continue

            if (localDate != null && YearMonth.from(localDate) == currentMonth) {
                categoryTotals[category] = categoryTotals.getOrDefault(category, 0.0) + amount
            }
        }

        workbook.close()
        return categoryTotals
    }
}
