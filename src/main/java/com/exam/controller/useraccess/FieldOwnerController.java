package com.exam.controller.useraccess;

import com.exam.Repository.FieldRepository;
import com.exam.Repository.UserRepository;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.Field;
import com.exam.model.entity.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dell-ewtu on 2017/5/6.
 */
@RestController
@RequestMapping("/api/fieldOwner")
public class FieldOwnerController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldRepository fieldRepository;

    //获取田块列表
    @RequestMapping(value = "/findFieldList")
    public String findFieldList(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            User user = userRepository.findByUserId(jsonRequest.getUserId());
            List<Field> fieldList = fieldRepository.findByFieldOwnerPhone(user.getUserPhone());
            jsonResult.setData(gson.toJson(fieldList));
            jsonResult.setResult(1);
            jsonResult.setMsg("土地所有者获取属于自己的田块列表");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);

    }


    //身份验证
    private boolean validateLevelJsonRequest(JsonRequest jsonRequest) {
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {
            return false;
        } else {
            return true;
        }
    }
}
