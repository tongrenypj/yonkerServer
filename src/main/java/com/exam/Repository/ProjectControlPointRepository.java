package com.exam.Repository;


import com.exam.model.entity.ProjectControlPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/4/19.
 */
public interface ProjectControlPointRepository extends JpaRepository<ProjectControlPoint, Integer> {

    public List<ProjectControlPoint> findByProcedureIdAndProjectId(Integer procedureId, Integer projectId);

    public List<ProjectControlPoint> findByProjectId(Integer projectId);

    public List<ProjectControlPoint> findByProcedureIdAndProStepAndNextStep
            (Integer procedureId, Integer proStep, Integer nextStep);

    public List<ProjectControlPoint> findByProcedureIdAndStep(Integer procedureId, Integer step);

    public List<ProjectControlPoint> findByControlPointId(Integer controlPointId);


    public List<ProjectControlPoint> findByProjectIdAndProcedureIdAndProStepAndNextStep
            (Integer projectId, Integer procedureId, Integer proStep, Integer nextStep);

    public List<ProjectControlPoint> findByProjectIdAndProcedureIdAndStep
            (Integer projectId, Integer procedureId, Integer step);


}
