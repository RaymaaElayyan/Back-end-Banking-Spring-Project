package org.example.itsmybaking.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.itsmybaking.dto.*;
import org.example.itsmybaking.entity.user;
import org.example.itsmybaking.repository.UserRepository;
import org.example.itsmybaking.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return buildResponse(AccountUtils.ACCOUNT_EXISTS_CODE, AccountUtils.ACCOUNT_EXISTS_MESSAGE, null);
        }

        user newUser = buildNewUser(userRequest);
        user savedUser = userRepository.save(newUser);

        sendAccountCreationEmail(savedUser);
        logTransaction(savedUser.getAccountNumber(), "CREDIT", BigDecimal.ZERO);

        AccountInfo accountInfo = buildAccountInfo(savedUser);
        return buildResponse(AccountUtils.ACCOUNT_CREATION_SUCCESS, AccountUtils.ACCOUNT_CREATION_MESSAGE, accountInfo);
    }

    private user buildNewUser(UserRequest userRequest) {
        return user.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.GenerateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
    }

    private void sendAccountCreationEmail(user savedUser) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully Created. \n Your Account Details : \n Your Name: "
                        + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName()
                        + " \n and your Account number is: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
    }

    private void logTransaction(String accountNumber, String transactionType, BigDecimal amount) {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(accountNumber)
                .transactionType(transactionType)
                .amount(amount)
                .build();
        transactionService.saveTransaction(transactionDto);
    }

    private BankResponse buildResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {
        return BankResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .accountInfo(accountInfo)
                .build();
    }

    private AccountInfo buildAccountInfo(user savedUser) {
        return AccountInfo.builder()
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                .build();
    }

    @Override
    public user getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        if (enquiryRequest.getAccountNumber() == null) {
            return buildResponse(AccountUtils.ACCOUNT_NOT_FOUND_CODE, "Account number must be provided.", null);
        }

        user foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        if (foundUser == null) {
            return buildResponse(AccountUtils.ACCOUNT_NOT_FOUND_CODE, AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE, null);
        }

        AccountInfo accountInfo = buildAccountInfo(foundUser);
        return buildResponse(AccountUtils.ACCOUNT_EXISTS_CODE, AccountUtils.ACCOUNT_EXISTS_MESSAGE, accountInfo);
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        if (enquiryRequest.getAccountNumber() == null) {
            return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
        }

        user foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        if (foundUser == null) {
            return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
        }

        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        user foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        if (foundUser == null) {
            return buildResponse("404", "Account not found.", null);
        }

        BigDecimal newBalance = foundUser.getAccountBalance().add(request.getAmount());
        foundUser.setAccountBalance(newBalance);
        userRepository.save(foundUser);

        logTransaction(foundUser.getAccountNumber(), "CREDIT", request.getAmount());
        AccountInfo accountInfo = buildAccountInfo(foundUser);

        return buildResponse("201", "Account credited successfully.", accountInfo);
    }

    @Override
    @Transactional
    public BankResponse debitAccount(CreditDebitRequest request) {
        user foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if (foundUser == null) {
            return buildResponse(AccountUtils.ACCOUNT_NOT_FOUND_CODE, AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE, null);
        }

        BigDecimal currentBalance = foundUser.getAccountBalance();
        BigDecimal amountToDebit = request.getAmount();

        System.out.println("Current balance: " + currentBalance);
        System.out.println("Amount to debit: " + amountToDebit);

        if (currentBalance.compareTo(amountToDebit) < 0) {
            return buildResponse(AccountUtils.INSUFFICIENT_BALANCE_CODE, AccountUtils.INSUFFICIENT_BALANCE_MESSAGE, null);
        }

        BigDecimal newBalance = currentBalance.subtract(amountToDebit);
        foundUser.setAccountBalance(newBalance);

        System.out.println("New balance after debit: " + newBalance);

        user savedUser = userRepository.save(foundUser);
        System.out.println("Saved user: " + savedUser);

        logTransaction(foundUser.getAccountNumber(), "DEBIT", amountToDebit);
        AccountInfo accountInfo = buildAccountInfo(foundUser);

        return buildResponse(AccountUtils.ACCOUNT_DEBITED_SUCCESS, AccountUtils.ACCOUNT_DEBITED_MESSAGE, accountInfo);
    }


    @Override
    public BankResponse transfer(TransferRequest req) {
        user sourceUser = userRepository.findByAccountNumber(req.getSourceAccountNumber());
        if (sourceUser == null) {
            return buildResponse("404", "Source account not found.", null);
        }

        user destinationUser = userRepository.findByAccountNumber(req.getDestinationAccountNumber());
        if (destinationUser == null) {
            return buildResponse("404", "Destination account not found.", null);
        }

        if (req.getAmount().compareTo(sourceUser.getAccountBalance()) > 0) {
            return buildResponse(AccountUtils.INSUFFICIENT_BALANCE_CODE, AccountUtils.INSUFFICIENT_BALANCE_MESSAGE, null);
        }

        // Perform Transfer
        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(req.getAmount()));
        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(req.getAmount()));
        userRepository.save(sourceUser);
        userRepository.save(destinationUser);

        // Log transactions
        logTransaction(sourceUser.getAccountNumber(), "DEBIT", req.getAmount());
        logTransaction(destinationUser.getAccountNumber(), "CREDIT", req.getAmount());

        AccountInfo accountInfo = buildAccountInfo(sourceUser);
        return buildResponse(AccountUtils.TRANSFERE_SUCCESSFUL_CODE, AccountUtils.TRANSFERE_SUCCESSFUL_MESSAGE, accountInfo);
    }

    public BankResponse deleteAccount(Long id) {
        Optional<user> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new BankResponse("404", "Account not found", null);
        }

        userRepository.deleteById(id);
        return new BankResponse("200", "Account deleted successfully", null);
    }
    @Transactional
    public BankResponse updateUser(Long id, UserRequest userRequest) {
        log.debug("Received ID: {}", id);

        Optional<user> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user existingUser = optionalUser.get();
            log.debug("Found user: {}", existingUser);

            // Update user fields
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPhoneNumber(userRequest.getPhoneNumber());
            existingUser.setAddress(userRequest.getAddress());

            // Save updated user
            user savedUser = userRepository.save(existingUser);
            log.debug("User updated successfully: {}", savedUser);
            return new BankResponse("User updated successfully", savedUser);
        } else {
            log.debug("User not found for ID: {}", id);
            return new BankResponse("User not found", "User not found");
        }
    }
    public BankResponse partialUpdateUser(Long id, PartialBankUserUpdateDTO partialUpdateDTO) {
        // Fetch the existing user by ID
        user existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        // Update fields based on non-null values in partialUpdateDTO
        Optional.ofNullable(partialUpdateDTO.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(partialUpdateDTO.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);

        // Log the updated user before saving
        System.out.println("Updating user: " + existingUser);

        // Save the updated user
        userRepository.save(existingUser);

        // Return the appropriate response
        return new BankResponse("007", existingUser);
    }



    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }


}

