package com.example;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        try (Scanner input = new Scanner(System.in)) {
            printCommands();
            while (true) {
                try {
                    switch (prompt(input, "Command")) {
                        case "register" -> {
                            Customer customer = bank.registerCustomer(
                                    prompt(input, "Name"),
                                    prompt(input, "Email"),
                                    prompt(input, "Phone"),
                                    prompt(input, "Address"));
                            System.out.println("Registered customer " + customer.getID() + ".");
                        }
                        case "customers" -> {
                            for (Customer customer : bank.getCustomers()) {
                                var accountNumbers = customer.getAccounts().stream()
                                        .map(Account::getAccountNumber)
                                        .toList();
                                System.out.println(customer.getID() + " " + customer.getName()
                                        + " " + accountNumbers);
                            }
                        }
                        case "open" -> {
                            Account account = bank.openAccount(
                                    prompt(input, "Customer ID"), amount(input, "Opening balance"));
                            System.out.println("Opened account " + account.getAccountNumber() + ".");
                        }
                        case "deposit" -> {
                            Account account = readAccount(bank, input, "Account number");
                            account.deposit(amount(input, "Amount"));
                            printBalance(account);
                        }
                        case "withdraw" -> {
                            Account account = readAccount(bank, input, "Account number");
                            account.withdraw(amount(input, "Amount"));
                            printBalance(account);
                        }
                        case "transfer" -> {
                            Account source = readAccount(bank, input, "Source account");
                            Account destination = readAccount(bank, input, "Destination account");
                            source.transferTo(destination, amount(input, "Amount"));
                            printBalance(source);
                            printBalance(destination);
                        }
                        case "balance" -> printBalance(readAccount(bank, input, "Account number"));
                        case "history" -> printHistory(readAccount(bank, input, "Account number"));
                        case "help" -> printCommands();
                        case "quit" -> {
                            return;
                        }
                        case "" -> { }
                        default -> System.out.println("Unknown command. Enter help for commands.");
                    }
                } catch (NoSuchElementException endOfInput) {
                    return;
                } catch (IllegalArgumentException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    private static void printCommands() {
        System.out.println("Bank commands");
        System.out.println("register, customers, open, deposit, withdraw, transfer, balance, history, help, quit");
    }

    private static String prompt(Scanner input, String label) {
        System.out.print(label + "> ");
        return input.nextLine().strip();
    }

    private static Account readAccount(Bank bank, Scanner input, String label) {
        return bank.getAccount(prompt(input, label));
    }

    private static BigDecimal amount(Scanner input, String label) {
        String value = prompt(input, label);
        if (!value.matches("[+-]?(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)")) {
            throw new IllegalArgumentException("Enter a decimal amount, such as 12.50.");
        }
        return new BigDecimal(value);
    }

    private static void printBalance(Account account) {
        System.out.println(account.getAccountNumber() + " balance " + account.getBalance().toPlainString());
    }

    private static void printHistory(Account account) {
        var transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions.");
        }
        for (Transaction transaction : transactions) {
            String otherAccount = transaction.otherAccount() == null ? "" : " account " + transaction.otherAccount();
            System.out.println(transaction.timestamp() + " " + transaction.type() + " "
                    + transaction.change().toPlainString() + otherAccount
                    + " balance " + transaction.balance().toPlainString());
        }
    }
}
