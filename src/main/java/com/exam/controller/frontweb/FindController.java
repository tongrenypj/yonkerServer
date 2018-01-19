package com.exam.controller.frontweb;

import com.exam.Repository.*;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-ewtu on 2017/3/10.
 */
@RestController
@RequestMapping("/api/find")
public class FindController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectQualifiedRepository projectQualifiedRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldExecuteRepository fieldExecuteRepository;
    @Autowired
    private ControlPointRepository controlPointRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;
    @Autowired
    private PlanProcedureRepository planProcedureRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private CollectDataRepository collectDataRepository;
    @Autowired
    private CollectPhotoRepository collectPhotoRepository;
    @Autowired
    private FieldPlanRepository fieldPlanRepository;
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private PreResearchRepository preResearchRepository;
    @Autowired
    private ResearchPhotoRepository researchPhotoRepository;
    @Autowired
    private ResearchDataRepository researchDataRepository;
    @Autowired
    private ProjectPreResearchRepository projectPreResearchRepository;


    //第二，通过方案名模糊查找方案，连带方案包含的工序一起返回
    @RequestMapping(value = "/findPlan")
    public String findProcedure(@RequestBody java.lang.String msg) {
        Gson gson = new Gson();
        Plan newUserMsg = gson.fromJson(msg, Plan.class);
        JsonResult jsonResult = new JsonResult();
        if (newUserMsg.getPlanName().equals("")) {
            Sort s = new Sort(Sort.Direction.DESC, "planId");
            List<Plan> planList = planRepository.findAll(s);
            getProcedureList(planList);
            jsonResult.setResult(1);
            jsonResult.setMsg("成功返回");
            jsonResult.setPlanList(planList);
            return gson.toJson(jsonResult);

        } else {
            Sort s = new Sort(Sort.Direction.DESC, "planId");
            List<Plan> planList = planRepository.findByPlanNameLike(newUserMsg.getPlanName() + "%", s);
            getProcedureList(planList);
            jsonResult.setResult(1);
            jsonResult.setMsg("成功返回");
            jsonResult.setPlanList(planList);
            return gson.toJson(jsonResult);
        }
    }

    private List<Plan> getProcedureList(List<Plan> planList) {
        for (int i = 0; i < planList.size(); i++) {
            int planId = planList.get(i).getPlanId();
            List<PlanProcedure> procedureList = planProcedureRepository.findByPlanId(planId);
            if (procedureList != null && procedureList.size() != 0) {
                planList.get(i).setPlanProcedureList(procedureList);
            } else {
                planList.get(i).setPlanProcedureList(null);
            }
        }
        return planList;
    }

    //第二，获取某个工序控制点名称列表

    /**
     * @param msg 通过工序id找到该工序的所有控制点
     * @return 返回该工序的控制点名称列表
     */

    @RequestMapping(value = "/findControlPoint")
    public String addMsg(@RequestBody String msg) {
        Gson gson = new Gson();
        PlanProcedure planProcedure = gson.fromJson(msg, PlanProcedure.class);

        List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(planProcedure.getProcedureId());

        JsonResult jsonResult = new JsonResult();
        jsonResult.setControlPointList(controlPointList);
        jsonResult.setResult(1);
        jsonResult.setMsg("获取控制点列表包含控制点名称");
        return gson.toJson(jsonResult);

    }

    //第三，通过控制点Id获取采集数据列表

    /**
     * @param msg controlPointId
     * @return
     */
    @RequestMapping(value = "/findCollect")
    public String findCollect(@RequestBody String msg) {
        Gson gson = new Gson();
        Collect newCollectMsg = gson.fromJson(msg, Collect.class);
        List<Collect> collectList = collectRepository.findByControlPointId(newCollectMsg.getControlPointId());
        JsonResult jsonResult = new JsonResult();
        if (collectList != null && collectList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取采集数据字段列表");
            jsonResult.setCollectList(collectList);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("无采集数据列表");
            jsonResult.setCollectList(collectList);
            return gson.toJson(jsonResult);
        }
    }

    @RequestMapping(value = "/findPreResearch")
    public String findPreResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        List<PreResearch> preResearchList = preResearchRepository.findAll();
        JsonResult jsonResult = new JsonResult();
        if (preResearchList != null && preResearchList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取前期调研任务列表");
            jsonResult.setPreResearchList(preResearchList);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("无获取前期调研任务列表");
            jsonResult.setPreResearchList(preResearchList);
            return gson.toJson(jsonResult);
        }
    }


    @RequestMapping(value = "/findResearch")
    public String findResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        Research research = gson.fromJson(msg, Research.class);
        List<Research> researchList = researchRepository.findByResearchStepName(research.getResearchStepName());
        JsonResult jsonResult = new JsonResult();
        if (researchList != null && researchList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取采集数据列表");
            jsonResult.setResearchList(researchList);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("无采集数据列表");
            jsonResult.setResearchList(researchList);
            return gson.toJson(jsonResult);
        }
    }


    //1、	项目基础信息查询 ，通过工程名模糊查找已经创建完成的工程
    @RequestMapping(value = "/findProjectBaseIoFo")
    public String findProjectBaseIoFo(@RequestBody String msg) {

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

            jsonResult.setMsg("获取项目基础信息列表");
            jsonResult.setResult(1);
            jsonResult.setProjectList(projectList1);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setMsg("无项目信息");
            jsonResult.setResult(1);
            jsonResult.setProjectList(null);
            return gson.toJson(jsonResult);
        }
    }
    //2、	项目工艺路线、工序、控制点信息查询

    @RequestMapping(value = "/findProjectPlanControlPoint")
    public String findProjectPlanControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        Project project = gson.fromJson(msg, Project.class);

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

        jsonResult.setMsg("获得项目工艺工序、控制点信息");
        jsonResult.setResult(1);
        jsonResult.setProjectPlanList(projectPlanList);
        return gson.toJson(jsonResult);

    }

    //3、	项目人员查询

    @RequestMapping(value = "/findQualified")
    public String findQualified(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        Project project = gson.fromJson(msg, Project.class);

        List<ProjectQualified> projectQualifiedList = projectQualifiedRepository.findByProjectId(project.getProjectId());
        if (projectQualifiedList != null && projectQualifiedList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取所有技术人员列表");
            jsonResult.setProjectQualifiedList(projectQualifiedList);

        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("暂无数据");

        }
        return gson.toJson(jsonResult);

    }
    //4、 售前工程师前期调研信息查询,获取前期调研控制点数据,传过来ControlPointId和ProjectId


