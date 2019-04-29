package com.boymask.alca.alcaasset.common;

public class TimeUtil {
    public static String getFormattedDate(String aaaammgg) {
        Formatter formatter = new Formatter(aaaammgg);
        return formatter.getGiorno() + "/" + formatter.getMese() + "/" + formatter.getAnno();
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
