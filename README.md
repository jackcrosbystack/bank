# Java Bank

A Java console app for registering customers, opening accounts, depositing and withdrawing funds, transferring money, and viewing balances and transaction history. Data lives in memory and resets when the app exits.

## Build and run

Requires Maven and JDK 17 or later.

```sh
mvn compile
java -cp target/classes com.example.Main
```

At the `Command>` prompt, enter `register` to create a customer. Use the customer ID it prints with `open` to open an account. Enter `help` for the full command list or `quit` to exit.