//    @RequestMapping(value = "/findCollectData")
//    public String findCollectData(@RequestBody String msg) {
//
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//
//        //传过来ControlPointId和ProjectId
//        CollectData collectData = gson.fromJson(msg, CollectData.class);
//        //if(collectData.getControlPointId()==0 && collectData.getProjectId()==0)
//
//        List<CollectData> collectDataList = collectDataRepository.findByControlPointIdAndProjectId(collectData.getControlPointId()
//                , collectData.getProjectId());
//
//        jsonResult.setResult(1);
//        jsonResult.setMsg("返回前期调研该控制点采集的数据");
//        jsonResult.setCollectDataList(collectDataList);
//
//        return gson.toJson(jsonResult);
//    }

    //4、 售前工程师前期调研信息查询,获取前期调研控制点数据

    @RequestMapping(value = "/findCollectData")
    public String findCollectData(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        //projectId fieldId nextStep proStep step procedureId ControlPointId

        CollectData collectData = gson.fromJson(msg, CollectData.class);

        if (collectData.getProcedureId() != null && collectData.getStep() != null) {

            if (collectData.getStep() != 0) {

                List<CollectData> collectDataList = collectDataRepository.findByProjectIdAndFieldIdAndProcedureIdAndStepAndControlPointId
                        (collectData.getProjectId(), collectData.getFieldId(), collectData.getProcedureId(),
                                collectData.getStep(), collectData.getControlPointId());

                jsonResult.setResult(1);
                jsonResult.setMsg("返回前期调研该控制点采集的数据");
                jsonResult.setCollectDataList(collectDataList);


            } else {
                List<CollectData> collectDataList = collectDataRepository.findByProjectIdAndFieldIdAndProcedureIdAndProStepAndNextStepAndControlPointId(
                        collectData.getProjectId(), collectData.getFieldId(), collectData.getProcedureId(),
                        collectData.getProStep(), collectData.getNextStep(), collectData.getControlPointId());
                jsonResult.setResult(1);
                jsonResult.setMsg("返回前期调研该控制点采集的数据");
                jsonResult.setCollectDataList(collectDataList);
            }
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("参数有误");

        }

        return gson.toJson(jsonResult);
    }

    //5、售前工程师前期调研信息查询,获取前期调研控制点图片
    @RequestMapping(value = "/findCollectPhoto")
    public String findCollectPhoto(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();


        //projectId fieldId nextStep proStep step procedureId ControlPointId

        CollectPhoto collectPhoto = gson.fromJson(msg, CollectPhoto.class);

        if (collectPhoto.getProcedureId() != null && collectPhoto.getStep() != null) {

            if (collectPhoto.getStep() != 0) {

                List<CollectPhoto> collectPhotoList = collectPhotoRepository.findByProjectIdAndFieldIdAndProcedureIdAndStepAndControlPointId
                        (collectPhoto.getProjectId(), collectPhoto.getFieldId(), collectPhoto.getProcedureId(),
                                collectPhoto.getStep(), collectPhoto.getControlPointId());

                jsonResult.setResult(1);
                jsonResult.setMsg("返回前期调研该控制点采集的图片");
                jsonResult.setCollectPhotoList(collectPhotoList);


            } else {
                List<CollectPhoto> collectPhotoList = collectPhotoRepository.findByProjectIdAndFieldIdAndProcedureIdAndProStepAndNextStepAndControlPointId(
                        collectPhoto.getProjectId(), collectPhoto.getFieldId(), collectPhoto.getProcedureId(),
                        collectPhoto.getProStep(), collectPhoto.getNextStep(), collectPhoto.getControlPointId());
                jsonResult.setResult(1);
                jsonResult.setMsg("返回前期调研该控制点采集的图片");
                jsonResult.setCollectPhotoList(collectPhotoList);
            }
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("参数有误");

        }

        return gson.toJson(jsonResult);

    }


    //6、 根据项目名查询该项目某田块的所有工序，工序详情中展示采集的所有数据和照片

    @RequestMapping(value = "/findProjectControlPoint")
    public String findProjectControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        //projectId isSerial nextStep proStep step procedureId


        FieldPlan fieldPlan = gson.fromJson(msg, FieldPlan.class);
        if (fieldPlan.getIsSerial() == true) {

            List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.findByProjectIdAndProcedureIdAndStep
                    (fieldPlan.getProjectId(), fieldPlan.getProcedureId(), fieldPlan.getStep());


            if (projectControlPointList.size() != 0) {
                jsonResult.setProjectControlPointList(projectControlPointList);
                jsonResult.setResult(1);
                jsonResult.setMsg("返回该项目该串行工序的控制点列表");
            } else {

                jsonResult.setResult(0);
                jsonResult.setMsg("没有控制点列表");

            }

        } else {


            List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.findByProjectIdAndProcedureIdAndProStepAndNextStep
                    (fieldPlan.getProjectId(), fieldPlan.getProcedureId(), fieldPlan.getProStep(), fieldPlan.getNextStep());


            if (projectControlPointList.size() != 0) {
                jsonResult.setProjectControlPointList(projectControlPointList);
                jsonResult.setResult(1);
                jsonResult.setMsg("返回该项目该并行工序的控制点列表");
            } else {

                jsonResult.setResult(0);
                jsonResult.setMsg("没有控制点列表");

            }

        }
        return gson.toJson(jsonResult);
    }


