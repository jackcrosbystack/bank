package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Account {
    // Transfers update two accounts under the same lock.
    private static final Object BALANCE_LOCK = new Object();

    private final String accountNumber;
    private final Customer owner;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance = BigDecimal.ZERO.setScale(2);

    public Account(String accountNumber, Customer owner, double initialBalance) {
        this(accountNumber, owner, BigDecimal.valueOf(initialBalance));
    }

    public Account(String accountNumber, Customer owner, BigDecimal initialBalance) {
        this.accountNumber = Objects.requireNonNull(accountNumber);
        this.owner = Objects.requireNonNull(owner);
        BigDecimal amount = normalize(initialBalance);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Opening balance must be zero or greater.");
        }
        if (amount.signum() > 0) {
            record(Transaction.Type.OPENING_DEPOSIT, amount, null, Instant.now());
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        synchronized (BALANCE_LOCK) {
            return balance;
        }
    }

    public List<Transaction> getTransactions() {
        synchronized (BALANCE_LOCK) {
            return List.copyOf(transactions);
        }
    }

    public void deposit(BigDecimal amount) {
        BigDecimal value = positiveAmount(amount);
        synchronized (BALANCE_LOCK) {
            record(Transaction.Type.DEPOSIT, value, null, Instant.now());
        }
    }

    public void withdraw(BigDecimal amount) {
        BigDecimal value = positiveAmount(amount);
        synchronized (BALANCE_LOCK) {
            requireFunds(value);
            record(Transaction.Type.WITHDRAWAL, value.negate(), null, Instant.now());
        }
    }

    public void transferTo(Account recipient, BigDecimal amount) {
        Objects.requireNonNull(recipient);
        if (recipient == this) {
            throw new IllegalArgumentException("Choose a different destination account.");
        }
        BigDecimal value = positiveAmount(amount);
        synchronized (BALANCE_LOCK) {
            requireFunds(value);
            Instant timestamp = Instant.now();
            record(Transaction.Type.TRANSFER_OUT, value.negate(), recipient.accountNumber, timestamp);
            recipient.record(Transaction.Type.TRANSFER_IN, value, accountNumber, timestamp);
        }
    }

    private void requireFunds(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }

    private void record(Transaction.Type type, BigDecimal change, String otherAccount, Instant timestamp) {
        BigDecimal newBalance = balance.add(change);
        transactions.add(new Transaction(timestamp, type, change, newBalance, otherAccount));
        balance = newBalance;
    }

    private static BigDecimal positiveAmount(BigDecimal amount) {
        BigDecimal value = normalize(amount);
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        return value;
    }

    private static BigDecimal normalize(BigDecimal amount) {
        Objects.requireNonNull(amount);
        try {
            return amount.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("Amount must be in whole cents.", exception);
        }
    }
}
