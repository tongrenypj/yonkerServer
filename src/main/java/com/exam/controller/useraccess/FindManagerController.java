package com.exam.controller.useraccess;

import com.exam.Repository.UserRepository;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dell-ewtu on 2017/3/12.
 */
@RestController
@RequestMapping("/api/users")
public class FindManagerController {
    @Autowired
    private UserRepository userRepository;

    //通讯录功能，查询所有项目创建者和项目实施者列表
    @RequestMapping(value = "/findManager")
    public String findManager(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        List<User> userList = userRepository.findByUserLevel(3);
        userList.addAll(userRepository.findByUserLevel(21));
        userList.addAll(userRepository.findByUserLevel(22));
        userList.addAll(userRepository.findByUserLevel(23));
        userList.addAll(userRepository.findByUserLevel(24));
        if (userList != null && userList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取所有项目创建者和实施者列表");
            jsonResult.setData(gson.toJson(userList));
            return gson.toJson(jsonResult);
        } else {

            jsonResult.setResult(0);
            jsonResult.setMsg("暂无数据");
            jsonResult.setData(null);
            return gson.toJson(jsonResult);
        }
    }
}
