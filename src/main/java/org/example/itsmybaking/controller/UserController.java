package org.example.itsmybaking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.itsmybaking.dto.*;
import org.example.itsmybaking.entity.user;
import org.example.itsmybaking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/user")
@Tag(name= "User Account Management APIs")
public class UserController {

    @Autowired
    UserService userService;
    @Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
    })
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/{id}")
    public user getUser(@PathVariable Long id) {
        return userService.getUserById(id); // Implement this method in UserService
    }
    @Operation(summary = "Balance Enquiry", description = "Give an acoount number , check how much the user has")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    })
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAccount(creditDebitRequest);
    }

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);

    }

    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest userRequest) {
        return userService.creditAccount(userRequest);
    }

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }
//    @Operation(summary = "Delete User Account", description = "Delete an existing user account by ID")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Http Status 200 OK"),
//            @ApiResponse(responseCode = "404", description = "Account not found")
//    })
//    @DeleteMapping("/{id}")
//    public BankResponse deleteAccount(@PathVariable Long id) {
//        return userService.deleteAccount(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @PutMapping("/{id}")//it may update things not listed So modify --->
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        BankResponse response = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public BankResponse partialUpdateUser(@PathVariable Long id, @RequestBody PartialBankUserUpdateDTO partialUpdateDTO) {
        return userService.partialUpdateUser(id, partialUpdateDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkUserExists(@PathVariable Long id) {
        if (userService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> optionsRequest(@PathVariable Long id) {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, PUT, DELETE, OPTIONS")
                .build();
    }

}

