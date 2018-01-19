package com.exam.controller.frontweb;


import com.exam.Repository.*;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/7/16.
 */

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private PlanProcedureRepository planProcedureRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ProjectQualifiedRepository projectQualifiedRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;
    @Autowired
    private ResearchDataRepository researchDataRepository;
    @Autowired
    private FieldExecuteRepository fieldExecuteRepository;
    @Autowired
    private CollectDataRepository collectDataRepository;

    //导出项目基本信息
//    @RequestMapping(value = "/exportProjectInFo")
//    public void exportProjectInFo(HttpServletResponse response,@RequestBody String msg) throws IOException {
//        Gson gson = new Gson();
//
//        Type listType = new TypeToken<List<Project>>() {
//        }.getType();
//
//        List<Project> projectList = gson.fromJson(msg, listType);
//
//
//        //创建HSSFWorkbook对象(excel的文档对象)
//        HSSFWorkbook wb = new HSSFWorkbook();
//        //建立新的sheet对象（excel的表单）
//        HSSFSheet sheet = wb.createSheet("项目表");
//        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        HSSFRow row1 = sheet.createRow(0);
//        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        HSSFCell cell = row1.createCell(0);
//        //设置单元格内容
//        cell.setCellValue("项目一览表");
//        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));
//        //在sheet里创建第二行
//        HSSFRow row2 = sheet.createRow(1);
//        //创建单元格并设置单元格内容
//
//
//        row2.createCell(0).setCellValue("项目编号");
//        row2.createCell(1).setCellValue("项目名称");
//        row2.createCell(2).setCellValue("项目来源");
//        row2.createCell(3).setCellValue("业主单位");
//        row2.createCell(4).setCellValue("污染物类型");
//        row2.createCell(5).setCellValue("目标污染物");
//        row2.createCell(6).setCellValue("修复技术");
//        row2.createCell(7).setCellValue("项目地点");
//        row2.createCell(8).setCellValue("项目面积");
//
//        row2.createCell(9).setCellValue("实施单位");
//        row2.createCell(10).setCellValue("项目开始时间");
//        row2.createCell(11).setCellValue("项目结束时间");
//        row2.createCell(12).setCellValue("监管单位");
//        row2.createCell(13).setCellValue("服务模式ID");
//        row2.createCell(14).setCellValue("基础信息录入人员");
//        row2.createCell(15).setCellValue("项目负责人");
//        row2.createCell(16).setCellValue("项目技术负责人");
//        row2.createCell(17).setCellValue("售前工程师");
//
//
//        for(int i = 0;i < projectList.size();i++){
//
//            HSSFRow row = sheet.createRow(i+2);
//            Project project = projectList.get(i);
//
//
//            row.createCell(0).setCellValue(project.getProjectNumber());
//            row.createCell(1).setCellValue(project.getProjectName());
//            row.createCell(2).setCellValue(project.getProjectResource());
//            row.createCell(3).setCellValue(project.getHostCompany());
//            row.createCell(4).setCellValue(project.getProjectPollutionType());
//            row.createCell(5).setCellValue(project.getProjectPollutionTarget());
//            row.createCell(6).setCellValue(project.getProjectPlanName());
//            row.createCell(7).setCellValue(project.getProjectLocation());
//            row.createCell(8).setCellValue(project.getProjectArea());
//
//            row.createCell(9).setCellValue(project.getProjectExecuteCompany());
//            row.createCell(10).setCellValue(project.getProjectStartTime());
//            row.createCell(11).setCellValue(project.getProjectEndTime());
//            row.createCell(12).setCellValue(project.getProjectMonitorCompany());
//            row.createCell(13).setCellValue(project.getProjectService());
//            row.createCell(14).setCellValue(project.getBaseInFoManagerName());
//            row.createCell(15).setCellValue(project.getManagerName());
//            row.createCell(16).setCellValue(project.getSkillManagerName());
//            row.createCell(17).setCellValue(project.getPreSaleEngineerName());
//
//        }
//        //输出Excel文件
//        OutputStream output = response.getOutputStream();
//        response.reset();
//        response.setHeader("Content-disposition", "attachment; filename=details.xls");
//        response.setContentType("application/msexcel");
//        wb.write(output);
//        output.close();
//
//    }


    //批量导出多个项目基本信息
    @RequestMapping(value = "/exportProjectList")
    public String exportProjectList(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        Project newProjectMsg = gson.fromJson(msg, Project.class);
        String projectName = newProjectMsg.getProjectName();
        List<Project> projectList = null;
        List<Project> projectList1 = new ArrayList<>();
        if (projectName.equals("")) {
            projectList = projectRepository.findAll();
        } else {
            projectList = projectRepository.findByProjectNameLike(projectName + "%");
        }

        for (Project project : projectList) {
            if (project.getCreateCompleted() == true) {

                String ManagerName = userRepository.findByUserId(project.getProjectManagerId()).getUserName();
                String SkillManagerName = userRepository.findByUserId(project.getProjectSkillManagerId()).getUserName();
                String PreSaleEngineerName = userRepository.findByUserId(project.getPreSaleEngineerId()).getUserName();
                String BaseInFoManagerName = userRepository.findByUserId(project.getBaseInFoManagerId()).getUserName();
                project.setManagerName(ManagerName);
                project.setSkillManagerName(SkillManagerName);
                project.setPreSaleEngineerName(PreSaleEngineerName);
                project.setBaseInFoManagerName(BaseInFoManagerName);
                projectList1.add(project);

            }
        }
        if (projectList1 != null && projectList1.size() != 0) {

            jsonResult.setMsg("获取导出项目基础信息列表");
            jsonResult.setResult(1);
            jsonResult.setProjectList(projectList1);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setMsg("无项目信息");
            jsonResult.setResult(0);
            return gson.toJson(jsonResult);
        }

    }


    @RequestMapping(value = "/exportFieldList")
    public String exportFieldList(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        //映射实体类
        Project project = gson.fromJson(msg, Project.class);
        List<Field> fieldList = fieldRepository.findByProjectId(project.getProjectId());
        if (fieldList != null && fieldList.size() != 0) {
            for (Field field : fieldList) {
                String FieldQualifiedName = userRepository.findByUserId(field.getFieldQualifiedId()).getUserName();
                String ProjectName = projectRepository.findByProjectId(field.getProjectId()).getProjectName();
                field.setProjectName(ProjectName);
                field.setFieldQualifiedName(FieldQualifiedName);
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("获取导出田块基础信息列表");
            jsonResult.setFieldList(fieldList);

        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("返回田块列表为空");
        }
        return gson.toJson(jsonResult);
    }


    //第三，点击项目列表中的项目，获取项目详情
    @RequestMapping(value = "/exportProjectDetail")
    public String exportProjectDetail(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        Project project = gson.fromJson(msg, Project.class);

        //通过工程列表名找到相应project
        Project project1 = projectRepository.findByProjectId(project.getProjectId());

        if (project1 != null) {

            //该项目的基础信息


            String BaseInfoName = userRepository.findByUserId(project1.getBaseInFoManagerId()).getUserName();

            String ManagerName = userRepository.findByUserId(project1.getProjectManagerId()).getUserName();

            String SkillManagerName = userRepository.findByUserId(project1.getProjectSkillManagerId()).getUserName();

            String PreSaleEngineerName = userRepository.findByUserId(project1.getPreSaleEngineerId()).getUserName();

            project1.setBaseInFoManagerName(BaseInfoName);

            project1.setManagerName(ManagerName);

            project1.setSkillManagerName(SkillManagerName);

            project1.setPreSaleEngineerName(PreSaleEngineerName);


            //该项目的技术人员


            List<ProjectQualified> projectQualifiedList = projectQualifiedRepository.findByProjectId(project1.getProjectId());


            //该项目的工艺路线
            List<ProjectPlan> projectPlanList = projectPlanRepository.findByProjectId(project.getProjectId());

            for (ProjectPlan projectPlan : projectPlanList) {
                PlanProcedure planProcedure = planProcedureRepository.findByProcedureId(projectPlan.getProcedureId());
                Plan plan = planRepository.findByPlanId(planProcedure.getPlanId());
                projectPlan.setPlanName(plan.getPlanName());

                List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.findByProcedureIdAndProjectId(
                        projectPlan.getProcedureId(), projectPlan.getProjectId()
                );

                List<ProjectControlPoint> projectControlPointList1 = new ArrayList<>();
                //除去一个项目中同名工序但step不同的控制点条目
                if (projectPlan.getStep() != 0) {
                    for (ProjectControlPoint projectControlPoint : projectControlPointList) {
                        if (projectControlPoint.getStep() == projectPlan.getStep()) {
                            projectControlPointList1.add(projectControlPoint);

                        }
                    }
                } else {

                    for (ProjectControlPoint projectControlPoint : projectControlPointList) {
                        if (projectControlPoint.getProStep() == projectPlan.getStep() &&
                                projectControlPoint.getNextStep() == projectPlan.getNextStep()) {
                            projectControlPointList1.add(projectControlPoint);

                        }
                    }

                }


                projectPlan.setProjectControlPointList(projectControlPointList1);
            }


            //该项目前期调研数据

            List<ResearchData> researchDataList = researchDataRepository.findByProjectId(project.getProjectId());

            List<ResearchData> researchDataList1 = new ArrayList<>();
            for (ResearchData researchData : researchDataList) {
                if (researchData.getResearchName() == null || !researchData.getResearchName().equals("样品编号")) {
                    researchDataList1.add(researchData);
                }

            }
            for (ResearchData researchData : researchDataList1) {

                if (researchData.getText() == null) {
                    researchData.setText("");
                } else {
                    researchData.setResearchName("");
                    researchData.setResearchValue("");
                    researchData.setSampleNumber("");
                }

            }

            List<Project> projectList = new ArrayList<>();

            //project1.setProjectQualifiedList(projectQualifiedList);
            //project1.setProjectPlanList(projectPlanList);
            //project1.setResearchDataList(researchDataList1);

            projectList.add(project1);

            jsonResult.setProjectList(projectList);

            jsonResult.setProjectQualifiedList(projectQualifiedList);
            jsonResult.setProjectPlanList(projectPlanList);
            jsonResult.setResearchDataList(researchDataList1);
            jsonResult.setResult(1);
            jsonResult.setMsg("返回该项目的所有相关信息");

            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("没有该项目，请传入正确的项目id");
            return gson.toJson(jsonResult);

        }
    }


    @RequestMapping(value = "/exportFieldListDetail")
    public String exportFieldListDetail(@RequestBody String msg) {


        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        Field field = gson.fromJson(msg, Field.class);
        Field field1 = fieldRepository.findByFieldId(field.getFieldId());

        if (field1 != null) {

            String FieldQualifiedName = userRepository.findByUserId(field1.getFieldQualifiedId()).getUserName();
            String ProjectName = projectRepository.findByProjectId(field1.getProjectId()).getProjectName();
            field1.setProjectName(ProjectName);
            field1.setFieldQualifiedName(FieldQualifiedName);


            List<FieldExecute> fieldExecuteList = fieldExecuteRepository.findByFieldId(field1.getFieldId());

            List<CollectData> collectDataList = collectDataRepository.findByFieldId(field1.getFieldId());


            List<Field> fieldList = new ArrayList<>();
            //field1.setFieldExecuteList(fieldExecuteList);
            //field1.setCollectDataList(collectDataList);

            fieldList.add(field1);

            jsonResult.setFieldList(fieldList);

            jsonResult.setFieldExecuteList(fieldExecuteList);
            jsonResult.setCollectDataList(collectDataList);
            jsonResult.setResult(1);
            jsonResult.setMsg("返回该田块的所有相关信息");
            return gson.toJson(jsonResult);


        } else {

            jsonResult.setResult(0);
            jsonResult.setMsg("没有该田块，请传入正确的田块id");
            return gson.toJson(jsonResult);

        }

    }
}
