package com.ATM;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ======================= ENUMS =======================
enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, BALANCE_INQUIRY, MINI_STATEMENT
}

enum AccountStatus {
    ACTIVE, BLOCKED, CLOSED
}

// ======================= TRANSACTION =======================
class Transaction {
    private final String transactionId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final double balanceAfter;
    private final String description;

    public Transaction(TransactionType type, double amount, double balanceAfter, String description) {
        this.transactionId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("│ %-8s │ %-14s │ ₹%10.2f │ ₹%10.2f │ %s │ %s",
                transactionId, type, amount, balanceAfter,
                timestamp.format(fmt), description);
    }
}

// ======================= ACCOUNT =======================
class Account {
    private final String accountNumber;
    private final String pin;
    private double balance;
    private AccountStatus status;
    private final List<Transaction> transactions;

    public Account(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void credit(double amount, TransactionType type, String desc) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
        transactions.add(new Transaction(type, amount, balance, desc));
    }

    public void debit(double amount, TransactionType type, String desc) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        balance -= amount;
        transactions.add(new Transaction(type, amount, balance, desc));
    }

    public void setStatus(AccountStatus status) { this.status = status; }

    public void addTransactionRecord(TransactionType type, double amount, String desc) {
        transactions.add(new Transaction(type, amount, balance, desc));
    }
}

// ======================= BANK =======================
class Bank {
    private final Map<String, Account> accounts;
    private final String bankName;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new HashMap<>();
    }

    public String getBankName() { return bankName; }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}

// ======================= ATM =======================
class ATM {
    private final Bank bank;
    private final String atmId;
    private double cashAvailable;
    private Account currentAccount;
    private final Scanner scanner;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public ATM(Bank bank, String atmId, double cashAvailable) {
        this.bank = bank;
        this.atmId = atmId;
        this.cashAvailable = cashAvailable;
        this.scanner = new Scanner(System.in);
    }

    // ---------- Entry Point ----------
    public void start() {
        printWelcome();
        while (true) {
            if (currentAccount == null) {
                if (!login()) {
                    System.out.println("\n⚠  Too many failed attempts. Card retained. Please contact your bank.");
                    break;
                }
            }
            showMainMenu();
            int choice = readInt("Enter your choice: ");
            if (!handleMainMenu(choice)) break;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("   Thank you for banking with " + bank.getBankName() + "!");
        System.out.println("   Have a great day! 👋");
        System.out.println("══════════════════════════════════════════════");
        scanner.close();
    }

    // ---------- Authentication ----------
    private boolean login() {
        System.out.println("\n┌─────────────────────────────────────────────┐");
        System.out.println("│              CARD AUTHENTICATION            │");
        System.out.println("└─────────────────────────────────────────────┘");

        System.out.print("  Enter Account Number: ");
        String accNo = scanner.nextLine().trim();

        Account account = bank.getAccount(accNo);
        if (account == null) {
            System.out.println("  ✖ Account not found.");
            return false;
        }
        if (account.getStatus() == AccountStatus.BLOCKED) {
            System.out.println("  ✖ Account is BLOCKED. Contact your branch.");
            return false;
        }
        if (account.getStatus() == AccountStatus.CLOSED) {
            System.out.println("  ✖ Account is CLOSED.");
            return false;
        }

        int attempts = 0;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("  Enter PIN: ");
            String pin = scanner.nextLine().trim();
            if (account.validatePin(pin)) {
                currentAccount = account;
                System.out.println("\n  ✔ Authentication successful. Welcome!");
                return true;
            }
            attempts++;
            System.out.printf("  ✖ Incorrect PIN. Attempts remaining: %d%n", MAX_LOGIN_ATTEMPTS - attempts);
        }
        return false;
    }

    private void logout() {
        currentAccount = null;
        System.out.println("\n  ✔ You have been securely logged out.");
    }

