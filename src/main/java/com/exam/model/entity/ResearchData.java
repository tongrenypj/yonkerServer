package com.exam.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mac on 2017/7/6.
 */
@Entity
public class ResearchData {
    @Id
    @GeneratedValue
    private Integer researchDataId;

    private Integer projectId;

    private Integer preResearchId;

    //污染数据收集(用1表示)与现场污染数据采样、监测(用2表示)

    private String researchStepName;

    private String researchName;

    private String researchValue;


    @Column(length = 1024)
    private String Text;

    private String sampleNumber;


    public Integer getResearchDataId() {
        return researchDataId;
    }

    public void setResearchDataId(Integer researchDataId) {
        this.researchDataId = researchDataId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getPreResearchId() {
        return preResearchId;
    }

    public void setPreResearchId(Integer preResearchId) {
        this.preResearchId = preResearchId;
    }

    public String getResearchStepName() {
        return researchStepName;
    }

    public void setResearchStepName(String researchStepName) {
        this.researchStepName = researchStepName;
    }

    public String getResearchName() {
        return researchName;
    }

    public void setResearchName(String researchName) {
        this.researchName = researchName;
    }

    public String getResearchValue() {
        return researchValue;
    }

    public void setResearchValue(String researchValue) {
        this.researchValue = researchValue;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
}
