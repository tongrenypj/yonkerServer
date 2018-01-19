package com.exam.controller.useraccess;

import com.exam.Repository.*;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.Project;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2018/1/18.
 */


@RestController
@RequestMapping("/api/modify")


public class ModifyController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectQualifiedRepository projectQualifiedRepository;
    @Autowired
    private ProjectResearchRepository projectResearchRepository;
    @Autowired
    private ResearchDataRepository researchDataRepository;
    @Autowired
    private ResearchPhotoRepository researchPhotoRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;


    //第二，获取项目列表
    @RequestMapping(value = "/getProjectList")
    public String getProjectList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();
        if (userRepository.findByUserId(uuid) != null && ((userRepository.findByUserId(uuid).getUserLevel() == 22
                && userLevel == 22) || (userRepository.findByUserId(uuid).getUserLevel() == 23
                && userLevel == 23) || (userRepository.findByUserId(uuid).getUserLevel() == 24
                && userLevel == 24))) {
            //实际只需要项目创建者userId;
            //String userId = gson.fromJson(msg,String.class);
            List<Project> projectList = new ArrayList<Project>();
            switch (userLevel) {
                case 22:
                    projectList = projectRepository.findByProjectManagerId(uuid);
                    break;
                case 23:
                    projectList = projectRepository.findByProjectSkillManagerId(uuid);
                    break;
                case 24:
                    projectList = projectRepository.findByPreSaleEngineerId(uuid);
                    break;
            }

            if (projectList != null && projectList.size() != 0) {
                List<Project> projectList1 = new ArrayList<Project>();
                for (Project project : projectList) {
                    if (project.getCompleted() != true && project.getPersonCompleted() == true) {

                        projectList1.add(project);

                    }
                }
                jsonResult.setResult(1);
                jsonResult.setMsg("获取修改项目列表成功");
                jsonResult.setData(gson.toJson(projectList1));
                String result = gson.toJson(jsonResult);
                return result;
            } else {
                jsonResult.setResult(0);
                jsonResult.setMsg("该用户没有需要修改的项目列表");
                jsonResult.setData("");
                return gson.toJson(jsonResult);
            }
        }
        jsonResult.setResult(0);
        jsonResult.setMsg("你没有操作权限");
        jsonResult.setData("");
        return gson.toJson(jsonResult);
    }


    //第二，重置任务
    @RequestMapping(value = "/reset")
    public String reset(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();
        if (userRepository.findByUserId(uuid) != null && ((userRepository.findByUserId(uuid).getUserLevel() == 22
                && userLevel == 22) || (userRepository.findByUserId(uuid).getUserLevel() == 23
                && userLevel == 23) || (userRepository.findByUserId(uuid).getUserLevel() == 24
                && userLevel == 24))) {
            //实际只需要项目创建者userId;
            //String userId = gson.fromJson(msg,String.class);

            Project project = gson.fromJson(msg, Project.class);
            Project project1 = projectRepository.findByProjectId(project.getProjectId());

            switch (userLevel) {
                case 22:

                    projectQualifiedRepository.delete(projectQualifiedRepository.findByProjectId(project1.getProjectId()));
                    project1.setPersonCompleted(false);
                    project1.setCreateCompleted(false);
                    projectRepository.save(project1);

                    break;
                case 23:

                    projectResearchRepository.delete(projectResearchRepository.findByProjectId(project1.getProjectId()));
                    researchDataRepository.delete(researchDataRepository.findByProjectId(project1.getProjectId()));
                    researchPhotoRepository.delete(researchPhotoRepository.findByProjectId(project1.getProjectId()));

                    project1.setResearchCompleted(false);
                    project1.setCreateCompleted(false);
                    projectRepository.save(project1);

                    break;
                case 24:

                    projectPlanRepository.delete(projectPlanRepository.findByProjectId(project1.getProjectId()));
                    projectControlPointRepository.delete(projectControlPointRepository.findByProjectId(project1.getProjectId()));

                    project1.setProcedureCompleted(false);
                    project1.setCreateCompleted(false);
                    projectRepository.save(project1);

                    break;
            }

        }
        jsonResult.setResult(0);
        jsonResult.setMsg("你没有操作权限");
        jsonResult.setData("");
        return gson.toJson(jsonResult);
    }


}
