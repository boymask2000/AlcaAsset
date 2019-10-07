package com.boymask.alca.alcaasset.rest.beans;

public enum TypeManuale {
    INFO_GENERICO(0, "", ""), //
    SCHEDA_TECNICA(1, "", ""), //
    INFO_RATING_GUIDANCE(2, "", ""), //
    SHORT_REFERENCE_FOR_MOBILE(3, "", ""); //

    private final int id;
    private final String longDescr;
    private final String shortDescr;

    TypeManuale(int id, String shortDescr, String longDescr) {
        this.id = id;
        this.longDescr = longDescr;
        this.shortDescr = shortDescr;
    }

    public int getId() {
        return id;
    }

    public String getLongDescr() {
        return longDescr;
    }

    public String getShortDescr() {
        return shortDescr;
    }
    public static TypeManuale getType(int idd ){
        switch(idd) {
            case 0: return INFO_GENERICO;
            case 1: return SCHEDA_TECNICA;
            case 2: return INFO_RATING_GUIDANCE;
            case 3: return SHORT_REFERENCE_FOR_MOBILE;
            default:
                break;
        }
        return INFO_GENERICO;
    }
}