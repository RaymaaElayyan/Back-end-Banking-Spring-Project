package org.example.itsmybaking.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found.";
    public static final String ACCOUNT_NOT_FOUND_CODE = "404";
    public static final String ACCOUNT_EXISTS_CODE = "409";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists.";
    public static final String ACCOUNT_CREATION_SUCCESS = "201";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully.";
public static final String INSUFFICIENT_BALANCE_MESSAGE= "Insufficient balance.";
public static final String INSUFFICIENT_BALANCE_CODE = "400";
public static final String ACCOUNT_DEBITED_SUCCESS="007";
public static final String ACCOUNT_DEBITED_MESSAGE = "Account HAS BEEN SUCCESSFULLY debited.";
  public static final String TRANSFERE_SUCCESSFUL_CODE="008";
  public static final String TRANSFERE_SUCCESSFUL_MESSAGE = "Transfer successful.";

    public  static String GenerateAccountNumber(){
      /*
    2024+randomSixDigit

     */

    Year CurrentYear=Year.now();
    int min =100000;
    int max=999999;
    //now we want to generate a random number between min and max
    int randNum=(int)Math.floor(Math.random()*(max-min+1)+min);
    //now the year currently and year random i want to concatenate :
    String randomNumber=String.valueOf(randNum);
    StringBuilder accountNumber=new StringBuilder();
    return accountNumber.append(CurrentYear).append(randomNumber).toString();
}
}