//    //6、 根据项目名查询该项目某田块的所有工序，工序详情中展示采集的所有数据和照片
//
//    @RequestMapping(value = "/findFiledCollectData")
//    public String findFiledCollectData(@RequestBody String msg) {
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//
//        //fieldId 和 projectId
//        CollectData collectData = gson.fromJson(msg,CollectData.class);
//
//        List<FieldPlan> fieldPlanList = fieldPlanRepository.findByFieldId(collectData.getFieldId());
//
//
//        if (fieldPlanList.size() != 0 ) {
//            for (FieldPlan fieldPlan:fieldPlanList){
//                if(fieldPlan.getIsSerial()==true){
//                    List<CollectData> collectDataList = collectDataRepository.findByProcedureIdAndStep(fieldPlan.getProcedureId()
//                    ,fieldPlan.getStep());
//                    List<CollectPhoto> collectPhotoList =collectPhotoRepository.findByProcedureIdAndStep(fieldPlan.getProcedureId()
//                    ,fieldPlan.getStep());
//
//                    fieldPlan.setCollectDataList(collectDataList);
//                    fieldPlan.setCollectPhotoList(collectPhotoList);
//
//
//                }
//                else {
//                    List<CollectData> collectDataList = collectDataRepository.findByProcedureIdAndProStepAndNextStep(
//                            fieldPlan.getProcedureId(),fieldPlan.getProStep(),fieldPlan.getNextStep());
//                    List<CollectPhoto> collectPhotoList = collectPhotoRepository.findByProcedureIdAndProStepAndNextStep(
//                            fieldPlan.getProcedureId(),fieldPlan.getProStep(),fieldPlan.getNextStep()
//                    );
//
//                    fieldPlan.setCollectDataList(collectDataList);
//                    fieldPlan.setCollectPhotoList(collectPhotoList);
//                }
//
//            }
//
//            jsonResult.setResult(1);
//            jsonResult.setMsg("返回该项目该田块所有工序列表");
//            jsonResult.setFieldPlanList(fieldPlanList);
//        } else {
//
//            jsonResult.setResult(1);
//            jsonResult.setMsg("该项目没有田块工序列表");
//
//        }
//        return gson.toJson(jsonResult);
//    }

    //7、项目田块信息查询

    @RequestMapping(value = "/findField")
    public String findField(@RequestBody String msg) {

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
                field.setFieldExecuteList(fieldExecuteRepository.findByFieldId(field.getFieldId()));
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("返回田块列表");
            jsonResult.setFieldList(fieldList);

        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("返回田块列表为空");
        }
        return gson.toJson(jsonResult);
    }

    @RequestMapping(value = "/findFieldPlan")
    public String findFieldPlan(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        //映射实体类
        Field field = gson.fromJson(msg, Field.class);
        List<FieldPlan> fieldPlanList = fieldPlanRepository.findByFieldId(field.getFieldId());
        if (fieldPlanList != null && fieldPlanList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("返回田块列表");
            jsonResult.setFieldPlanList(fieldPlanList);

        } else {
            jsonResult.setResult(1);
            jsonResult.setFieldList(null);
            jsonResult.setMsg("返回田块列表为空");
        }
        return gson.toJson(jsonResult);
    }


    //前期调研


    //1、获取一个项目的前期调研任务
    @RequestMapping(value = "/findProjectPreResearch")
    public String findProjectPreResearch(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        Project project = gson.fromJson(msg, Project.class);

        List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectId(project.getProjectId());
        List<ProjectPreResearch> projectPreResearchList1 = new ArrayList<>();

        List<String> stringList = new ArrayList<>();

        for (ProjectPreResearch projectPreResearch : projectPreResearchList) {

            if (!stringList.contains(projectPreResearch.getResearchStepName())) {
                ProjectPreResearch projectPreResearch1 = new ProjectPreResearch();
                stringList.add(projectPreResearch.getResearchStepName());
                projectPreResearch1.setPreResearchId(projectPreResearch.getPreResearchId());
                projectPreResearch1.setResearchStepName(projectPreResearch.getResearchStepName());
                projectPreResearch1.setProjectId(projectPreResearch.getProjectId());
                projectPreResearchList1.add(projectPreResearch1);
            }

        }

        jsonResult.setResult(1);
        jsonResult.setProjectPreResearchList(projectPreResearchList1);
        jsonResult.setMsg("返回该项目的前期调研任务列表");
        return gson.toJson(jsonResult);

    }

