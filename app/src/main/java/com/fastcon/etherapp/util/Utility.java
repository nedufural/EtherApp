package com.fastcon.etherapp.util;


import android.view.View;
import android.view.animation.TranslateAnimation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utility {
    /**
     * Converts from epoch unix timestamp to date time format.
     *
     * @param timeStamp epoch time format multiples by 1000 since it is based
     *                  since the standard base time known as "the epoch"
     * @return formatted date.
     */
    public static String epochToDateFormatter(Long timeStamp) {
        Date time = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(time);
    }

    public static SimpleDateFormat epochToDateFormat(Long timeStamp) {
        Date time = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.format(time);
        return  sdf;
    }
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

    public static void slideFromRightToLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(view.getWidth(), 0f, 0f, 0f);
        animate.setDuration(2000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static Long dateFormatterToEpoch() throws ParseException {

        Date today = Calendar.getInstance().getTime();
        // Constructs a SimpleDateFormat using the given pattern
        SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");

        // format() formats a Date into a date/time string.
        String currentTime = crunchifyFormat.format(today);

        // parse() parses text from the beginning of the given string to produce a date.
        Date date = crunchifyFormat.parse(currentTime);
        // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
        return date.getTime();
    }
}
