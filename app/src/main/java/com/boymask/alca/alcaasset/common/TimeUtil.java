package com.boymask.alca.alcaasset.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String getFormattedDate(String aaaammgg) {
        Formatter formatter = new Formatter(aaaammgg);
        return formatter.getGiorno() + "/" + formatter.getMese() + "/" + formatter.getAnno();
    }

    public static String getLocalizedData(){

        return getLocalizedData(new Date());
    }
    public static String getLocalizedData(Date d){
        String format="MM/dd/yyyy";

        Locale locale = Locale.getDefault();
        if(locale.getCountry().toUpperCase().equals("IT"))format="dd/MM/yyyy";
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(d);
    }
    public static String getCanonicData(){

        return getCanonicData(new Date());
    }
    public static String getCanonicData(Date d){
        String format="yyyyMMdd";

        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(d);
    }

}

class Formatter {
    private final String anno;
    private final String mese;
    private final String giorno;

    public Formatter(String aaaammgg) {
        anno = aaaammgg.substring(0, 4);
        mese = aaaammgg.substring(4, 6);
        giorno = aaaammgg.substring(6, 8);
    }

    public String getAnno() {
        return anno;
    }

    public String getMese() {
        return mese;
    }

    public String getGiorno() {
        return giorno;
    }

}
