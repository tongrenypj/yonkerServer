package com.exam.controller.useraccess;

import com.exam.Repository.FieldRepository;
import com.exam.Repository.ProjectGovernmentRepository;
import com.exam.Repository.ProjectRepository;
import com.exam.Repository.UserRepository;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.Field;
import com.exam.model.entity.Project;
import com.exam.model.entity.ProjectGovernment;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-ewtu on 2017/5/6.
 */
@RestController
@RequestMapping("/api/government")
public class GovernmentController {
    @Autowired
    private ProjectGovernmentRepository projectGovernmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FieldRepository fieldRepository;

    //获取项目列表
    @RequestMapping(value = "/findProject")
    public String findManager(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            List<ProjectGovernment> projectGovernmentList = projectGovernmentRepository.findByGovernmentId(jsonRequest.getUserId());
            List<Project> projectList = new ArrayList<Project>();
            if (projectGovernmentList != null && projectGovernmentList.size() > 0) {
                for (ProjectGovernment projectGovernment : projectGovernmentList) {
                    projectList.add(projectRepository.findByProjectId(projectGovernment.getProjectId()));
                }
            }
            jsonResult.setData(gson.toJson(projectList));
            jsonResult.setResult(1);
            jsonResult.setMsg("政府部门所负责的项目列表");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);

    }

    //获取田块列表
    @RequestMapping(value = "/findFieldList")
    public String findFieldList(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        msg = jsonRequest.getData();
        Project project = gson.fromJson(msg, Project.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            List<Field> fieldList = fieldRepository.findByProjectId(project.getProjectId());
            jsonResult.setData(gson.toJson(fieldList));
            jsonResult.setResult(1);
            jsonResult.setMsg("政府部门所负责的田块列表");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);

    }


    //获取田块位置列表
    @RequestMapping(value = "/findFieldLocation")
    public String findFieldLocation(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        msg = jsonRequest.getData();
        Project project = gson.fromJson(msg, Project.class);

        if (validateLevelJsonRequest(jsonRequest)) {
            List<Field> fieldList = fieldRepository.findByProjectId(project.getProjectId());
            List<String> fieldLocationList = new ArrayList<>();
            for (Field field : fieldList) {
                if (field.getCompleted() == true && !fieldLocationList.contains(field.getFieldLocation())) {

                    fieldLocationList.add(field.getFieldLocation());
                }
            }
            if (fieldLocationList != null && fieldLocationList.size() != 0) {
                jsonResult.setResult(1);
                jsonResult.setData(gson.toJson(fieldLocationList));
                jsonResult.setMsg("获取该项目的已经完成田块的位置信息列表");
            } else {

                jsonResult.setResult(0);
                jsonResult.setMsg("没有该项目已经完成田块的位置信息！");

            }


        } else {

            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");

        }
        return gson.toJson(jsonResult);
    }


    //筛选田块列表
    @RequestMapping(value = "/selectField")
    public String selectField(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        msg = jsonRequest.getData();


        //1.传入项目id 2.污染程度或者田块位置或者啥都不传
        Field field = gson.fromJson(msg, Field.class);
        List<Field> fieldList = new ArrayList<Field>();
        Project project = new Project();
        project.setProjectId(field.getProjectId());

        double size = 0;
        if (validateLevelJsonRequest(jsonRequest)) {

            fieldList = fieldRepository.findByProjectId(field.getProjectId());
            List<Field> fields = new ArrayList<>();
            for (Field field0 : fieldList) {

                if (field0.getCompleted() != false) {
                    fields.add(field0);
                }

            }
            size = fields.size();

            if (field.getFieldLocation() == null && field.getFieldPollutionLevel() == null) {

                if (fields.size() == 0) {

                    project.setFieldSize(0);
                    project.setRate(0);
                } else {
                    project.setFieldSize(fields.size());
                    project.setRate(1);

                }
                project.setFieldList(fields);
                jsonResult.setData(gson.toJson(project));
                jsonResult.setResult(1);
                jsonResult.setMsg("政府部门所负责的该项目下的已完成的所有田块列表");
            } else if (field.getFieldLocation() != null && field.getFieldPollutionLevel() == null) {

                List<Field> fieldList1 = new ArrayList<>();
                for (Field field1 : fields) {


                    if (field1.getFieldLocation().equals(field.getFieldLocation())) {
                        fieldList1.add(field1);
                    }
                }

                if (fieldList1.size() == 0) {

                    project.setFieldSize(0);
                    project.setRate(0);
                } else {
                    project.setFieldSize(fieldList1.size());
                    project.setRate(fieldList1.size() / size);

                }
                project.setFieldList(fieldList1);

                jsonResult.setData(gson.toJson(project));
                jsonResult.setResult(1);
                jsonResult.setMsg("政府部门所负责的该项目下的已完成的该位置的所有田块列表");

            } else if (field.getFieldLocation() == null && field.getFieldPollutionLevel() != null) {


                List<Field> fieldList1 = new ArrayList<>();
                for (Field field1 : fields) {
                    if (field1.getFieldPollutionLevel().equals(field.getFieldPollutionLevel())) {
                        fieldList1.add(field1);
                    }
                }

                if (fieldList1.size() == 0) {

                    project.setRate(0);
                    project.setFieldSize(0);

                } else {
                    project.setFieldSize(fieldList1.size());
                    project.setRate(fieldList1.size() / size);

                }
                project.setFieldList(fieldList1);
                jsonResult.setData(gson.toJson(project));
                jsonResult.setResult(1);
                jsonResult.setMsg("政府部门所负责的该项目下的已完成的该污染程度的所有田块列表");

            } else {


                List<Field> fieldList1 = new ArrayList<>();
                for (Field field1 : fields) {
                    if (field1.getFieldPollutionLevel().equals(field.getFieldPollutionLevel()) &&
                            field1.getFieldLocation().equals(field.getFieldLocation())) {
                        fieldList1.add(field1);
                    }
                }

                if (fieldList1.size() == 0) {

                    project.setRate(0);
                    project.setFieldSize(0);

                } else {
                    project.setRate(fieldList1.size() / size);
                    project.setFieldSize(fieldList1.size());

                }
                project.setFieldList(fieldList1);
                jsonResult.setData(gson.toJson(project));
                jsonResult.setResult(1);
                jsonResult.setMsg("政府部门所负责的该项目下的已完成的该污染程度与该位置的所有田块列表");


            }

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
