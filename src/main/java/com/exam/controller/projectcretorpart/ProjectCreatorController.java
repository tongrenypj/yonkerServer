package com.exam.controller.projectcretorpart;

import com.exam.MyWebAppConfigurer;
import com.exam.Repository.*;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2017/4/12.
 */

@RestController
@RequestMapping("/api/creators")
public class ProjectCreatorController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectQualifiedRepository projectQualifiedRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanProcedureRepository planProcedureRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private ControlPointRepository controlPointRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;
    @Autowired
    private CollectDataRepository collectDataRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private CollectPhotoRepository collectPhotoRepository;
    @Autowired
    private ProjectGovernmentRepository projectGovernmentRepository;
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private ProjectPreResearchRepository projectPreResearchRepository;
    @Autowired
    private PreResearchRepository preResearchRepository;
    @Autowired
    private FieldExecuteRepository fieldExecuteRepository;
    @Autowired
    private FieldRepository fieldRepository;

    //第一，基础信息录入人员(level = 21)接口，获取项目负责人员列表、获取技术人员列表、获取售前人员列表
    @RequestMapping(value = "/inputProject/21/getMemberList")
    public String getMemberList(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 21
                || userLevel != 21) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是基础信息录入人员，没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        List<User> userList1 = userRepository.findByUserLevel(22);
        List<User> userList2 = userRepository.findByUserLevel(23);
        List<User> userList3 = userRepository.findByUserLevel(24);

        Map<String, List<User>> maps = new HashMap<>();
        maps.put("ManagerName", userList1);
        maps.put("SkillManagerName", userList2);
        maps.put("SaleEngineerName", userList3);

        jsonResult.setResult(1);
        jsonResult.setMsg("获取项目负责人员列表、获取技术人员列表、获取售前人员列表");
        jsonResult.setData(gson.toJson(maps));

        return gson.toJson(jsonResult);

    }


    //第二，基础信息录入人员(level = 21)接口，录入项目基础信息
    @RequestMapping(value = "/inputProject/21/addBaseIoFo")
    public String addBaseIoFo(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 21
                || userLevel != 21) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是基础信息录入人员，没有该操作的权限");
            return gson.toJson(jsonResult);
        }
        //映射实体类
        //新建工程时必须传来该工程的所有信息
        Project project = gson.fromJson(msg, Project.class);
        //数据库对象
        if (project.getProjectName() == null) {

            jsonResult.setResult(0);
            jsonResult.setMsg("请传入工程名");
            jsonResult.setData("");
            return gson.toJson(jsonResult);

        }


        //录入多个政府部门

        Project project1 = projectRepository.findByProjectNumber(project.getProjectNumber());
        if (project1 == null) {
            projectRepository.save(project);


            Project project2 = projectRepository.findByProjectNumber(project.getProjectNumber());

            List<PreResearch> preResearchList = preResearchRepository.findAll();

            for (PreResearch preResearch : preResearchList) {

                List<Research> researchList = researchRepository.findByPreResearchId(preResearch.getPreResearchId());
                if (researchList != null && researchList.size() != 0) {
                    for (Research research : researchList) {
                        ProjectPreResearch projectPreResearch = new ProjectPreResearch();
                        projectPreResearch.setProjectId(project2.getProjectId());
                        projectPreResearch.setPreResearchId(research.getPreResearchId());
                        projectPreResearch.setResearchStepName(research.getResearchStepName());
                        projectPreResearch.setResearchName(research.getResearchName());
                        projectPreResearchRepository.save(projectPreResearch);
                    }

                } else {
                    ProjectPreResearch projectPreResearch = new ProjectPreResearch();
                    projectPreResearch.setProjectId(project2.getProjectId());
                    projectPreResearch.setResearchStepName(preResearch.getResearchStepName());
                    projectPreResearch.setPreResearchId(preResearch.getPreResearchId());
                    projectPreResearchRepository.save(projectPreResearch);


                }

            }
            //录入政府人员信息
            List<ProjectGovernment> projectGovernmentList = project.getProjectGovernmentList();
            for (ProjectGovernment projectGovernment : projectGovernmentList) {
                projectGovernment.setProjectId(project2.getProjectId());
                projectGovernmentRepository.save(projectGovernment);
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("工程信息录入成功");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("该项目编号已存在，录入失败");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }
    }


    //第三，项目负责人(level = 22)接口获取实施者列表，返回列表以便项目负责人选取田块创建者，也就是实施者中的技术员

    @RequestMapping(value = "/inputProject/22/getQualifiedList")
    public String getQualifiedList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 22
                || userLevel != 22) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是项目负责人人员，没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        List<User> userList = userRepository.findByUserLevel(3);

        if (userList != null && userList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取项目所有实施者列表");
            jsonResult.setData(gson.toJson(userList));

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("暂无数据");
            jsonResult.setData("");
        }
        return gson.toJson(jsonResult);
    }

    //第四，各个创建者获取尚未创建完成完成的项目列表
    @RequestMapping(value = "/inputProject/unCompletedList")
    public String getIsPersonCompletedList(@RequestBody String msg) {

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

            List<Project> projectList1 = new ArrayList<Project>();
            if (userLevel == 22) {
                List<Project> projectList = projectRepository.findByProjectManagerId(uuid);

                if (projectList != null && projectList.size() != 0) {
                    for (Project project : projectList) {

                        if (project.getPersonCompleted() == false) {
                            Project project1 = new Project();
                            project1.setProjectId(project.getProjectId());
                            project1.setProjectName(project.getProjectName());
                            projectList1.add(project1);
                        }
                    }
                    jsonResult.setResult(1);
                    jsonResult.setMsg("返回尚未操作的项目列表");
                    jsonResult.setData(gson.toJson(projectList1));
                } else {
                    jsonResult.setResult(0);
                    jsonResult.setMsg("没有未操作的项目列表");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);
                }
            }

            if (userLevel == 23) {
                List<Project> projectList = projectRepository.findByProjectSkillManagerId(uuid);
                if (projectList != null && projectList.size() != 0) {
                    for (Project project : projectList) {

                        if (project.getProcedureCompleted() == false) {
                            Project project1 = new Project();
                            project1.setProjectId(project.getProjectId());
                            project1.setProjectName(project.getProjectName());
                            projectList1.add(project1);
                        }
                    }
                    jsonResult.setResult(1);
                    jsonResult.setMsg("返回尚未操作的项目列表");
                    jsonResult.setData(gson.toJson(projectList1));
                } else {
                    jsonResult.setResult(0);
                    jsonResult.setMsg("没有未操作的项目列表");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);
                }
            }

            if (userLevel == 24) {
                List<Project> projectList = projectRepository.findByPreSaleEngineerId(uuid);
                if (projectList != null && projectList.size() != 0) {
                    for (Project project : projectList) {

                        if (project.getResearchCompleted() == false) {
                            Project project1 = new Project();
                            project1.setProjectId(project.getProjectId());
                            project1.setProjectName(project.getProjectName());
                            projectList1.add(project1);
                        }
                    }
                    jsonResult.setResult(1);
                    jsonResult.setMsg("返回尚未操作的项目列表");
                    jsonResult.setData(gson.toJson(projectList1));
                } else {
                    jsonResult.setResult(0);
                    jsonResult.setMsg("没有未操作的项目列表");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);
                }

            }
            return gson.toJson(jsonResult);
        }
        jsonResult.setResult(0);
        jsonResult.setMsg("你没有操作权限");
        jsonResult.setData("");
        return gson.toJson(jsonResult);
    }


    //第五，项目负责人(level = 22)接口，项目负责人在基础信息录入后负责进行人员分配，选择田块创建者，也就是实施者中的技术员

    /**
     * @param msg 传入 projectQualifiedList
     * @return
     */
    @RequestMapping(value = "/inputProject/22/selectQualified")
    public String selectQualified(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 22
                || userLevel != 22) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是项目负责人人员，没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        Type listType = new TypeToken<List<ProjectQualified>>() {
        }.getType();

        List<ProjectQualified> projectQualifiedList = gson.fromJson(msg, listType);

        if (projectQualifiedList != null && projectQualifiedList.size() != 0) {

            for (ProjectQualified projectQualified : projectQualifiedList) {
                projectQualifiedRepository.save(projectQualified);
            }
            Integer projectId = projectQualifiedList.get(0).getProjectId();
            Project project = projectRepository.findByProjectId(projectId);
            project.setPersonCompleted(true);
            if (project.getResearchCompleted() == true && project.getProcedureCompleted() == true) {

                project.setCreateCompleted(true);

            }
            projectRepository.save(project);
            jsonResult.setResult(1);
            jsonResult.setMsg("分配田块创建者成功，写入技术员列表");
            jsonResult.setData("");
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("传入的技术员列表为空，分配失败");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }

    }

    //第六， 该项目的技术人员负责人，权限为23，获取方案工序控制点列表 以便项目的工艺路线、工序的选择

    @RequestMapping(value = "/inputProject/23/getProcedureControlPoint")
    public String getProcedureControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        Plan plan = gson.fromJson(msg, Plan.class);

        if (plan.getPlanName().equals("")) {
            Sort s = new Sort(Sort.Direction.DESC, "planId");
            List<Plan> planList = planRepository.findAll(s);
            if (planList == null && planList.size() == 0) {
                jsonResult.setResult(0);
                jsonResult.setMsg("返回方案列表为空");
                jsonResult.setData(gson.toJson(planList));

            } else {

                for (Plan plan1 : planList) {
                    List<PlanProcedure> planProcedureList = planProcedureRepository.findByPlanId(plan1.getPlanId());
                    for (PlanProcedure planProcedure : planProcedureList) {
                        List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(planProcedure.getProcedureId());
                        planProcedure.setControlPointList(controlPointList);
                    }
                    plan1.setPlanProcedureList(planProcedureList);
                }

                jsonResult.setResult(1);
                jsonResult.setMsg("返回全部方案以及工序控制点列表");
                jsonResult.setData(gson.toJson(planList));

            }
            return gson.toJson(jsonResult);
        } else {
            Sort s = new Sort(Sort.Direction.DESC, "planId");
            List<Plan> planList = planRepository.findByPlanNameLike(plan.getPlanName() + "%", s);
            if (planList == null && planList.size() == 0) {
                jsonResult.setResult(0);
                jsonResult.setMsg("返回方案列表为空");
                jsonResult.setData(gson.toJson(planList));

            } else {
                for (Plan plan1 : planList) {
                    List<PlanProcedure> planProcedureList = planProcedureRepository.findByPlanId(plan1.getPlanId());

                    for (PlanProcedure planProcedure : planProcedureList) {
                        List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(planProcedure.getProcedureId());
                        planProcedure.setControlPointList(controlPointList);

                    }
                    plan1.setPlanProcedureList(planProcedureList);
                }
                jsonResult.setResult(1);
                jsonResult.setMsg("返回模糊查询方案以及工序控制点列表");
                jsonResult.setData(gson.toJson(planList));
            }
            return gson.toJson(jsonResult);
        }
    }

    //创建者选择串行工序并行工序之后选择控制点的那块
    @RequestMapping(value = "/getControlPointListByProcedureId")
    public String getControlPointListByProcedureId(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();
        ProjectPlan plan = gson.fromJson(msg, ProjectPlan.class);
        List<ControlPoint> controlPointList = new ArrayList<>();
        if (plan.getProcedureId() != null) {
            controlPointList = controlPointRepository.findByProcedureId(plan.getProcedureId());
            jsonResult.setResult(1);
            jsonResult.setMsg("工序控制点列表获取成功");
            jsonResult.setData(gson.toJson(controlPointList));
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("工序控制点列表获取失败");
            jsonResult.setData(gson.toJson(controlPointList));
        }


        return gson.toJson(jsonResult);
    }


    //第七，该项目的技术人员负责人，权限为23，负责该项目方案工序控制点的设定

    @RequestMapping(value = "/inputProject/23/setProcedureControlPoint")
    public String setProcedureControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 23
                || userLevel != 23) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是该项目的技术负责人，没有该操作的权限");
            return gson.toJson(jsonResult);
        }


        Project project = gson.fromJson(msg, Project.class);
        List<ProjectPlan> projectPlanList = project.getProjectPlanList();

        List<ProjectControlPoint> projectControlPointList = project.getProjectControlPointList();


        if (projectPlanList != null && projectPlanList.size() != 0) {

            for (ProjectPlan projectPlan : projectPlanList) {
                projectPlanRepository.save(projectPlan);
            }
            for (ProjectControlPoint projectControlPoint : projectControlPointList) {
                projectControlPointRepository.save(projectControlPoint);

            }

            Integer projectId = project.getProjectId();
            Project project1 = projectRepository.findByProjectId(projectId);
            project1.setProcedureCompleted(true);

            if (project1.getResearchCompleted() == true && project1.getPersonCompleted() == true) {

                project1.setCreateCompleted(true);

            }
            projectRepository.save(project1);
            jsonResult.setResult(1);
            jsonResult.setMsg("该项目的技术工序控制点设定成功，写入方案工序列表");
            jsonResult.setData("");
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("传入的技术工序列表为空，分配失败");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }

    }


    //第八，该项目的售前工程师，权限为24，负责该项目前期调研,获取前期调研某个控制点数据


