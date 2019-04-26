package com.boymask.alca.alcaasset.rest.beans;


import java.io.Serializable;

public class Asset implements Serializable {
    private long id;
    private int family;
    private String assetNum;
    private String changedDate;
    private String description;
    private String longDescription;
    private String masterSystem;
    private String system;
    private String subSystem;
    private String location;
    private String siteId;
    private String workCenter;
    private String assetType;
    private String assetQuantity;
    private String unitOfMeasure;
    private String inventoryCategory;
    private String purchasePrice;
    private String budgetedCost;
    private String replacementCost;
    private String meterGroup;
    private String belongsTo;
    private String contractNumber;
    private String taskDelivOrderNum;
    private String drawingRefId;
    private String warrantyExpDate;
    private String statusDate;
    private String installationDate;

    private int lastStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMasterSystem() {
        return masterSystem;
    }

    public void setMasterSystem(String masterSystem) {
        this.masterSystem = masterSystem;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(String workCenter) {
        this.workCenter = workCenter;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getInventoryCategory() {
        return inventoryCategory;
    }

    public void setInventoryCategory(String inventoryCategory) {
        this.inventoryCategory = inventoryCategory;
    }

    public String getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(String replacementCost) {
        this.replacementCost = replacementCost;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(String changedDate) {
        this.changedDate = changedDate;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getAssetQuantity() {
        return assetQuantity;
    }

    public void setAssetQuantity(String assetQuantity) {
        this.assetQuantity = assetQuantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getBudgetedCost() {
        return budgetedCost;
    }

    public void setBudgetedCost(String budgetedCost) {
        this.budgetedCost = budgetedCost;
    }

    public String getMeterGroup() {
        return meterGroup;
    }

    public void setMeterGroup(String meterGroup) {
        this.meterGroup = meterGroup;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getTaskDelivOrderNum() {
        return taskDelivOrderNum;
    }

    public void setTaskDelivOrderNum(String taskDelivOrderNum) {
        this.taskDelivOrderNum = taskDelivOrderNum;
    }

    public String getDrawingRefId() {
        return drawingRefId;
    }

    public void setDrawingRefId(String drawingRefId) {
        this.drawingRefId = drawingRefId;
    }

    public String getWarrantyExpDate() {
        return warrantyExpDate;
    }

    public void setWarrantyExpDate(String warrantyExpDate) {
        this.warrantyExpDate = warrantyExpDate;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    @Override
    public String toString() {
        return "Asset [id=" + id + ", family=" + family + ", assetNum=" + assetNum + ", changedDate=" + changedDate
                + ", description=" + description + ", longDescription=" + longDescription + ", masterSystem="
                + masterSystem + ", system=" + system + ", subSystem=" + subSystem + ", location=" + location
                + ", siteId=" + siteId + ", workCenter=" + workCenter + ", assetType=" + assetType + ", assetQuantity="
                + assetQuantity + ", unitOfMeasure=" + unitOfMeasure + ", inventoryCategory=" + inventoryCategory
                + ", purchasePrice=" + purchasePrice + ", budgetedCost=" + budgetedCost + ", replacementCost="
                + replacementCost + ", meterGroup=" + meterGroup + ", belongsTo=" + belongsTo + ", contractNumber="
                + contractNumber + ", taskDelivOrderNum=" + taskDelivOrderNum + ", drawingRefId=" + drawingRefId
                + ", warrantyExpDate=" + warrantyExpDate + ", statusDate=" + statusDate + ", installationDate="
                + installationDate + "]";
    }

    public int getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(int lastStatus) {
        this.lastStatus = lastStatus;
    }

}
