package com.exam.controller.useraccess;

import com.exam.Repository.*;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mac on 2017/5/7.
 */


@RestController
@RequestMapping("/api/ProjectOverView")
public class ProjectOverViewController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectPlanRepository projectPlanRepository;

    @Autowired
    FieldPlanRepository fieldPlanRepository;

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectControlPointRepository projectControlPointRepository;

    @RequestMapping("/getOverView")
    public String getOverView(@RequestBody String msg) {


        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        msg = jsonRequest.getData();
        int userLevel = jsonRequest.getUserLevel();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        Project project = gson.fromJson(msg, Project.class);
        Project project1 = projectRepository.findByProjectId(project.getProjectId());
        List<ProjectPlan> projectPlanList = projectPlanRepository.findByProjectId(project1.getProjectId());
        for (ProjectPlan projectPlan : projectPlanList) {
            List<FieldPlan> fieldPlanList = fieldPlanRepository.findByProjectPlanId(projectPlan.getProjectPlanId());
            Double countFieldArea = 0.0;
            Integer fieldCompleteNumber = 0;
            for (FieldPlan fieldPlan : fieldPlanList) {
                if (fieldPlan.getIsCompleted()) {
                    Field field = fieldRepository.findByFieldId(fieldPlan.getFieldId());
                    countFieldArea = countFieldArea + field.getFieldArea();
                    fieldCompleteNumber++;
                }
            }
            if (fieldPlanList.size() != 0) {
                if (fieldCompleteNumber == fieldPlanList.size()) {
                    projectPlan.setSchedule(1);
                } else {
                    projectPlan.setSchedule(countFieldArea / project1.getProjectArea());
                }
            } else {
                projectPlan.setSchedule(0);
            }

            if (projectPlan.getIsSerial() == true) {

                List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.
                        findByProcedureIdAndStep(projectPlan.getProcedureId(), projectPlan.getStep());
                projectPlan.setProjectControlPointList(projectControlPointList);

            } else {

                List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.
                        findByProcedureIdAndProStepAndNextStep(projectPlan.getProcedureId(), projectPlan.getProStep(),
                                projectPlan.getNextStep());
                projectPlan.setProjectControlPointList(projectControlPointList);
            }

        }


        project1.setProjectPlanList(projectPlanList);
        jsonResult.setResult(1);
        jsonResult.setMsg("返回项目概览");
        jsonResult.setData(gson.toJson(project1));

        return gson.toJson(jsonResult);
    }


}
