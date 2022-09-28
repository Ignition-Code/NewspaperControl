package com.ms.newspapercontrol.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public String getFormattedDate(String format, Date date) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }
}