//    @RequestMapping(value = "/findResearchDetailedData")
//    public String findResearchDetailedData(@RequestBody String msg) {
//
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//
//        //(1projectId;2PreResearchId;3SampleNumber)
//        ResearchData researchData = gson.fromJson(msg, ResearchData.class);
//
//        if (researchData.getProjectId() != null && researchData.getPreResearchId() != null && researchData.getSampleNumber()!=null) {
//
//            List<ResearchData> researchDataList = researchDataRepository.findByProjectIdAndPreResearchIdAndSampleNumber(
//                    researchData.getProjectId(), researchData.getPreResearchId(), researchData.getSampleNumber());
//
//            jsonResult.setResult(1);
//            jsonResult.setMsg("返回样品编号列表");
//            jsonResult.setResearchDataList(researchDataList);
//            return gson.toJson(jsonResult);
//        } else {
//            jsonResult.setResult(0);
//            jsonResult.setMsg("参数有误，请输入项目id，调研任务Id，及样品编号");
//            return gson.toJson(jsonResult);
//
//        }
//    }


    //2、前期调研获取项目任务数据
    @RequestMapping(value = "/findResearchData")
    public String getResearchData(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        //1、projectId;2、preResearchId（污染数据收集与现场污染数据采样、监测researchStepName)3ResearchName

        ProjectPreResearch projectPreResearch = gson.fromJson(msg, ProjectPreResearch.class);


        //判断该控制点是否需要按字段收集
        List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectIdAndPreResearchId
                (projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());
        if (projectPreResearchList.size() != 0 && projectPreResearchList.get(0).getResearchName() != null) {

            if (projectPreResearch.getProjectId() != null && projectPreResearch.getPreResearchId() != null) {

                List<ResearchData> researchDataList = researchDataRepository.findByProjectIdAndPreResearchId(
                        projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());

                List<ResearchData> researchDataList1 = new ArrayList<>();
                for (ResearchData researchData1 : researchDataList) {
                    if (!researchData1.getResearchName().equals("样品编号")) {

                        researchDataList1.add(researchData1);
                    }

                }

                jsonResult.setResult(1);
                jsonResult.setMsg("返回该任务的样品数据列表");
                jsonResult.setResearchDataList(researchDataList1);
                return gson.toJson(jsonResult);
            } else {
                jsonResult.setResult(0);
                jsonResult.setMsg("参数有误，请输入项目id，调研任务Id");
                return gson.toJson(jsonResult);

            }

        } else {
            if (projectPreResearch.getProjectId() != null && projectPreResearch.getPreResearchId() != null) {

                List<ResearchData> researchData1 = researchDataRepository.findByProjectIdAndPreResearchId(
                        projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());

                jsonResult.setResult(1);
                jsonResult.setMsg("返回该该任务的文本数据");
                jsonResult.setResearchDataList(researchData1);
                return gson.toJson(jsonResult);
            } else {

                jsonResult.setResult(0);
                jsonResult.setMsg("参数有误，请输入项目id，调研任务id");
                return gson.toJson(jsonResult);
            }
        }
    }


    //3、前期调研项目获取采集照片
    @RequestMapping(value = "/findResearchPhoto")
    public String getResearchPhoto1(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        ResearchPhoto researchPhoto = gson.fromJson(msg, ResearchPhoto.class);

        if (researchPhoto.getProjectId() != 0) {
            List<ResearchPhoto> researchPhotoList = researchPhotoRepository.findByProjectId(researchPhoto.getProjectId());
            jsonResult.setResult(1);
            jsonResult.setResearchPhotoList(researchPhotoList);
            jsonResult.setMsg("返回该项目前期调研图片列表");
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("请传入ProjectId");
            return gson.toJson(jsonResult);
        }

    }

}
