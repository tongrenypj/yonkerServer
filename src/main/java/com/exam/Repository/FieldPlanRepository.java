package com.exam.Repository;

import com.exam.model.entity.FieldPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/3/12.
 */
public interface FieldPlanRepository extends JpaRepository<FieldPlan, Integer> {

    public List<FieldPlan> findByFieldId(Integer fieldId);

    public FieldPlan findByFieldPlanId(Integer fieldPlanId);

    public List<FieldPlan> findByFieldIdAndIsCompleted(Integer fieldId, Boolean isCompleted);

    public List<FieldPlan> findByProjectPlanId(Integer projectPlanId);

    public List<FieldPlan> findByProcedureId(Integer procedureId);

    public FieldPlan findByFieldIdAndStep(Integer field, Integer step);


}
