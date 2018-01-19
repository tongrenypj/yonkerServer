package com.exam.model.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mac on 2017/3/10.
 */
@Entity
public class Field {
    private Integer fieldId;
    private Integer projectId;
    private String fieldQualifiedId;//技术员
    private String fieldName;//田块名
    private String fieldLocation;//田块位置
    private Double fieldArea;//田块面积
    private String fieldNumber;//田块编号
    private String fieldWaterCondition;//灌溉条件

    private String fieldWaterQualified;//水源质量

    private String fieldPlantType;//作物种植情况

    private String fieldOwner;//田块拥有者姓名
    private String fieldOwnerIdentification;//土地拥有者身份证号

    private String fieldOwnerPhone;//土地拥有者电话


    private String fieldPollutionLevel;//污染程度
    private String fieldNotes;//备注

    private Double lon;  //经度

    private Double lat;  //纬度

    private Boolean isCompleted;//田块是否完成


    private String waterManagerName;

    private String waterManagerPhone;


    //非田块内容，是额外需求

    private String photoPath;

    private List<FieldExecute> fieldExecuteList;
    private List<FieldPlan> fieldPlanList;
    private String projectName;
    private String fieldQualifiedName;//田块技术员

    private List<CollectData> collectDataList;


    @Id
    @GeneratedValue
    @Basic
    @Column(name = "fieldId")
    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    @Basic
    @Column(name = "projectId")
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Basic
    @Column(name = "fieldNumber")
    public String getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    @Basic
    @Column(name = "fieldQualifiedId")
    public String getFieldQualifiedId() {
        return fieldQualifiedId;
    }

    public void setFieldQualifiedId(String fieldQualifiedId) {
        this.fieldQualifiedId = fieldQualifiedId;
    }

    @Basic
    @Column(name = "fieldName")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Basic
    @Column(name = "fieldLocation")
    public String getFieldLocation() {
        return fieldLocation;
    }

    public void setFieldLocation(String fieldLocation) {
        this.fieldLocation = fieldLocation;
    }

    @Basic
    @Column(name = "fieldArea")
    public Double getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(Double fieldArea) {
        this.fieldArea = fieldArea;
    }

    @Basic
    @Column(name = "fieldWaterCondition")
    public String getFieldWaterCondition() {
        return fieldWaterCondition;
    }

    public void setFieldWaterCondition(String fieldWaterCondition) {
        this.fieldWaterCondition = fieldWaterCondition;
    }

    @Basic
    @Column(name = "fieldWaterQualified")
    public String getFieldWaterQualified() {
        return fieldWaterQualified;
    }

    public void setFieldWaterQualified(String fieldWaterQualified) {
        this.fieldWaterQualified = fieldWaterQualified;
    }

    @Basic
    @Column(name = "fieldPlantType")
    public String getFieldPlantType() {
        return fieldPlantType;
    }

    public void setFieldPlantType(String fieldPlantType) {
        this.fieldPlantType = fieldPlantType;
    }


    @Basic
    @Column(name = "fieldOwner")
    public String getFieldOwner() {
        return fieldOwner;
    }

    public void setFieldOwner(String fieldOwner) {
        this.fieldOwner = fieldOwner;
    }

    @Basic
    @Column(name = "fieldOwnerIdentification")
    public String getFieldOwnerIdentification() {
        return fieldOwnerIdentification;
    }

    public void setFieldOwnerIdentification(String fieldOwnerIdentification) {
        this.fieldOwnerIdentification = fieldOwnerIdentification;
    }


    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "fieldOwnerPhone")
    public String getFieldOwnerPhone() {
        return fieldOwnerPhone;
    }

    public void setFieldOwnerPhone(String fieldOwnerPhone) {
        this.fieldOwnerPhone = fieldOwnerPhone;
    }

    @Basic
    @Column(name = "fieldPollutionLevel")
    public String getFieldPollutionLevel() {
        return fieldPollutionLevel;
    }

    public void setFieldPollutionLevel(String fieldPollutionLevel) {
        this.fieldPollutionLevel = fieldPollutionLevel;
    }

    @Basic
    @Column(name = "fieldNotes")
    public String getFieldNotes() {
        return fieldNotes;
    }

    public void setFieldNotes(String fieldNotes) {
        this.fieldNotes = fieldNotes;
    }

    @Basic
    @Column(name = "isCompleted")
    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }


    public String getWaterManagerName() {
        return waterManagerName;
    }

    public void setWaterManagerName(String waterManagerName) {
        this.waterManagerName = waterManagerName;
    }

    public String getWaterManagerPhone() {
        return waterManagerPhone;
    }

    public void setWaterManagerPhone(String waterManagerPhone) {
        this.waterManagerPhone = waterManagerPhone;
    }

    @Transient
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Transient
    public List<FieldExecute> getFieldExecuteList() {
        return fieldExecuteList;
    }

    public void setFieldExecuteList(List<FieldExecute> fieldExecuteList) {
        this.fieldExecuteList = fieldExecuteList;
    }

    @Transient
    public List<FieldPlan> getFieldPlanList() {
        return fieldPlanList;
    }

    public void setFieldPlanList(List<FieldPlan> fieldPlanList) {
        this.fieldPlanList = fieldPlanList;
    }

    @Transient
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    @Transient
    public String getFieldQualifiedName() {
        return fieldQualifiedName;
    }

    public void setFieldQualifiedName(String fieldQualifiedName) {
        this.fieldQualifiedName = fieldQualifiedName;
    }

    @Transient

    public List<CollectData> getCollectDataList() {
        return collectDataList;
    }

    public void setCollectDataList(List<CollectData> collectDataList) {
        this.collectDataList = collectDataList;
    }
}
