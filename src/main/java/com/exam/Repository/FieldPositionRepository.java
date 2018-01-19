package com.exam.Repository;

import com.exam.model.entity.FieldPosition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 2017/7/11.
 */
public interface FieldPositionRepository extends JpaRepository<FieldPosition, Integer> {

    public FieldPosition findByFieldId(Integer fieldId);
}
