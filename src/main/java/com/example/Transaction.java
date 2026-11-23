package com.example;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        Instant timestamp, Type type, BigDecimal change, BigDecimal balance, String otherAccount) {
    public enum Type {
        OPENING_DEPOSIT, DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }
}
