🏧 Java ATM Machine — OOP Simulation
<p align="center"> <img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"> <img src="https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge" alt="OOP"> <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"> <img src="https://img.shields.io/badge/Status-Complete-brightgreen?style=for-the-badge" alt="Status"> <img src="https://img.shields.io/badge/PRs-Welcome-orange?style=for-the-badge" alt="PRs Welcome"> </p><p align="center"> A professional, single-file, console-based ATM simulation built with pure Java — demonstrating clean Object-Oriented design, secure authentication, and real-world banking operations. </p>
📖 Table of Contents
Overview

Features

Architecture

Tech Stack

Getting Started

Usage

Sample Credentials

Project Structure

OOP Concepts Demonstrated

Screenshots

Roadmap

Contributing

License

Author

🎯 Overview
Java ATM Machine is a fully functional, dependency-free ATM simulation written in a single Java file. It replicates real-world ATM workflows including secure card authentication, cash withdrawals, deposits, fund transfers, mini statements, and PIN changes — all through an elegant console interface.

The project is designed as a learning showcase for Java OOP principles and is ideal for:

🎓 Students learning Java & OOP

💼 Portfolio demonstrations

🧪 Interview preparation

🏗️ A foundation for GUI or web-based ATM apps

✨ Features
Feature	Description
🔐 Secure Login	Account number + 4-digit PIN with a 3-attempt lockout mechanism
💵 Balance Inquiry	Instant balance display with masked account number
💸 Cash Withdrawal	Quick-amount buttons (₹500 / ₹1000 / ₹2000 / ₹5000) + manual entry
💰 Cash Deposit	Deposit validation (multiples of ₹100)
🔄 Fund Transfer	Peer-to-peer transfers with real-time beneficiary validation
📊 Mini Statement	Last 10 transactions with TXN ID, type, amount, balance & timestamp
🔑 PIN Change	Secure PIN update with confirmation
🧾 Transaction Ledger	Auto-generated UUID-based TXN IDs & running balance tracking
✅ Input Validation	Guards against invalid amounts, insufficient funds, and cash limits
🏦 Multi-Account Support	Bank manages multiple accounts via HashMap
🎨 Clean Console UI	Box-drawing characters for a professional terminal look
🏗️ Architecture
The project follows a layered OOP architecture with clear separation of concerns:

text
┌─────────────────────────────────────────────┐
│                 ATMMachine                  │  ← Entry Point
└───────────────────┬─────────────────────────┘
                    │
        ┌───────────┴───────────┐
        │                       │
   ┌────▼─────┐          ┌──────▼──────┐
   │   Bank   │◄────────►│     ATM     │  ← Controller / UI
   └────┬─────┘          └──────┬──────┘
        │                       │
        │                ┌──────▼──────┐
        │                │ Transaction │  ← Data Model
        │                └─────────────┘
        │
   ┌────▼─────┐
   │ Account  │  ← Core Entity
   └──────────┘
Class Responsibilities
Class	Responsibility
ATMMachine	Entry point — boots up the bank and ATM
Bank	Manages account registry & lookups
Account	Holds balance, PIN, status & transaction history
ATM	Handles UI, authentication, and banking operations
Transaction	Immutable record of a single transaction
TransactionType	Enum — DEPOSIT, WITHDRAWAL, TRANSFER_IN/OUT, etc.
AccountStatus	Enum — ACTIVE, BLOCKED, CLOSED
🛠️ Tech Stack
Language: Java 17+ (uses switch expressions, records-ready patterns)

Paradigm: Object-Oriented Programming

Libraries: Zero external dependencies — pure JDK

Data Structures: HashMap, ArrayList, UUID, LocalDateTime

I/O: Scanner for console interaction

🚀 Getting Started
Prerequisites
☕ JDK 17 or higher installed (Download)

💻 Any terminal / command prompt

Installation
bash
# 1. Clone the repository
git clone https://github.com/your-username/java-atm-machine.git

# 2. Navigate into the project
cd java-atm-machine

# 3. Compile the source
javac ATMMachine.java

# 4. Run the application
java ATMMachine
That's it — no build tools, no Maven, no Gradle. Just pure Java. 🎉

