package com.exam.controller.useraccess;

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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mac on 2017/3/10.
 */

@RestController
@RequestMapping("/api/executor")
public class ExecutorController {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectPlanRepository projectPlanRepository;
    @Autowired
    ProjectQualifiedRepository projectQualifiedRepository;
    @Autowired
    FieldExecuteRepository fieldExecuteRepository;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    private FieldPlanRepository fieldPlanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private ControlPointRepository controlPointRepository;
    @Autowired
    private CollectPhotoRepository collectPhotoRepository;
    @Autowired
    private CollectDataRepository collectDataRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private FieldPositionRepository fieldPositionRepository;


    //第一，项目执行者（包括技术人员和施工人员）获取工程列表
    @RequestMapping(value = "/getExecutorProject")
    public String getExecutorProject(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        User user = gson.fromJson(msg, User.class);
        //实际只需要项目实施者userId;
        //String userId = gson.fromJson(msg,String.class);

        List<ProjectQualified> projectQualifiedList = projectQualifiedRepository.findByQualifiedId(user.getUserId());
        List<FieldExecute> fieldExecuteList = fieldExecuteRepository.findByExecutorId(user.getUserId());

        List<Project> projectList = new ArrayList<Project>();
        if (projectQualifiedList.size() != 0 || fieldExecuteList.size() != 0) {
            //遍历fieldExecuteList
            for (FieldExecute fieldExecute : fieldExecuteList) {

                //通过田块id找到田块
                Integer fieldId = fieldExecute.getFieldId();
                Field field = fieldRepository.findOne(fieldId);

                if (field != null) {
                    //通过田块找到对应项目
                    Integer projectId = field.getProjectId();
                    Project project = projectRepository.findOne(projectId);

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
                Project project = projectRepository.findOne(projectId);


                if (project != null && !projectList.contains(project) && project.getCreateCompleted()) {

                    project.setProjectQualifiedList(projectQualifiedRepository.findByProjectId(project.getProjectId()));
                    projectList.add(project);
                }

            }
            jsonResult.setResult(1);
            jsonResult.setMsg("获取项目列表成功");
            jsonResult.setData(gson.toJson(projectList));

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("该用户没有项目列表");
            jsonResult.setData("");

        }
        return gson.toJson(jsonResult);
    }


    //第二，由安卓端来验证是否该用户是否属于技术人员，只有技术人员可以新增田块
    @RequestMapping(value = "/newAddField")
    public String newAddField(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        String msg = params.get("JsonRequest");


        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }
        //映射实体类
        //新建田块时必须传来田块的所有信息,projectId（除了田块id）,传入实施人员列表


        Field field = gson.fromJson(msg, Field.class);
        Field field1 = fieldRepository.findByFieldNumber(field.getFieldNumber());

        if (field1 == null) {

            User user = userRepository.findByUserPhone(field.getFieldOwnerPhone());
            //创建田块时为土地所有者注册账号
            if (user == null) {

                User user1 = new User();
                user1.setUserPhone(field.getFieldOwnerPhone());
                user1.setUserName(field.getFieldOwner());
                //注意这的密码加密
                user1.setUserPwd("e10adc3949ba59abbe56e057f20f883e");
                user1.setFirst(true);
                user1.setUserLevel(6);
                userRepository.save(user1);

                String userId = userRepository.findByUserPhone(user1.getUserPhone()).getUserId();

                UserDetail userDetail1 = new UserDetail();
                userDetail1.setUserId(userId);
                userDetail1.setUserIdentification(field.getFieldOwnerIdentification());
                userDetailRepository.save(userDetail1);
            } else {
                if (user.getUserLevel() == 1) {

                    jsonResult.setResult(0);
                    jsonResult.setMsg("该手机号已经被普通用户使用，请更换！");
                    return gson.toJson(jsonResult);
                }

                if (user.getUserLevel() == 6) {
                    if (!user.getUserName().equals(field.getFieldOwner())) {
                        user.setUserName(field.getFieldOwner());
                        userRepository.save(user);
                    }
                }
            }


            fieldRepository.save(field);
            Integer fieldId = fieldRepository.findByFieldNumber(field.getFieldNumber()).getFieldId();
            List<ProjectPlan> projectPlanList = projectPlanRepository.findByProjectId(field.getProjectId());

            //新建田块时把该田块所属项目的方案工序列表赋值给田块方案工序列表
            for (ProjectPlan projectPlan : projectPlanList) {

                FieldPlan fieldPlan = new FieldPlan();
                fieldPlan.setFieldId(fieldId);
                fieldPlan.setStep(projectPlan.getStep());
                //工序第一步是否点击设为true
                if ((projectPlan.getStep() != null && projectPlan.getStep() == 1) || (projectPlan.getStep() != null && !projectPlan.getIsSerial() && projectPlan.getProStep() == 0)) {
                    fieldPlan.setIsClick(true);
                } else {
                    fieldPlan.setIsClick(false);
                }
                fieldPlan.setIsCompleted(false);
                fieldPlan.setIsSerial(projectPlan.getIsSerial());
                fieldPlan.setNextStep(projectPlan.getNextStep());
                fieldPlan.setProStep(projectPlan.getProStep());
                fieldPlan.setProcedureName(projectPlan.getProcedureName());

                fieldPlan.setProcedureId(projectPlan.getProcedureId());
                fieldPlan.setProjectId(projectPlan.getProjectId());
                fieldPlan.setProjectPlanId(projectPlan.getProjectPlanId());
                fieldPlanRepository.save(fieldPlan);

            }


            //获取传来的实施人员列表并传入数据库
            List<FieldExecute> fieldExecuteList = field.getFieldExecuteList();
            if (fieldExecuteList != null && fieldExecuteList.size() != 0) {
                for (FieldExecute fieldExecute : fieldExecuteList) {
                    fieldExecute.setFieldId(fieldId);
                    fieldExecuteRepository.save(fieldExecute);
                }
            }

            String currentTime = String.valueOf(System.currentTimeMillis());
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);


            //传入采集图片的基本信息
            FieldPosition fieldPosition = new FieldPosition();
            fieldPosition.setFieldId(fieldId);
            fieldPosition.setPhotoPath(fileName);

            if (!file.isEmpty()) {
                try {

                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.FieldPositionPath
                            + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    fieldRepository.delete(fieldId);
                    fieldExecuteRepository.delete(fieldExecuteRepository.findByFieldId(fieldId));
                    fieldPlanRepository.delete(fieldPlanRepository.findByFieldId(fieldId));

                    jsonResult.setResult(0);
                    jsonResult.setMsg("图片上传失败," + e.getMessage());
                    return gson.toJson(jsonResult);
                }
                fieldPositionRepository.save(fieldPosition);
            } else {
                fieldRepository.delete(fieldId);
                fieldExecuteRepository.delete(fieldExecuteRepository.findByFieldId(fieldId));
                fieldPlanRepository.delete(fieldPlanRepository.findByFieldId(fieldId));

                jsonResult.setResult(0);
                jsonResult.setMsg("图片上传失败，因为文件是空的。");
                return gson.toJson(jsonResult);
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("创建田块，田块实施人员，田块工艺路线成功");
            return gson.toJson(jsonResult);


        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("失败,田块编号已存在");
            return gson.toJson(jsonResult);

        }

    }


