package com.exam.Repository;

import com.exam.model.entity.CollectPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/3/23.
 */
public interface CollectPhotoRepository extends JpaRepository<CollectPhoto, Integer> {

    public List<CollectPhoto> findByControlPointIdAndProjectId(int controlPoint, int projectId);

    public List<CollectPhoto> findByProjectIdAndFieldIdAndProcedureIdAndStepAndControlPointId
            (int projectId, int field, int procedureId, int step, int controlPoint);

    public List<CollectPhoto> findByProjectIdAndFieldIdAndProcedureIdAndProStepAndNextStepAndControlPointId
            (int projectId, int field, int procedureId, int proStep, int nextStep, int controlPoint);


    public List<CollectPhoto> findByProcedureIdAndStep(Integer procedureId, Integer step);

    public List<CollectPhoto> findByProcedureIdAndProStepAndNextStep(Integer procedureId, Integer proStep, Integer nextStep);


}