//    @RequestMapping(value = "/inputProject/24/getCollectData")
//    public String getCollectData(@RequestBody String msg) {
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
//
//        String uuid = jsonRequest.getUserId();
//        int userLevel = jsonRequest.getUserLevel();
//        msg = jsonRequest.getData();
//
//        if (userRepository.findByUserId(uuid) != null && ((userRepository.findByUserId(uuid).getUserLevel() == 21 && userLevel == 21)
//                || (userRepository.findByUserId(uuid).getUserLevel() == 22 && userLevel == 22)
//                || (userRepository.findByUserId(uuid).getUserLevel() == 23 && userLevel == 23)
//                || (userRepository.findByUserId(uuid).getUserLevel() == 24 && userLevel == 24))) {
//
//            //传过来ControlPointId和ProjectId
//            CollectData collectData = gson.fromJson(msg, CollectData.class);
//            List<CollectData> collectDataList = collectDataRepository.findByControlPointIdAndProjectId(
//                    collectData.getControlPointId(), collectData.getProjectId());
//            if (collectDataList == null || collectDataList.size() == 0) {
//                List<Collect> collectList = collectRepository.findByControlPointId(collectData.getControlPointId());
//                for (Collect collect : collectList) {
//                    CollectData collectData1 = new CollectData();
//                    collectData1.setCollectionName(collect.getCollectionName());
//                    collectData1.setCollectionWeight(collect.getCollectionWeight());
//                    collectData1.setControlPointId(collect.getControlPointId());
//                    collectDataList.add(collectData1);
//
//                }
//                jsonResult.setResult(1);
//                jsonResult.setMsg("返回控制点采集数据的名称和单位");
//                jsonResult.setData(gson.toJson(collectDataList));
//            } else {
//                jsonResult.setResult(1);
//                jsonResult.setMsg("返回控制点采集数据的名称和单位和数据");
//                jsonResult.setData(gson.toJson(collectDataList));
//            }
//        } else {
//            jsonResult.setResult(0);
//            jsonResult.setMsg("你不是该项目的售前工程师，没有该操作的权限");
//        }
//        return gson.toJson(jsonResult);
//
//
//    }

    //第九，该项目的售前工程师，权限为24，负责该项目前期调研,修改前期调研某个控制点图片数据
    @RequestMapping(value = "/inputProject/24/setCollectPhoto")

    public String setCollectPhoto(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        String currentTime = String.valueOf(System.currentTimeMillis());
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        //msg是json格式的字符串
        String msg = params.get("CollectPhoto");

        //传入采集图片的基本信息
        CollectPhoto collectPhoto = gson.fromJson(msg, CollectPhoto.class);
        //collectPhoto.setCollectTime(df.format(new Date()));

        collectPhoto.setPath(fileName);

        if (!file.isEmpty()) {
            try {

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.CollectPhotosPath
                        + fileName)));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();

                jsonResult.setResult(0);
                jsonResult.setMsg("上传失败," + e.getMessage());
                return gson.toJson(jsonResult);
            }
            collectPhotoRepository.save(collectPhoto);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传采集照片成功");
            jsonResult.setData(fileName);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("上传失败，因为文件是空的。");
            return gson.toJson(jsonResult);
        }
    }


