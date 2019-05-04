package com.boymask.alca.alcaasset.rest.beans;

public class Check {

    private int id;
    private long famigliaId;
    private String description;
    private String descriptionUS;
    private String codiceNormativa;

    private String filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodiceNormativa() {
        return codiceNormativa;
    }

    public void setCodiceNormativa(String codiceNormativa) {
        this.codiceNormativa = codiceNormativa;
    }

    public String getDescriptionUS() {
        return descriptionUS;
    }

    public void setDescriptionUS(String descriptionUS) {
        this.descriptionUS = descriptionUS;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFamigliaId() {
        return famigliaId;
    }

    public void setFamigliaId(long famigliaId) {
        this.famigliaId = famigliaId;
    }
}
