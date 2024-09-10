package org.example.itsmybaking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
