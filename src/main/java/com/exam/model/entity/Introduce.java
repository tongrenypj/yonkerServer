package com.exam.model.entity;

import javax.persistence.*;

/**
 * Created by mac on 2017/3/21.
 */
@Entity
public class Introduce {
    private Integer sectionId;
    private String imagePath;

    private String sectionText;
    private String sectionTitle;

    @Id
    @GeneratedValue
    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    @Basic
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Basic
    @Column(length = 1024)

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    @Basic
    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
}
