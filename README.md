# 📱 SMSReaderApp

SMSReaderApp is a Kotlin-based Android app that automatically reads incoming SMS messages, extracts financial transaction data, and updates an Excel file stored locally on the device. This app is designed to help users keep track of their expenses and investments with minimal manual input.

---

## 🚀 Features Implemented

### ✅ SMS Reading & Parsing
- Listens for incoming SMS using `BroadcastReceiver`
- Extracts details such as:
    - Sender (Bank name)
    - Transaction amount
    - Timestamp
    - Message content

### ✅ Excel Integration
- Automatically creates an Excel file (`Expense_Tracker_Khushal.xlsx`) if not present
- Appends each valid transaction to the Excel file
- Stores Excel file in app-specific external storage:
    - `/storage/emulated/0/Android/data/com.example.smsreaderapp/files/`

### ✅ Categorization Logic
- Classifies transactions into two top-level categories:
    - **Expenditures**
    - **Investments**
- Further breaks down **Expenditures** into:
    - Grocery
    - Food
    - Fun Activities
    - Shopping
- Payment mode categorization:
    - UPI
    - ICICI Credit Card
    - HDFC Credit Card

### 🧱 Clean Architecture
- `SmsReceiver`: Handles SMS intercept and parsing
- `ExcelManager`: Manages Excel creation and updates
- Designed with **future integration of APIs** in mind for parsing SMS into structured JSON

---

## 📂 Folder Structure

```plaintext
com.example.smsreaderapp/
│
├── MainActivity.kt           # Sets up UI & permissions
├── SmsReceiver.kt            # Listens for incoming SMS messages
├── ExcelManager.kt           # Handles Excel file creation and updates
├── model/
│   └── Transaction.kt        # Data class for structured transaction details
├── parser/
│   └── SmsParser.kt          # Extracts structured data from raw SMS messages
├── utils/
│   └── FileUtil.kt           # File helpers (optional/if added later)