    //第三，技术人员，施工人员通过project条目获取田块列表,加田块详情
    @RequestMapping(value = "/getFieldList")
    public String getFieldList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        //映射实体类
        Project project = gson.fromJson(msg, Project.class);
        List<Field> fieldList = fieldRepository.findByProjectId(project.getProjectId());
        List<Field> fieldList1 = new ArrayList<Field>();

        for (Field field : fieldList) {

            String fieldQualifiedId = field.getFieldQualifiedId();

            if (fieldQualifiedId != null) {
                if (fieldQualifiedId.equals(uuid))

                    fieldList1.add(field);
            }
        }
        for (Field field : fieldList) {
            FieldExecute fieldExecute = fieldExecuteRepository.findByFieldIdAndExecutorId(field.getFieldId(), uuid);
            if (fieldExecute != null) {

                Field field1 = fieldRepository.findByFieldId(fieldExecute.getFieldId());
                fieldList1.add(field1);
            }
        }

        if (fieldList1.size() != 0) {
            for (Field field : fieldList1) {

                FieldPosition fieldPosition = fieldPositionRepository.findByFieldId(field.getFieldId());
                if (fieldPosition != null) {
                    field.setPhotoPath(fieldPosition.getPhotoPath());
                }
                List<FieldExecute> fieldExecuteList = fieldExecuteRepository.findByFieldId(field.getFieldId());
                field.setFieldExecuteList(fieldExecuteList);
                List<FieldPlan> fieldPlanList = fieldPlanRepository.findByFieldId(field.getFieldId());
                field.setFieldPlanList(fieldPlanList);
                String projectName = projectRepository.findByProjectId(field.getProjectId()).getProjectName();
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("返回田块列表");
            jsonResult.setData(gson.toJson(fieldList1));

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("返回田块列表为空");
        }
        return gson.toJson(jsonResult);
    }

