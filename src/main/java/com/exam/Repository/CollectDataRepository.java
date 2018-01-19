package com.exam.Repository;

import com.exam.model.entity.CollectData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/3/26.
 */
public interface CollectDataRepository extends JpaRepository<CollectData, Integer> {

    public List<CollectData> findByControlPointIdAndProjectId(int controlPoint, int projectId);


    public List<CollectData> findByProjectIdAndFieldIdAndProcedureIdAndProStepAndNextStepAndControlPointId
            (Integer projectId, Integer FieldId, Integer procedureId, Integer proStep, Integer nextStep, Integer controlPointId);


    public List<CollectData> findByProjectIdAndFieldIdAndProcedureIdAndStepAndControlPointId
            (Integer projectId, Integer FieldId, Integer procedureId, Integer step, Integer controlPointId);

    public List<CollectData> findByProjectIdAndFieldIdAndProcedureIdAndControlPointIdAndStepAndProStepAndNextStep
            (Integer projectId, Integer FieldId, Integer procedureId, Integer controlPointId, Integer step, Integer proStep, Integer nextStep);

    public CollectData findByDataId(Integer dataId);

    public List<CollectData> findByFieldId(Integer fieldId);


}
