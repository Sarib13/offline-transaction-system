# Android Banking Application (Beginner-Friendly) - Java + OOP Principles

## 📄 Overview

This is a beginner-friendly Android banking application developed in **Java** using **Object-Oriented Programming (OOP)** principles. It simulates banking operations 
such as login,transaction management (offline) and transaction tracking using a local database.

## 🤖 Features

* Simple login system with input validation
* Secure offline transaction system
* Local transaction queue and history tracking using SQLite/Room
* Internet check using `NetworkUtil.java`
* Clean, modular, and commented Java code

## compilation
1. Clone repo in Android Studio using the command:

```
git clone https://github.com/Sarib13/offline-transaction-system.git
```

2. Build the project
3. Run the Application in any virtual or physical device



## 🔧 OOP Principles Implemented

* **Abstraction**: `Transaction.java` abstract class
* **Encapsulation**: Private fields with getters/setters in `User.java`, `BankAccount.java`
* **Inheritance**: `OnlineTransaction` and `OfflineTransaction` inherit from `Transaction`
* **Polymorphism**: Overridden methods for different transaction types
* **Interfaces**: `TransactionInterface.java` for common transaction behaviors
* **Exception Handling**: Extensive use of `try-catch`, `if-else`, and user-friendly messages

---

## 🔄 Application Flow

### 1. Login Screen

* GUI: `EditText`, `Button`, `TextView`
* Inputs: Email and Password
* Buttons: Login
* Validations:

  * Email format check
  * Non-empty fields
* Feedback:

  * Toast/Alert for success or error
  * Redirect to Dashboard on valid login
  * Show "Invalid Credentials" otherwise

### 2. Bank Dashboard (Post-Login)

* Display:

  * Account holder name, bank name
  * Static/simulated current balance
* Options:

  * Online Transaction
  * Offline Transaction

### 3. Internet Check Logic

* Use `NetworkUtil.java` to check internet
* If online: enable Online Transaction(Unavailable yet)
* If offline: Show dialog:

  * Message: "Internet not available. Continue with Offline Transaction?"

### 4. Offline Transaction Page

* Fields:

  * Bank type (HBL, UBL, Easypaisa, JazzCash, etc.)
  * Receiver’s account number
  * Amount (must be > 0)
  * Transaction reason
* Submit:

  * MFA: 4-digit PIN + Face ID (mocked)
  * Show transaction ID and confirmation
 
### 5. Backend Classes

* `User.java`: Login credentials (email, password)
* `BankAccount.java`: Account holder details, bank type, balance
* `Transaction.java`: Abstract base class for transactions
* `OnlineTransaction.java` and `OfflineTransaction.java`: Inherit and implement specific logic
* `TransactionInterface.java`: Interface for `execute()`, `validate()` methods
* `QRCodeGenerator.java`: Generate QR codes using ZXing or similar library

### 6. Programming Features

* Input validation: email, password, amount, etc.
* Use of loops (for retry, listing transactions)
* Exception handling via `try-catch`
* Meaningful user feedback at each step

### 7. Code Structure

* Clean and organized class files
* Clear separation of logic and UI
* Java code with proper naming conventions and comments
* No third-party libraries unless for QR generation (ZXing allowed)

### 8. GUI Design

* Basic, beginner-friendly, clean UI
* Minimal use of icons/images
* Layouts: LinearLayout, ConstraintLayout
* Compatible with Android Virtual Device (AVD)

### 9. Android Studio Implementation

* Develop exclusively in Android Studio
* Include correct file placements:

  * Java files in `src/java/...`
  * XML files in `res/layout`
* Required Permissions in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

* Internet check using `NretworkUtil.java`
* No Firebase or backend server integration (offline/local only)

### 10. Offline Banking Simulation

* Simulates real-world offline banking experience
* Clean code with expandable structure
* Maintain local transaction queue:

  * Use SQLite/Room for storing offline transactions
  * Assign unique IDs and timestamps
  * On internet recovery: process transactions in FIFO order
* Transaction History:

  * Stores all transactions (success, failed, pending)
  * Fully detailed and viewable

---

## 📂 Folder Structure

```
app/
├── java/
│   ├── com/example/bankapp/
│       ├── LoginActivity.java
│       ├── DashboardActivity.java
│       ├── OfflineTransactionActivity.java
│       ├── User.java
│       ├── BankAccount.java
│       ├── Transaction.java
│       ├── OnlineTransaction.java
│       ├── OfflineTransaction.java
│       ├── TransactionInterface.java
│       ├── QRCodeGenerator.java
│       ├── DatabaseHelper.java
└── res/
    ├── layout/
    │   ├── activity_login.xml
    │   ├── activity_dashboard.xml
    │   └── activity_offline_transaction.xml
    └── AndroidManifest.xml
```

## 🚀 Future Expansion Ideas

* Add actual backend server integration
* Enable online transaction API calls
* Include biometric (real) Face ID / fingerprint
* SMS/Email verification for MFA
* Blockchain-enabled transaction logging