    //第四，获取田块工序列表
    @RequestMapping(value = "/getFieldPlanList")
    public String getFieldPlanList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            jsonRequest.setData("");
            return gson.toJson(jsonResult);
        }

        //通过FieldId获取田块工序列表

        Field field = gson.fromJson(msg, Field.class);
        List<FieldPlan> fieldPlanList = fieldPlanRepository.findByFieldId(field.getFieldId());
        jsonResult.setResult(1);
        jsonResult.setMsg("返回该田块工序列表");
        jsonResult.setData(gson.toJson(fieldPlanList));
        return gson.toJson(jsonResult);

    }


    //第五，通过工序id获取工序控制点列表
    @RequestMapping(value = "/getControlPoint")
    public String getControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) != null
                && (userRepository.findByUserId(uuid).getUserLevel() == 21 && userLevel == 21)
                || (userRepository.findByUserId(uuid).getUserLevel() == 22 && userLevel == 22)
                || (userRepository.findByUserId(uuid).getUserLevel() == 23 && userLevel == 23)
                || (userRepository.findByUserId(uuid).getUserLevel() == 24 && userLevel == 24)
                || (userRepository.findByUserId(uuid).getUserLevel() == 3 && userLevel == 3)
                || (userRepository.findByUserId(uuid).getUserLevel() == 4 && userLevel == 4)) {
            FieldPlan fieldPlan = gson.fromJson(msg, FieldPlan.class);
            List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(fieldPlan.getProcedureId());
            jsonResult.setResult(1);
            jsonResult.setMsg("返回该道工序控制点列表");
            jsonResult.setData(gson.toJson(controlPointList));
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
        }
        return gson.toJson(jsonResult);
    }

    //获取某个控制点数据
    @RequestMapping(value = "/getCollectData")
    public String getCollectData(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();
        if (userRepository.findByUserId(uuid) != null && (userRepository.findByUserId(uuid).getUserLevel() == 21 && userLevel == 21)
                || (userRepository.findByUserId(uuid).getUserLevel() == 22 && userLevel == 22)
                || (userRepository.findByUserId(uuid).getUserLevel() == 23 && userLevel == 23)
                || (userRepository.findByUserId(uuid).getUserLevel() == 24 && userLevel == 24)
                || (userRepository.findByUserId(uuid).getUserLevel() == 3 && userLevel == 3)
                || (userRepository.findByUserId(uuid).getUserLevel() == 4 && userLevel == 4)
                || (userRepository.findByUserId(uuid).getUserLevel() == 6 && userLevel == 6)) {
            CollectData collectData = gson.fromJson(msg, CollectData.class);
            List<CollectData> collectDataList = collectDataRepository.findByProjectIdAndFieldIdAndProcedureIdAndControlPointIdAndStepAndProStepAndNextStep
                    (collectData.getProjectId(), collectData.getFieldId(), collectData.getProcedureId(), collectData.getControlPointId(), collectData.getStep(), collectData.getProStep(), collectData.getNextStep());
            if (collectDataList == null || collectDataList.size() == 0) {
                List<Collect> collectList = collectRepository.findByControlPointId(collectData.getControlPointId());
                for (Collect collect : collectList) {
                    CollectData collectData1 = new CollectData();
                    collectData1.setCollectionName(collect.getCollectionName());
                    collectData1.setCollectionWeight(collect.getCollectionWeight());
                    collectData1.setControlPointId(collect.getControlPointId());
                    collectDataList.add(collectData1);
                }
                jsonResult.setResult(1);
                jsonResult.setMsg("返回控制点采集数据的名称和单位");
                jsonResult.setData(gson.toJson(collectDataList));
            } else {
                jsonResult.setResult(1);
                jsonResult.setMsg("返回控制点采集数据的名称和单位和数据");
                jsonResult.setData(gson.toJson(collectDataList));
            }
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
        }
        return gson.toJson(jsonResult);
    }

    //第七，保存实施者上传的采集的数据

    @RequestMapping(value = "/upLoadCollectData")
    public String upLoadCollectData(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }


        Type listType = new TypeToken<List<CollectData>>() {
        }.getType();

        List<CollectData> collectDataList = gson.fromJson(msg, listType);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (CollectData collectData : collectDataList) {
            //如果开始时数据库没有数据则DataId都为空，则只需要保存即可
            if (collectData.getDataId() == null) {
                collectData.setCollectTime(df.format(new Date()));
                collectDataRepository.save(collectData);
            } else {
                //获取原来数据库里面的上传者id的身份（即是技术员还是工程师）

                CollectData collectData1 = collectDataRepository.findByDataId(collectData.getDataId());


                //对比该条数据田块的创建者的userId
                Field field = fieldRepository.findByFieldQualifiedIdAndFieldId
                        (collectData1.getUpLoadUserId(), collectData1.getFieldId());
                //上传者id的身份（即是技术员还是工程师）

                Field field1 = fieldRepository.findByFieldQualifiedIdAndFieldId
                        (collectData.getUpLoadUserId(), collectData.getFieldId());

                if (field != null && field1 == null) {
                    //如果之前数据库里面的collectData是技术员上传的，且现在该条修改数据是工程师上传的则修改不成功，也不会保存到数据库
                } else {
                    //其他情况则保存collectData到数据库
                    collectData.setCollectTime(df.format(new Date()));
                    collectDataRepository.save(collectData);
                }
            }

        }

        jsonResult.setResult(1);
        jsonResult.setMsg("上传采集数据到数据库成功");
        return gson.toJson(jsonResult);

    }

    //5、获取控制点图片,传过来ControlPointId和ProjectId
    @RequestMapping(value = "/findCollectPhoto")
    public String findCollectPhoto(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) != null && (userRepository.findByUserId(uuid).getUserLevel() == 21 && userLevel == 21)
                || (userRepository.findByUserId(uuid).getUserLevel() == 22 && userLevel == 22)
                || (userRepository.findByUserId(uuid).getUserLevel() == 23 && userLevel == 23)
                || (userRepository.findByUserId(uuid).getUserLevel() == 24 && userLevel == 24)
                || (userRepository.findByUserId(uuid).getUserLevel() == 3 && userLevel == 3)
                || (userRepository.findByUserId(uuid).getUserLevel() == 4 && userLevel == 4)
                || (userRepository.findByUserId(uuid).getUserLevel() == 6 && userLevel == 6)) {

            //传过来ControlPointId和ProjectId
            CollectPhoto collectPhoto = gson.fromJson(msg, CollectPhoto.class);

            List<CollectPhoto> collectPhotoList;

            if (collectPhoto.getStep() == 0) {
                collectPhotoList = collectPhotoRepository.findByProjectIdAndFieldIdAndProcedureIdAndProStepAndNextStepAndControlPointId(
                        collectPhoto.getProjectId(), collectPhoto.getFieldId(), collectPhoto.getProcedureId(),
                        collectPhoto.getProStep(), collectPhoto.getNextStep(), collectPhoto.getControlPointId()
                );
            } else {
                collectPhotoList = collectPhotoRepository.findByProjectIdAndFieldIdAndProcedureIdAndStepAndControlPointId(
                        collectPhoto.getProjectId(), collectPhoto.getFieldId(), collectPhoto.getProcedureId(),
                        collectPhoto.getStep(), collectPhoto.getControlPointId()
                );
            }

            String str = MyWebAppConfigurer.CollectPhotosPath;
            String str1 = str.substring(3, str.length());

            for (CollectPhoto collectPhoto1 : collectPhotoList) {

                collectPhoto1.setPath(MyWebAppConfigurer.baseUrl + str1 + collectPhoto1.getPath());
            }

            jsonResult.setResult(1);
            jsonResult.setMsg("返回前期调研该控制点采集的图片");
            jsonResult.setData(gson.toJson(collectPhotoList));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }


    }

    //第六，项目实施者保存上传的采集的图片
    @RequestMapping(value = "/uploadCollectPhoto")
    public String uploadCollectPhoto(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        //msg是json格式的字符串
        String msg = params.get("CollectPhoto");

        //传入采集图片的基本信息
        CollectPhoto collectPhoto = gson.fromJson(msg, CollectPhoto.class);
        collectPhoto.setCollectTime(df.format(new Date()));
        collectPhoto.setPath(fileName);

        if (!file.isEmpty()) {
            try {

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.CollectPhotosPath + fileName)));
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


    //最复杂！！！！！实施者提交

    @RequestMapping(value = "/Submit")
    public String SubmitResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }
        //需要传入field_plan_id

        FieldPlan fieldPlan0 = gson.fromJson(msg, FieldPlan.class);

        FieldPlan fieldPlan = fieldPlanRepository.findByFieldPlanId(fieldPlan0.getFieldPlanId());

        Project project = projectRepository.findByProjectId(fieldPlan.getProjectId());

        fieldPlan.setIsCompleted(true);
        fieldPlanRepository.save(fieldPlan);

        Field field = fieldRepository.findByFieldId(fieldPlan.getFieldId());

        List<FieldPlan> fieldPlanList = fieldPlanRepository.findByFieldId(fieldPlan.getFieldId());
        field.setCompleted(true);


        if (fieldPlan.getIsSerial() == true) {

            FieldPlan fieldPlan2 = fieldPlanRepository.findByFieldIdAndStep
                    (field.getFieldId(), (fieldPlan.getStep() + 1));
            if (fieldPlan2 != null) {
                fieldPlan2.setIsClick(true);
                fieldPlanRepository.save(fieldPlan2);
                for (FieldPlan fieldPlan3 : fieldPlanList) {
                    if (fieldPlan3.getIsSerial() == false) {
                        if (fieldPlan2.getStep() == fieldPlan3.getNextStep() && !fieldPlan3.getIsCompleted()) {
                            fieldPlan2.setIsClick(false);
                            fieldPlanRepository.save(fieldPlan2);
                            break;
                        }
                    }
                }

            }


            for (FieldPlan fieldPlan3 : fieldPlanList) {

                if (fieldPlan.getStep() == fieldPlan3.getProStep()) {
                    fieldPlan3.setIsClick(true);
                    fieldPlanRepository.save(fieldPlan3);
                }

            }

        } else {
            FieldPlan fieldPlan2 = fieldPlanRepository.findByFieldIdAndStep
                    (field.getFieldId(), fieldPlan.getNextStep());
            if (fieldPlan2 != null) {
                fieldPlan2.setIsClick(true);
                fieldPlanRepository.save(fieldPlan2);

                for (FieldPlan fieldPlan3 : fieldPlanList) {
                    if (fieldPlan3.getIsSerial() == false) {
                        if (fieldPlan2.getStep() == fieldPlan3.getNextStep() && !fieldPlan3.getIsCompleted()) {
                            fieldPlan2.setIsClick(false);
                            fieldPlanRepository.save(fieldPlan2);
                            break;
                        }
                    } else {
                        if (fieldPlan2.getStep() == (fieldPlan3.getStep() + 1) && !fieldPlan3.getIsCompleted()) {
                            fieldPlan2.setIsClick(false);
                            fieldPlanRepository.save(fieldPlan2);
                            break;
                        }
                    }
                }


            }

        }
        for (FieldPlan fieldPlan1 : fieldPlanList) {
            if (!fieldPlan1.getIsCompleted()) {
                field.setCompleted(false);
            }
        }

        fieldRepository.save(field);

        project.setCompleted(true);
        List<Field> fieldList = fieldRepository.findByProjectId(project.getProjectId());
        for (Field field1 : fieldList) {
            if (field1.getCompleted() == false) {
                project.setCompleted(false);
                break;
            }
        }
        projectRepository.save(project);

        jsonResult.setResult(1);
        jsonResult.setMsg("该田块的该工序提交完成");
        jsonResult.setData("");
        return gson.toJson(jsonResult);
    }

    //第八，获取公告
    @RequestMapping(value = "getExecutorNoticeList")
    public String getExecutorNoticeList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

        List<ProjectQualified> projectQualifiedList = projectQualifiedRepository.findByQualifiedId(uuid);
        List<FieldExecute> fieldExecuteList = fieldExecuteRepository.findByExecutorId(uuid);
        List<Notice> noticeList = new ArrayList<Notice>();
        List<Integer> projectIdList = new ArrayList<Integer>();
        Sort s = new Sort(Sort.Direction.DESC, "noticeId");
        if (projectQualifiedList.size() != 0 || fieldExecuteList.size() != 0) {
            for (FieldExecute fieldExecute : fieldExecuteList) {
                Integer fieldId = fieldExecute.getFieldId();
                Field field = fieldRepository.findOne(fieldId);
                Integer projectId = field.getProjectId();
                if (!projectIdList.contains(projectId)) {
                    projectIdList.add(projectId);
                    noticeList.addAll(noticeRepository.findByProjectId(projectId, s));
                }
            }
            for (ProjectQualified projectQualified : projectQualifiedList) {
                Integer projectId = projectQualified.getProjectId();
                if (!projectIdList.contains(projectId)) {
                    projectIdList.add(projectId);
                    noticeList.addAll(noticeRepository.findByProjectId(projectId, s));
                }
            }
            jsonResult.setResult(1);
            jsonResult.setData(gson.toJson(noticeList));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("无相关公告信息");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }
    }

    //第九，获取由我提出的审批列表（工程人员提出审批）
    @RequestMapping(value = "getApplyFromList")
    public String getApplyFromList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }
        List<Apply> applyList = applyRepository.findByApplyFromId(uuid);
        if (applyList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取审批列表");
            jsonResult.setData(gson.toJson(applyList));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("无相关审批！");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }
    }

    //第十，获取待我审批的列表
    @RequestMapping(value = "getApplyToList")
    public String getApplyToList(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }
        List<Apply> applyList = applyRepository.findByApplyToId(uuid);
        //获取未审批的列表
        List<Apply> undoList = new ArrayList<Apply>();
        if (applyList.size() > 0) {
            for (Apply apply : applyList) {
                if (apply.getIsPass() == 0) {
                    undoList.add(apply);
                }
            }
        }
        if (undoList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setMsg("获取待审批列表");
            jsonResult.setData(gson.toJson(undoList));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("无相关审批！");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }
    }

    //审批通过/不通过
    @RequestMapping(value = "editApply")
    public String editApply(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }
        Apply apply = gson.fromJson(jsonRequest.getData(), Apply.class);
        Apply apply1 = applyRepository.findByApplyId(apply.getApplyId());
        apply1.setIsPass(apply.getIsPass());
        //如果审批通过，开放此工序
        if (apply.getIsPass() == 2) {
            FieldPlan fieldPlan = fieldPlanRepository.findByFieldPlanId(apply1.getFieldPlanId());
            fieldPlan.setIsClick(true);
            fieldPlanRepository.save(fieldPlan);
        }
        applyRepository.save(apply1);
        jsonResult.setResult(1);
        jsonResult.setMsg("审批成功！");
        return gson.toJson(jsonResult);
    }

    @RequestMapping(value = "getControlPointListByProject")
    public String getControlPointListByProject(@RequestBody String msg) {
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
        ProjectControlPoint projectControlPoint = gson.fromJson(jsonRequest.getData(), ProjectControlPoint.class);
        List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.findByProjectId(projectControlPoint.getProjectId());
        jsonResult.setData(gson.toJson(projectControlPointList));
        jsonResult.setResult(1);
        jsonResult.setMsg("获取项目控制点列表");
        return gson.toJson(jsonResult);
    }

    @RequestMapping(value = "openProcedure")
    public String openProcedure(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != 3
                || userLevel != 3) {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
        } else {
            FieldPlan fieldPlan = gson.fromJson(jsonRequest.getData(), FieldPlan.class);
            FieldPlan fieldPlan1 = fieldPlanRepository.findByFieldPlanId(fieldPlan.getFieldPlanId());
            fieldPlan1.setIsClick(true);
            jsonResult.setResult(1);
            fieldPlanRepository.save(fieldPlan1);
            jsonResult.setMsg("开放工序成功");
        }
        return gson.toJson(jsonResult);
    }

}
