package com.exam.Repository;

import com.exam.model.entity.Research;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/7/6.
 */
public interface ResearchRepository extends JpaRepository<Research, Integer> {


    public List<Research> findByResearchStepName(String researchStepName);

    public List<Research> findByPreResearchId(Integer preResearchId);


}
