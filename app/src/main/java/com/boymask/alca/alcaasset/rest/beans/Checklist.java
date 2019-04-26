package com.boymask.alca.alcaasset.rest.beans;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Checklist implements Serializable {

    private long id;
    private long assetId;
    private long checkId;
    private String description;
    private String codiceNormativa;
    private String fileNormativa;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
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

    public String getFileNormativa() {
        return fileNormativa;
    }

    public void setFileNormativa(String fileNormativa) {
        this.fileNormativa = fileNormativa;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}

