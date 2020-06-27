package com.fastcon.etherapp.util;

import java.util.regex.Pattern;

public class Commons {
    public static final long SPLASH_WAITING_TIME = 2000;
    public static final String SPEC = "secp256k1";
    public static final String Base_Url = "https://api.coincap.io/";
    public static final String API_Key = "6FNCDKF4VQYQ4FH4NKZF6Z5PKRKXJC15ZB";
    public static final String etherTransactionsUrl = "http://api.etherscan.io/api?module=account&action=txlist&address=";
    public static final String btcTransactionsUrl = "https://blockchain.info/address/3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy?format=json";
    public static final String Notification_Base_Url = "";
    public static final String bitcoin_balance = "https://sochain.com/api/v2/get_address_balance/BTC/";
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +     //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
}
