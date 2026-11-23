package com.example;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Bank {
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public synchronized Customer registerCustomer(
            String name, String email, String phoneNumber, String address) {
        Objects.requireNonNull(name);
        if (name.isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        String id = "C" + (customers.size() + 1);
        Customer customer = new Customer(name, email, phoneNumber, address, id);
        customers.put(id, customer);
        return customer;
    }

    public synchronized Account openAccount(String customerId, BigDecimal openingBalance) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Unknown customer " + customerId + ".");
        }
        String accountNumber = "A" + (accounts.size() + 1);
        Account account = new Account(accountNumber, customer, openingBalance);
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        return account;
    }

    public synchronized Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Unknown account " + accountNumber + ".");
        }
        return account;
    }

    public synchronized List<Customer> getCustomers() {
        return List.copyOf(customers.values());
    }
}
