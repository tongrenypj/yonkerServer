package com.exam.Repository;

import com.exam.model.entity.IndustryContent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/3/22.
 */
public interface IndustryContentRepository extends JpaRepository<IndustryContent, Integer> {
    public IndustryContent findByContentId(Integer contentId);

    List<IndustryContent> findByProfessionId(Integer professionId, Sort s);
}
