package com.exam.model.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mac on 2017/3/2.
 */

@Entity
public class PlanProcedure {

    @Id
    @GeneratedValue

    @Column(length = 11)
    Integer procedureId;

    @Column(length = 11)
    Integer planId;

    @Column(length = 1)
    boolean isSerial;

    @Column(length = 50)
    String procedureName;


    String standardFileName;

    String standardFilepath;

    boolean isUpload = false;

    @Transient
    private List<ControlPoint> controlPointList;


    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerial(boolean serial) {
        isSerial = serial;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }


    public String getStandardFileName() {
        return standardFileName;
    }

    public void setStandardFileName(String standardFileName) {
        this.standardFileName = standardFileName;
    }

    public String getStandardFilepath() {
        return standardFilepath;
    }

    public void setStandardFilepath(String standardFilepath) {
        this.standardFilepath = standardFilepath;
    }

    public List<ControlPoint> getControlPointList() {
        return controlPointList;
    }

    public void setControlPointList(List<ControlPoint> controlPointList) {
        this.controlPointList = controlPointList;
    }
}
