package com.exam.controller.frontweb;


import com.exam.Repository.WebUserRepository;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.WebUser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mac on 2017/6/6.
 */

@RestController
@RequestMapping("/api/webUser")

public class WebUserController {

    @Autowired
    public WebUserRepository webUserRepository;

    @RequestMapping(value = "/webUserLogin")
    public String addMsg(@RequestBody String msg) {
        Gson gson = new Gson();
        WebUser webUser = gson.fromJson(msg, WebUser.class);
        JsonResult jsonResult = new JsonResult();

        if (webUser.getUserName() != null && webUser.getUserPwd() != null) {
            WebUser webUser1 = webUserRepository.findByUserNameAndUserPwd(webUser.getUserName(),
                    webUser.getUserPwd());
            if (webUser1 != null) {

                jsonResult.setResult(1);
                jsonResult.setMsg("用户名密码正确");
                return gson.toJson(jsonResult);

            } else {
                jsonResult.setResult(0);
                jsonResult.setMsg("用户名密码错误");
                return gson.toJson(jsonResult);
            }

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("请输入用户名或密码");
            return gson.toJson(jsonResult);

        }
    }
}
