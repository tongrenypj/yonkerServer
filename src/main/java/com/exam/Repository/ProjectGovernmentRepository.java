package com.exam.Repository;

import com.exam.model.entity.ProjectGovernment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/3/22.
 */
public interface ProjectGovernmentRepository extends JpaRepository<ProjectGovernment, Integer> {
    public List<ProjectGovernment> findByGovernmentId(String governmentId);
}