//    //第九，该项目的售前工程师，权限为24，负责该项目前期调研,修改前期调研某个控制点数据
//    @RequestMapping(value = "/inputProject/24/setCollectData")
//
//    public String setCollectData(@RequestBody String msg) {
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
//
//        String uuid = jsonRequest.getUserId();
//        int userLevel = jsonRequest.getUserLevel();
//        msg = jsonRequest.getData();
//
//
//        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 24
//                || userLevel != 24) {
//            jsonResult.setResult(0);
//            jsonResult.setMsg("你不是该项目的售前工程师，没有该操作的权限");
//            return gson.toJson(jsonResult);
//
//        }
//
//        Type listType = new TypeToken<List<CollectData>>() {
//        }.getType();
//        List<CollectData> collectDataList = gson.fromJson(msg, listType);
//
//
//        collectDataRepository.save(collectDataList);
//
//        jsonResult.setResult(1);
//        jsonResult.setMsg("保存控制点采集数据成功");
//        jsonResult.setData("");
//        return gson.toJson(jsonResult);
//
//    }

    //第十，该项目的售前工程师，权限为24，负责该项目前期调研，提交该工程的前期调研

    @RequestMapping(value = "/inputProject/24/SubmitResearch")
    public String SubmitResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 24
                || userLevel != 24) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是该项目的售前工程师，没有该操作的权限");
            return gson.toJson(jsonResult);

        }
        Integer projectId = gson.fromJson(msg, Project.class).getProjectId();
        Project project = projectRepository.findByProjectId(projectId);
        project.setResearchCompleted(true);


        if (project.getProcedureCompleted() && project.getPersonCompleted()) {

            project.setCreateCompleted(true);

        }
        projectRepository.save(project);
        jsonResult.setResult(1);
        jsonResult.setMsg("该项目前期调研完成");
        jsonResult.setData("");
        project = null;
        projectId = null;
        return gson.toJson(jsonResult);

    }

    //第十一，level22，23，24管理员点项目列表中的项目,通过工程列表名找到相应project,获取项目基础信息详情
    @RequestMapping(value = "/getProjectBaseDetail")
    public String getProjectBaseDetail(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) != null && (userRepository.findByUserId(uuid).getUserLevel() == 22
                && userLevel == 22) || (userRepository.findByUserId(uuid).getUserLevel() == 23
                && userLevel == 23) || (userRepository.findByUserId(uuid).getUserLevel() == 24
                && userLevel == 24) || (userRepository.findByUserId(uuid).getUserLevel() == 4
                && userLevel == 4)) {

            Project project = gson.fromJson(msg, Project.class);
            Project project1 = projectRepository.findByProjectId(project.getProjectId());
            if (project1 != null) {
                String BaseInfoName = userRepository.findByUserId(project1.getBaseInFoManagerId()).getUserName();

                String ManagerName = userRepository.findByUserId(project1.getProjectManagerId()).getUserName();

                String SkillManagerName = userRepository.findByUserId(project1.getProjectSkillManagerId()).getUserName();

                String PreSaleEngineerName = userRepository.findByUserId(project1.getPreSaleEngineerId()).getUserName();

                project1.setBaseInFoManagerName(BaseInfoName);

                project1.setManagerName(ManagerName);

                project1.setSkillManagerName(SkillManagerName);

                project1.setPreSaleEngineerName(PreSaleEngineerName);

                jsonResult.setResult(1);
                jsonResult.setMsg("返回该项目基础信息");
                jsonResult.setData(gson.toJson(project1));
                return gson.toJson(jsonResult);
            } else {
                jsonResult.setData("");
                jsonResult.setResult(0);
                jsonResult.setMsg("无信息");
                return gson.toJson(jsonResult);

            }
        }
        jsonResult.setResult(0);
        jsonResult.setMsg("你没有该操作的权限");
        return gson.toJson(jsonResult);
    }

    //第十二，项目负责人(level = 21)接口获取政府部门列表

    @RequestMapping(value = "/inputProject/21/getQualifiedList")
    public String getGovernmentList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 21
                || userLevel != 21) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你不是项目负责人人员，没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        List<User> userList = userRepository.findByUserLevel(4);

        if (userList != null && userList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取政府部门列表");
            jsonResult.setData(gson.toJson(userList));

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("暂无数据");
            jsonResult.setData("");
        }
        return gson.toJson(jsonResult);
    }

    //获取项目实施者列表
    @RequestMapping(value = "/findExecutor")
    public String findExecutor(@RequestBody String msg) {
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
        if (userList != null && userList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取实施者列表");
            jsonResult.setData(gson.toJson(userList));
            return gson.toJson(jsonResult);
        } else {

            jsonResult.setResult(0);
            jsonResult.setMsg("暂无数据");
            jsonResult.setData(null);
            return gson.toJson(jsonResult);
        }
    }


