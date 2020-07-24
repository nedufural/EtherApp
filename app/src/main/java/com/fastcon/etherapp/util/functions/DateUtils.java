package com.fastcon.etherapp.util.functions;


import android.view.View;
import android.view.animation.TranslateAnimation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
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

}
