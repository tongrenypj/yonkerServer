package com.exam.Repository;

import com.exam.model.entity.ProjectPreResearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/7/21.
 */
public interface ProjectPreResearchRepository extends JpaRepository<ProjectPreResearch, Integer> {

    public List<ProjectPreResearch> findByProjectId(Integer projectId);


    public List<ProjectPreResearch> findByProjectIdAndPreResearchId(Integer projectId, Integer preResearchId);


}
