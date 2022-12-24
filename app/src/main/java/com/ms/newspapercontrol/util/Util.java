package com.ms.newspapercontrol.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    final String decimalPattern = "#,##0";
    final DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    public Util() {
        this.symbols.setGroupingSeparator('.');
    }

    public String getFormattedNumber(Double number) {
        return new DecimalFormat(decimalPattern, symbols).format(number);
    }

    public String getFormattedNumber(Float number) {
        return new DecimalFormat(decimalPattern, symbols).format(number);
    }

    public String getFormattedNumber(Integer number) {
        return new DecimalFormat(decimalPattern, symbols).format(number);
    }

    public String getFormattedDate(String format, Date date) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }
}
