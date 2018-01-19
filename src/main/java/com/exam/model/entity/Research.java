package com.exam.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mac on 2017/7/6.
 */

@Entity
public class Research {
    @Id
    @GeneratedValue

    private Integer researchId;

    private Integer preResearchId;

    //污染数据收集与现场污染数据采样、监测

    private String researchStepName;

    //每个步骤需要的采集字段(样品编号,乡镇,全镉,水稻全镉,有效态镉,pH值)

    private String researchName;

    public Integer getResearchId() {
        return researchId;
    }

    public void setResearchId(Integer researchId) {
        this.researchId = researchId;
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
}
