package org.example.itsmybaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.itsmybaking.entity.user;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

    // Constructor for successful update with account info
    public BankResponse(String responseCode, user existingUser) {
        this.responseCode = responseCode;
        this.responseMessage = "User successfully updated";
        this.accountInfo = new AccountInfo(existingUser); // Ensure AccountInfo class handles User correctly
    }

    // Constructor for user not found
    public BankResponse(String responseCode) {
        this.responseCode = responseCode;
        this.responseMessage = "User not found";
        this.accountInfo = null; // Set accountInfo to null for not found cases
    }

    // Constructor with responseCode and responseMessage, used for custom scenarios
    public BankResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountInfo = null; // Default to null unless explicitly set otherwise
    }
}
