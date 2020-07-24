package com.fastcon.etherapp.util.common;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.regex.Pattern;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class Commons {
    public static final long SPLASH_WAITING_TIME = 2000;
    public static final String SPEC = "secp256k1";
    public static final String Base_Url = "https://api.coincap.io/";
    public static final String API_Key = "6FNCDKF4VQYQ4FH4NKZF6Z5PKRKXJC15ZB";
    public static final String etherTransactionsUrl = "http://api.etherscan.io/api?module=account&action=txlist&address=";
    public static final String btcTransactionsUrl = "https://blockchain.info/address/3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy?format=json";
    public static final String Notification_Base_Url = "https://fcm.googleapis.com";
    public static final String bitcoin_balance = "https://sochain.com/api/v2/get_address_balance/BTC/";
    public static final String TEST_NET = "https://mainnet.infura.io/v3/b6a2befbf40043a5b25bde109d3037ea";

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

    public static final String SERVER_KEY = "AAAA_5iyWN0:APA91bFGwvYgWa_-ZhtVVki1WgnDxjwS-JB2kRAuFEy3PQapW8TSNZcWORzkXmbEzeV0yxwJyhBp0USxRxQhd9ZJixOA3FEFFB4Be6-LIbQooq2672jAUrygENHQMZRkiprA61F_oJvE";

    public static final String CONTENT_TYPE = "application/json";


    public static Bitmap encodeAsBitmap(String str) throws IllegalArgumentException, WriterException {
        final int WIDTH = 400;
        final int HEIGHT = 400;
        BitMatrix result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }

    public static String removeSpecialCharacter(String str) {
        str = str.replaceAll("[^a-zA-Z0-9]", " ");
        return removeWhiteSpaces(str);
    }

    public static String removeWhiteSpaces(String str) {

        return str.trim().replaceAll(" ", "");
    }


    public static String[] splitAddress(String scannedAddress, String regex) {
        return scannedAddress.split(regex);
    }
}
