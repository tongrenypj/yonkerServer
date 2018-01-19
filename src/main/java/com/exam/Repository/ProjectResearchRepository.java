package com.exam.Repository;

import com.exam.model.entity.ProjectResearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/7/19.
 */
public interface ProjectResearchRepository extends JpaRepository<ProjectResearch, Integer> {


    public List<ProjectResearch> findByProjectIdAndPreResearchId(Integer projectId, Integer preResearchId);

    public List<ProjectResearch> findByProjectId(Integer projectId);


}
