package com.exam.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mac on 2017/7/15.
 */
@Entity
public class ResearchPhoto {
    @Id
    @GeneratedValue

    private Integer researchPhotoId;

    private int projectId;

    private String researchPhotoPath;


    public Integer getResearchPhotoId() {
        return researchPhotoId;
    }

    public void setResearchPhotoId(Integer researchPhotoId) {
        this.researchPhotoId = researchPhotoId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getResearchPhotoPath() {
        return researchPhotoPath;
    }

    public void setResearchPhotoPath(String researchPhotoPath) {
        this.researchPhotoPath = researchPhotoPath;
    }

}
