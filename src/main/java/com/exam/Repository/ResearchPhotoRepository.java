package com.exam.Repository;

import com.exam.model.entity.ResearchPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mac on 2017/7/16.
 */

public interface ResearchPhotoRepository extends JpaRepository<ResearchPhoto, Integer> {
    public List<ResearchPhoto> findByProjectId(int projectId);


}
