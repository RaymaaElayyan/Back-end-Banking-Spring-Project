package org.example.itsmybaking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.itsmybaking.entity.user;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountInfo {
    @Schema(name ="User Account Name ")
    private String accountName;
    @Schema(name= "User Account Balance")
    private BigDecimal accountBalance;
    @Schema(name = "User Account Number")
    private String accountNumber;
    public AccountInfo(user user) {
        this.accountName = user.getFirstName() + " " + user.getLastName();
        this.accountBalance = user.getAccountBalance(); // Ensure these fields are available
        this.accountNumber = user.getAccountNumber();
    }
}
