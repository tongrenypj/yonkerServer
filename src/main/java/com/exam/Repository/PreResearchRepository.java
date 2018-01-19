package com.exam.Repository;

import com.exam.model.entity.PreResearch;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 2017/7/14.
 */
public interface PreResearchRepository extends JpaRepository<PreResearch, Integer> {

    public PreResearch findByResearchStepName(String researchStepName);

    public PreResearch findByPreResearchId(Integer preResearchId);

}