//    @RequestMapping(value = "/showProjectNumber")
//    public String showProjectNumber(@RequestBody String msg) {
//        Gson gson = new Gson();
//        JsonResult jsonResult = new JsonResult();
//
//        Project project = new Project();
//        int i = 0;
//        int j = 0;
//
//        List<Project> projectList = projectRepository.findAll();
//        for (Project project1 : projectList) {
//
//            if (project1.getCompleted() == true) {
//                i++;
//            } else {
//                if (project1.getCreateCompleted() == true)//zgs  修改
//                    j++;
//            }
//
//        }
//        project.setCompletedNumber(i);
//        project.setUnCompletedNumber(j);
//
//        jsonResult.setResult(1);
//        jsonResult.setMsg("返回项目已经完成的个数和未完成的个数");
//        jsonResult.setData(gson.toJson(project));
//        return gson.toJson(jsonResult);
//
//
//    }


    @RequestMapping(value = "/showProjectNumber")
    public String showProjectNumber(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) != null && ((userRepository.findByUserId(uuid).getUserLevel() == 21
                && userLevel == 21) || (userRepository.findByUserId(uuid).getUserLevel() == 22
                && userLevel == 22) || (userRepository.findByUserId(uuid).getUserLevel() == 23
                && userLevel == 23) || (userRepository.findByUserId(uuid).getUserLevel() == 24
                && userLevel == 24) || (userRepository.findByUserId(uuid).getUserLevel() == 3
                && userLevel == 3))) {
            //实际只需要项目创建者userId;
            //String userId = gson.fromJson(msg,String.class);
            List<Project> projectList = new ArrayList<Project>();
            switch (userLevel) {
                case 21:
                    projectList = projectRepository.findByBaseInFoManagerId(uuid);
                    break;
                case 22:
                    projectList = projectRepository.findByProjectManagerId(uuid);
                    break;
                case 23:
                    projectList = projectRepository.findByProjectSkillManagerId(uuid);
                    break;
                case 24:
                    projectList = projectRepository.findByPreSaleEngineerId(uuid);
                    break;
                case 3:
                    List<ProjectQualified> projectQualifiedList = projectQualifiedRepository.findByQualifiedId(uuid);
                    List<FieldExecute> fieldExecuteList = fieldExecuteRepository.findByExecutorId(uuid);
                    if (projectQualifiedList.size() != 0 || fieldExecuteList.size() != 0) {
                        //遍历fieldExecuteList
                        for (FieldExecute fieldExecute : fieldExecuteList) {

                            //通过田块id找到田块
                            Integer fieldId = fieldExecute.getFieldId();

                            Field field = fieldRepository.findByFieldId(fieldId);

                            if (field != null) {
                                //通过田块找到对应项目
                                Integer projectId = field.getProjectId();
                                Project project = projectRepository.findByProjectId(projectId);

                                //把实施人员项目加入到项目列表中
                                if (project != null && !projectList.contains(project) && project.getCreateCompleted()) {

                                    //安卓端通过此列表来获知哪些是技术人员
                                    project.setProjectQualifiedList(projectQualifiedRepository.findByProjectId(project.getProjectId()));
                                    projectList.add(project);

                                }
                            }
                        }


                        for (ProjectQualified projectQualified : projectQualifiedList) {


                            //通过工程Id找到对应项目
                            Integer projectId = projectQualified.getProjectId();
                            Project project = projectRepository.findByProjectId(projectId);


                            if (project != null && !projectList.contains(project) && project.getCreateCompleted()) {

                                project.setProjectQualifiedList(projectQualifiedRepository.findByProjectId(project.getProjectId()));
                                projectList.add(project);
                            }

                        }

                    }
                    break;
            }


            Project project = new Project();
            int i = 0;
            int j = 0;

            for (Project project1 : projectList) {

                if (project1.getCreateCompleted() == true) {

                    if (project1.getCompleted() == true) {
                        i++;
                    } else {

                        j++;
                    }
                }

            }
            project.setCompletedNumber(i);
            project.setUnCompletedNumber(j);

            jsonResult.setResult(1);
            jsonResult.setMsg("返回项目已经完成的个数和未完成的个数");
            jsonResult.setData(gson.toJson(project));
            return gson.toJson(jsonResult);


        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有操作权限");
            return gson.toJson(jsonResult);
        }
    }


}