    // ---------- Menus ----------
    private void printWelcome() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║                                              ║");
        System.out.println("║        " + center(bank.getBankName().toUpperCase(), 30) + "        ║");
        System.out.println("║           ATM SERVICE — " + atmId + "            ║");
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    private void showMainMenu() {
        System.out.println("\n┌─────────────────────────────────────────────┐");
        System.out.println("│                  MAIN MENU                  │");
        System.out.println("├─────────────────────────────────────────────┤");
        System.out.println("│  1. Balance Inquiry                         │");
        System.out.println("│  2. Cash Withdrawal                         │");
        System.out.println("│  3. Cash Deposit                            │");
        System.out.println("│  4. Fund Transfer                           │");
        System.out.println("│  5. Mini Statement                          │");
        System.out.println("│  6. Change PIN                              │");
        System.out.println("│  7. Logout                                  │");
        System.out.println("│  8. Exit ATM                                │");
        System.out.println("└─────────────────────────────────────────────┘");
    }

    private boolean handleMainMenu(int choice) {
        try {
            switch (choice) {
                case 1 -> balanceInquiry();
                case 2 -> withdraw();
                case 3 -> deposit();
                case 4 -> transfer();
                case 5 -> miniStatement();
                case 6 -> changePin();
                case 7 -> logout();
                case 8 -> { return false; }
                default -> System.out.println("  ✖ Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
        return true;
    }

    // ---------- Operations ----------
    private void balanceInquiry() {
        System.out.println("\n──────────── BALANCE INQUIRY ────────────");
        System.out.printf("  Account: %s%n", maskAccount(currentAccount.getAccountNumber()));
        System.out.printf("  Available Balance: ₹%.2f%n", currentAccount.getBalance());
        currentAccount.addTransactionRecord(TransactionType.BALANCE_INQUIRY, 0,
                "Balance checked at " + atmId);
        System.out.println("─────────────────────────────────────────");
    }

    private void withdraw() {
        System.out.println("\n──────────── CASH WITHDRAWAL ────────────");
        System.out.printf("  Available Balance: ₹%.2f%n", currentAccount.getBalance());
        System.out.printf("  ATM Cash Available: ₹%.2f%n", cashAvailable);
        System.out.println("  Quick amounts: [1] ₹500  [2] ₹1000  [3] ₹2000  [4] ₹5000");
        System.out.print("  Choose quick amount or [0] to enter manually: ");
        int quick = readInt("");

        double amount;
        if (quick >= 1 && quick <= 4) {
            amount = switch (quick) {
                case 1 -> 500; case 2 -> 1000; case 3 -> 2000; default -> 5000;
            };
        } else {
            amount = readDouble("  Enter amount: ₹");
        }

        if (amount <= 0) { System.out.println("  ✖ Invalid amount."); return; }
        if (amount % 100 != 0) { System.out.println("  ✖ Amount must be in multiples of ₹100."); return; }
        if (amount > currentAccount.getBalance()) { System.out.println("  ✖ Insufficient balance."); return; }
        if (amount > cashAvailable) { System.out.println("  ✖ ATM does not have sufficient cash."); return; }

        currentAccount.debit(amount, TransactionType.WITHDRAWAL, "Withdrawal at " + atmId);
        cashAvailable -= amount;
        System.out.printf("  ✔ Please collect ₹%.2f cash.%n", amount);
        System.out.printf("  Remaining Balance: ₹%.2f%n", currentAccount.getBalance());
    }

    private void deposit() {
        System.out.println("\n──────────── CASH DEPOSIT ────────────");
        double amount = readDouble("  Enter deposit amount: ₹");
        if (amount <= 0) { System.out.println("  ✖ Invalid amount."); return; }
        if (amount % 100 != 0) { System.out.println("  ✖ Amount must be in multiples of ₹100."); return; }

        currentAccount.credit(amount, TransactionType.DEPOSIT, "Deposit at " + atmId);
        cashAvailable += amount;
        System.out.printf("  ✔ ₹%.2f deposited successfully.%n", amount);
        System.out.printf("  New Balance: ₹%.2f%n", currentAccount.getBalance());
    }

    private void transfer() {
        System.out.println("\n──────────── FUND TRANSFER ────────────");
        System.out.print("  Enter beneficiary account number: ");
        String targetAccNo = scanner.nextLine().trim();

        if (targetAccNo.equals(currentAccount.getAccountNumber())) {
            System.out.println("  ✖ Cannot transfer to your own account.");
            return;
        }
        Account target = bank.getAccount(targetAccNo);
        if (target == null) { System.out.println("  ✖ Beneficiary account not found."); return; }
        if (target.getStatus() != AccountStatus.ACTIVE) { System.out.println("  ✖ Beneficiary account is not active."); return; }

        double amount = readDouble("  Enter transfer amount: ₹");
        if (amount <= 0) { System.out.println("  ✖ Invalid amount."); return; }
        if (amount > currentAccount.getBalance()) { System.out.println("  ✖ Insufficient balance."); return; }

        currentAccount.debit(amount, TransactionType.TRANSFER_OUT,
                "Transfer to " + maskAccount(targetAccNo));
        target.credit(amount, TransactionType.TRANSFER_IN,
                "Transfer from " + maskAccount(currentAccount.getAccountNumber()));

        System.out.printf("  ✔ ₹%.2f transferred to %s.%n", amount, maskAccount(targetAccNo));
        System.out.printf("  Remaining Balance: ₹%.2f%n", currentAccount.getBalance());
    }

    private void miniStatement() {
        System.out.println("\n─────────────────────────── MINI STATEMENT ───────────────────────────");
        List<Transaction> txns = currentAccount.getTransactions();
        if (txns.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            System.out.println("│ TXN ID   │ TYPE           │ AMOUNT     │ BALANCE    │ DATE & TIME         │ DESCRIPTION");
            System.out.println("├──────────┼────────────────┼────────────┼────────────┼─────────────────────┼──────────────────────");
            int start = Math.max(0, txns.size() - 10);
            for (int i = start; i < txns.size(); i++) {
                System.out.println(txns.get(i));
            }
        }
        currentAccount.addTransactionRecord(TransactionType.MINI_STATEMENT, 0,
                "Mini statement generated");
        System.out.println("──────────────────────────────────────────────────────────────────────");
    }

    private void changePin() {
        System.out.println("\n──────────── CHANGE PIN ────────────");
        System.out.print("  Enter current PIN: ");
        String oldPin = scanner.nextLine().trim();
        if (!currentAccount.validatePin(oldPin)) {
            System.out.println("  ✖ Incorrect current PIN.");
            return;
        }
        System.out.print("  Enter new 4-digit PIN: ");
        String newPin = scanner.nextLine().trim();
        if (!newPin.matches("\\d{4}")) {
            System.out.println("  ✖ PIN must be exactly 4 digits.");
            return;
        }
        System.out.print("  Confirm new PIN: ");
        String confirmPin = scanner.nextLine().trim();
        if (!newPin.equals(confirmPin)) {
            System.out.println("  ✖ PINs do not match.");
            return;
        }
        // In a real system, we'd update the pin securely
        System.out.println("  ✔ PIN changed successfully. (Simulated)");
    }

    // ---------- Helpers ----------
    private int readInt(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  ✖ Invalid input. Enter a number: ");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  ✖ Invalid input. Enter a valid amount: ");
            }
        }
    }

    private String maskAccount(String accNo) {
        if (accNo.length() <= 4) return "****";
        return "****" + accNo.substring(accNo.length() - 4);
    }

    private static String center(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
}

// ======================= MAIN =======================
public class ATMMachine {
    public static void main(String[] args) {
        // ---- Setup Bank & Accounts ----
        Bank bank = new Bank("SBI Bank");

        Account acc1 = new Account("1001234567", "1234", 25000.00);
        Account acc2 = new Account("1009876543", "5678", 50000.00);
        Account acc3 = new Account("1005556667", "9999", 12000.00);

        bank.addAccount(acc1);
        bank.addAccount(acc2);
        bank.addAccount(acc3);

        // ---- Initialize ATM ----
        ATM atm = new ATM(bank, "SBI-ATM-DELHI-01", 200000.00);
        atm.start();
    }
}