package com.exam.Repository;

import com.exam.model.entity.RepairSituation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 2017/3/22.
 */
public interface RepairSituationRepository extends JpaRepository<RepairSituation, Integer> {
    public RepairSituation findBySituationId(Integer situationId);

    public RepairSituation findByThumbImage(String thumbImage);
}
