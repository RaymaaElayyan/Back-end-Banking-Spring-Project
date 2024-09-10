package org.example.itsmybaking.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.itsmybaking.dto.*;
import org.example.itsmybaking.entity.user;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {//interprise projects for"services" do not need interfaces
    BankResponse createAccount(UserRequest userRequest);//so here when user creates an account so the request goes responding by bank response after the client sends the request

    user getUserById(Long id);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    // Balance enquiry should accept EnquiryRequest instead of UserRequest
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest request);
BankResponse debitAccount(CreditDebitRequest request);
BankResponse transfer(TransferRequest req);
    public BankResponse deleteAccount(Long id) ;
    BankResponse updateUser(Long id, UserRequest userRequest);

    BankResponse partialUpdateUser(Long id, PartialBankUserUpdateDTO partialUpdateDTO);

    void deleteUserById(Long id);

    boolean existsById(Long id);
}
