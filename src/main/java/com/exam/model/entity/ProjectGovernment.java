package com.exam.model.entity;

import javax.persistence.*;

/**
 * Created by mac on 2017/3/8.
 */
@Entity
public class ProjectGovernment {
    private Integer projectGovernmentId;
    private Integer projectId;
    private String governmentId;//技术员userId,属于项目实施者，权限为3
    private String governmentPhone;//技术员电话
    private String governmentName;//技术员名字

    @Id
    @GeneratedValue
    @Basic
    @Column(name = "projectGovernmentId")
    public Integer getProjectGovernmentId() {
        return projectGovernmentId;
    }

    public void setProjectGovernmentId(Integer projectGovernmentId) {
        this.projectGovernmentId = projectGovernmentId;
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
    @Column(name = "governmentId")
    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    @Basic
    @Column(name = "governmentPhone")

    public String getGovernmentPhone() {
        return governmentPhone;
    }

    public void setGovernmentPhone(String governmentPhone) {
        this.governmentPhone = governmentPhone;
    }

    @Basic
    @Column(name = "governmentName")
    public String getGovernmentName() {
        return governmentName;
    }

    public void setGovernmentName(String governmentName) {
        this.governmentName = governmentName;
    }
}
