package com.fastcon.etherapp.util.functions;

import java.math.BigDecimal;

public class OperationsUtils {
    public static String exponentialNumToString(Double decimal) {
        return BigDecimal.valueOf(decimal).toPlainString();
    }

    public static int decimalPointCounter(Double doubleFigure) {
        String[] result = doubleFigure.toString().split("\\.");
        return result[1].length() - 1;
    }

    public static Double exchangeRateReverseCalculation(Double rates) {
        int decimals = decimalPointCounter(rates);
        return Math.pow(10, decimals) / (rates * Math.pow(10, decimals));
    }

}
