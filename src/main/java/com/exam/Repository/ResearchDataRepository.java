package com.exam.Repository;

import com.exam.model.entity.ResearchData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/7/10.
 */
public interface ResearchDataRepository extends JpaRepository<ResearchData, Integer> {

    public List<ResearchData> findByProjectIdAndPreResearchIdAndResearchName
            (Integer projectId, Integer preResearchId, String researchName);

    public List<ResearchData> findByProjectIdAndPreResearchIdAndSampleNumber
            (Integer projectId, Integer preResearchId, String sampleNumber);

    public List<ResearchData> findByProjectIdAndPreResearchId(Integer projectId, Integer preResearchId);

    public List<ResearchData> findByProjectId(Integer projectId);

    public ResearchData findByResearchDataId(Integer researchDataId);


}