🎮 Usage
Once you run the program, you'll be greeted with the ATM welcome screen:

text
╔══════════════════════════════════════════════╗
║                                              ║
║              SBI BANK                        ║
║           ATM SERVICE — SBI-ATM-DELHI-01     ║
║                                              ║
╚══════════════════════════════════════════════╝

┌─────────────────────────────────────────────┐
│              CARD AUTHENTICATION            │
└─────────────────────────────────────────────┘
  Enter Account Number: 
  Enter PIN: 
After logging in, you'll see the Main Menu:

text
┌─────────────────────────────────────────────┐
│                  MAIN MENU                  │
├─────────────────────────────────────────────┤
│  1. Balance Inquiry                         │
│  2. Cash Withdrawal                         │
│  3. Cash Deposit                            │
│  4. Fund Transfer                           │
│  5. Mini Statement                          │
│  6. Change PIN                              │
│  7. Logout                                  │
│  8. Exit ATM                                │
└─────────────────────────────────────────────┘
🔑 Sample Credentials
Use any of the pre-configured accounts to explore:

Account Number	PIN	Initial Balance
1001234567	1234	₹25,000.00
1009876543	5678	₹50,000.00
1005556667	9999	₹12,000.00
💡 Tip: Transfer between these accounts to see both sides of a transaction reflected in their mini statements!

📂 Project Structure
text
java-atm-machine/
│
├── ATMMachine.java          # Complete application (single file)
│   ├── enum TransactionType
│   ├── enum AccountStatus
│   ├── class Transaction
│   ├── class Account
│   ├── class Bank
│   ├── class ATM
│   └── class ATMMachine (main)
│
└── README.md                # You are here 📍
🧠 OOP Concepts Demonstrated
This project is a practical OOP masterclass — every core principle is used meaningfully:

Principle	Where & How
🔒 Encapsulation	Account and Transaction fields are private, accessed via getters
🧬 Inheritance / Composition	ATM composes Bank, Account, and Transaction
🎭 Polymorphism	Overridden toString() in Transaction; overloaded constructors
🎨 Abstraction	ATM exposes simple methods (withdraw(), deposit()) hiding internals
📦 Enums	Type-safe TransactionType & AccountStatus
🛡️ Immutability	Transaction fields are final — cannot be tampered with
🔗 Collections Framework	HashMap for accounts, ArrayList for history
📅 Modern Date/Time API	LocalDateTime + DateTimeFormatter for stamps
🚨 Exception Handling	try-catch blocks for robust input validation
🧪 Defensive Programming	Pin masking, attempt limits, amount validation
🖼️ Screenshots
Add your terminal screenshots here for a richer README — e.g., login, menu, mini statement.

Login Screen	Main Menu	Mini Statement
(add screenshot)	(add screenshot)	(add screenshot)
🗺️ Roadmap
Planned enhancements to evolve this into a production-grade simulation:

□ 💾 Persistent Storage — File-based or JDBC (MySQL/PostgreSQL)
□ 🖥️ GUI Version — JavaFX or Swing interface
□ 🔒 PIN Hashing — BCrypt or SHA-256 with salt
□ 📱 REST API — Spring Boot endpoint layer
□ 🧾 Receipt Printing — Exportable transaction PDFs
□ 🌐 Multi-Language Support — i18n for regional languages
□ 🧪 Unit Tests — JUnit 5 coverage for all operations
□ 📊 Admin Panel — Cash refill & audit logs
🤝 Contributing
Contributions are always welcome! Here's how:

🍴 Fork the repository

🌿 Create a feature branch (git checkout -b feature/amazing-feature)

💾 Commit your changes (git commit -m 'Add amazing feature')

📤 Push to the branch (git push origin feature/amazing-feature)

🔃 Open a Pull Request

Please make sure your code follows the existing style and includes appropriate comments.

Copyright (c) 2026 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
👨‍💻 Author
Your Name

🐙 GitHub: @dhanushkavinofficial


📧 Email: dhanushkavinofficial@gmail.com
<p align="center"> <b>⭐ If this project helped you learn Java OOP, consider giving it a star! ⭐</b> <br><br> <i>Built with ☕ Java and a lot of ❤️</i> </p>
