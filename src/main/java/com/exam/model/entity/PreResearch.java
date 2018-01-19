package com.exam.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mac on 2017/7/14.
 */

@Entity
public class PreResearch {
    @Id
    @GeneratedValue
    private Integer preResearchId;

    //1污染数据收集，2现场污染数据采样、监测 3道路交通现状，4田块流转与作物种植情况，5乡镇村组信息数据

    private String researchStepName;


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
}
