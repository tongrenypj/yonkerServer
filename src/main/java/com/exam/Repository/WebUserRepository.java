package com.exam.Repository;

import com.exam.model.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 2017/6/6.
 */
public interface WebUserRepository extends JpaRepository<WebUser, Integer> {

    public WebUser findByUserNameAndUserPwd(String userName, String userPwd);
}
