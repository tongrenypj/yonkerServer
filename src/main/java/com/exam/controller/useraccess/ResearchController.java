package com.exam.controller.useraccess;

import com.exam.MyWebAppConfigurer;
import com.exam.Repository.*;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mac on 2017/7/10.
 */


//前期调研
@RestController
@RequestMapping("/api/research")
public class ResearchController {

    @Autowired
    private ResearchDataRepository researchDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResearchPhotoRepository researchPhotoRepository;
    @Autowired
    private ProjectResearchRepository projectResearchRepository;
    @Autowired
    private ProjectPreResearchRepository projectPreResearchRepository;

    //第一,前期调研获取项目任务列表
    @RequestMapping(value = "/inputProject/24/findProjectPreResearch")
    public String findProjectPreResearch(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        Project project = gson.fromJson(msg, Project.class);

        List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectId(project.getProjectId());
        List<ProjectPreResearch> projectPreResearchList1 = new ArrayList<>();

        List<String> strings = new ArrayList<>();

        for (ProjectPreResearch projectPreResearch : projectPreResearchList) {

            if (!strings.contains(projectPreResearch.getResearchStepName())) {
                ProjectPreResearch projectPreResearch0 = new ProjectPreResearch();
                strings.add(projectPreResearch.getResearchStepName());
                projectPreResearch0.setPreResearchId(projectPreResearch.getPreResearchId());
                projectPreResearch0.setResearchStepName(projectPreResearch.getResearchStepName());
                projectPreResearch0.setProjectId(projectPreResearch.getProjectId());
                projectPreResearchList1.add(projectPreResearch0);
            }

        }


        if (projectPreResearchList1 != null && projectPreResearchList1.size() != 0) {

            jsonResult.setResult(1);
            jsonResult.setData(gson.toJson(projectPreResearchList1));
            jsonResult.setMsg("返回该项目的前期调研任务列表");
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(1);
            jsonResult.setMsg("无获取前期调研任务列表");
            jsonResult.setData("");
            return gson.toJson(jsonResult);
        }
    }


    //第二、根据任务的不同进入不同的界面。提醒安卓端还要修改
    @RequestMapping(value = "/inputProject/24/getProjectPreResearch")
    public String getProjectPreResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }


        ProjectPreResearch projectPreResearch = gson.fromJson(msg, ProjectPreResearch.class);

        if (projectPreResearch.getProjectId() == null && projectPreResearch.getPreResearchId() == null) {

            jsonResult.setResult(0);
            jsonResult.setMsg("请传入正确参数");
            return gson.toJson(jsonResult);
        }


        List<ProjectResearch> projectResearchList = projectResearchRepository.findByProjectIdAndPreResearchId
                (projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());

        if (projectResearchList != null && projectResearchList.size() != 0) {
            jsonResult.setResult(1);
            jsonResult.setData("1");
            jsonResult.setMsg("该任务该用户之前已经勾选过采集字段，进入样品列表界面");
        } else {

            List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectIdAndPreResearchId
                    (projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());
            if (projectPreResearchList.get(0).getResearchName() != null) {


                jsonResult.setResult(1);
                jsonResult.setData("3");
                jsonResult.setMsg("该任务该用户没有勾选过采集字段，进入字段勾选界面");
            } else {

                jsonResult.setResult(1);
                jsonResult.setData("2");
                jsonResult.setMsg("进入该任务的输入文本界面");

            }

        }
        return gson.toJson(jsonResult);
    }


    //第三、根据任务的不同进入不同的界面。
    @RequestMapping(value = "/inputProject/24/getSelectProjectPreResearch")
    public String getSelectProjectPreResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        ProjectPreResearch projectPreResearch = gson.fromJson(msg, ProjectPreResearch.class);

        if (projectPreResearch.getProjectId() == null && projectPreResearch.getPreResearchId() == null) {

            jsonResult.setResult(0);
            jsonResult.setMsg("请传入正确参数");
            return gson.toJson(jsonResult);
        }

        List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectIdAndPreResearchId
                (projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());
        if (projectPreResearchList.get(0).getResearchName() != null) {


            jsonResult.setResult(1);
            jsonResult.setData(gson.toJson(projectPreResearchList));
            jsonResult.setMsg("返回该任务勾选采集字段列表，供用户勾选");
            return gson.toJson(jsonResult);
        } else {

            jsonResult.setResult(0);
            jsonResult.setMsg("输入任务不对");
            return gson.toJson(jsonResult);

        }
    }


    //第四，存储或修改字段数值
    @RequestMapping(value = "/inputProject/24/setResearchData")

    public String setResearchData(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        Type listType = new TypeToken<List<ResearchData>>() {
        }.getType();
        //获取前期调研数据列表
        List<ResearchData> researchDataList = gson.fromJson(msg, listType);
        Integer researchDataId = researchDataList.get(0).getResearchDataId();

        //判断是新增还是修改
        if (researchDataId != null) {
            Integer projectId = researchDataList.get(0).getProjectId();
            Integer preResearchId = researchDataList.get(0).getPreResearchId();
            String sampleNumber = researchDataList.get(0).getSampleNumber();

            //判断数据是样品数据还是文本数据
            if (sampleNumber != null) {
                ResearchData researchData = researchDataRepository.findByResearchDataId(researchDataId);
                //判断是否修改了样品编号
                if (researchData.getSampleNumber().equals(sampleNumber)) {

                    researchDataRepository.save(researchDataList);
                    jsonResult.setResult(1);
                    jsonResult.setMsg("修改该样品前期调研采集数据成功");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);


                } else {

                    //如果修改了样品编号，需要判断样品编号是否已经被使用
                    List<ResearchData> researchDatas = researchDataRepository.
                            findByProjectIdAndPreResearchIdAndSampleNumber(projectId, preResearchId, sampleNumber);

                    if (researchDatas.size() == 0 || researchDatas == null) {

                        researchDataRepository.save(researchDataList);
                        jsonResult.setResult(1);
                        jsonResult.setMsg("修改前期调研样品采集数据成功");
                        jsonResult.setData("");
                        return gson.toJson(jsonResult);
                    } else {

                        jsonResult.setResult(0);
                        jsonResult.setMsg("样品编号已存在!");
                        jsonResult.setData("");
                        return gson.toJson(jsonResult);
                    }
                }
            } else {
                researchDataRepository.save(researchDataList);
                jsonResult.setResult(1);
                jsonResult.setMsg("修改前期调文本采集数据成功");
                jsonResult.setData("");
                return gson.toJson(jsonResult);
            }

        } else {
            Integer projectId = researchDataList.get(0).getProjectId();
            Integer preResearchId = researchDataList.get(0).getPreResearchId();
            String sampleNumber = researchDataList.get(0).getSampleNumber();

            //判断数据是样品数据还是文本数据
            if (sampleNumber != null) {

                //需要判断样品编号是否已经被使用

                List<ResearchData> researchDatas = researchDataRepository.
                        findByProjectIdAndPreResearchIdAndSampleNumber(projectId, preResearchId, sampleNumber);

                if (researchDatas.size() == 0 || researchDatas == null) {

                    researchDataRepository.save(researchDataList);
                    jsonResult.setResult(1);
                    jsonResult.setMsg("保存前期调研样品采集数据成功");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);

                } else {

                    jsonResult.setResult(0);
                    jsonResult.setMsg("样品编号已存在!");
                    jsonResult.setData("");
                    return gson.toJson(jsonResult);
                }

            } else {
                researchDataRepository.save(researchDataList);
                jsonResult.setResult(1);
                jsonResult.setMsg("保存前期调文本采集数据成功");
                jsonResult.setData("");
                return gson.toJson(jsonResult);
            }

        }
    }


    //第五
    @RequestMapping(value = "/inputProject/24/getResearchDetailedData")
    public String getResearchDetailedData(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        //(1projectId;2PreResearchId;3SampleNumber)
        ResearchData researchData = gson.fromJson(msg, ResearchData.class);

        if (researchData.getProjectId() != null && researchData.getPreResearchId() != null && researchData.getSampleNumber() != null) {

            List<ResearchData> researchDataList = researchDataRepository.findByProjectIdAndPreResearchIdAndSampleNumber(
                    researchData.getProjectId(), researchData.getPreResearchId(), researchData.getSampleNumber());

            jsonResult.setResult(1);
            jsonResult.setMsg("返回该样品的详细数据");
            jsonResult.setData(gson.toJson(researchDataList));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("参数有误，请输入项目id，调研任务Id，及样品编号");
            return gson.toJson(jsonResult);

        }
    }

    //第六
    @RequestMapping(value = "/inputProject/24/getResearchData")
    public String getResearchData(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        //1、projectId;2、preResearchId（污染数据收集与现场污染数据采样、监测researchStepName)3ResearchName

        ProjectPreResearch projectPreResearch = gson.fromJson(msg, ProjectPreResearch.class);

        //判断该控制点是否需要按字段收集
        List<ProjectPreResearch> projectPreResearchList = projectPreResearchRepository.findByProjectIdAndPreResearchId
                (projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId());
        if (projectPreResearchList.size() != 0 && projectPreResearchList.get(0).getResearchName() != null) {


            if (projectPreResearch.getProjectId() != null && projectPreResearch.getPreResearchId() != null) {

                List<ResearchData> researchDataList = researchDataRepository.findByProjectIdAndPreResearchIdAndResearchName(
                        projectPreResearch.getProjectId(), projectPreResearch.getPreResearchId(), "样品编号");

                jsonResult.setResult(1);
                jsonResult.setMsg("返回样品编号列表");
                jsonResult.setData(gson.toJson(researchDataList));
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
                jsonResult.setData(gson.toJson(researchData1));
                return gson.toJson(jsonResult);
            } else {

                jsonResult.setResult(0);
                jsonResult.setMsg("参数有误，请输入项目id，调研任务id");
                return gson.toJson(jsonResult);
            }
        }
    }


    //第七
    @RequestMapping(value = "/inputProject/24/uploadResearchPhoto")
    public String uploadResearchPhoto(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        String msg = params.get("JsonRequest");

        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();

        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        ResearchPhoto researchPhoto = gson.fromJson(msg, ResearchPhoto.class);


        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        ResearchPhoto researchPhoto1 = new ResearchPhoto();
        researchPhoto1.setProjectId(researchPhoto.getProjectId());
        researchPhoto1.setResearchPhotoPath(fileName);


        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.ResearchPhotoFilePath + fileName)));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                jsonResult.setResult(0);
                jsonResult.setData("上传失败," + e.getMessage());
                jsonResult.setMsg("上传成功失败文件未空");
                return gson.toJson(jsonResult);
            }
            researchPhotoRepository.save(researchPhoto1);
            jsonResult.setResult(1);
            jsonResult.setData(fileName);
            jsonResult.setMsg("上传成功");
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setData(fileName);
            jsonResult.setMsg("上传成功失败文件未空");
            return gson.toJson(jsonResult);
        }
    }

    //第八
    @RequestMapping(value = "/inputProject/24/getResearchPhoto")
    public String getResearchPhoto1(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        ResearchPhoto researchPhoto = gson.fromJson(msg, ResearchPhoto.class);


        if (researchPhoto.getProjectId() != 0) {
            List<ResearchPhoto> researchPhotoList = researchPhotoRepository.findByProjectId(researchPhoto.getProjectId());


            String str = MyWebAppConfigurer.ResearchPhotoFilePath;
            String str1 = str.substring(3, str.length());
            for (ResearchPhoto researchPhoto1 : researchPhotoList) {

                researchPhoto1.setResearchPhotoPath(MyWebAppConfigurer.baseUrl + str1 + researchPhoto1.getResearchPhotoPath());
            }

            jsonResult.setResult(1);
            jsonResult.setData(gson.toJson(researchPhotoList));
            jsonResult.setMsg("返回该项目前期调研图片列表");
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("请传入ProjectId");
            return gson.toJson(jsonResult);
        }
    }

    //第九
    @RequestMapping(value = "/inputProject/24/getProjectResearch")
    public String getProjectResearch(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        ProjectResearch projectResearch = gson.fromJson(msg, ProjectResearch.class);
        if (projectResearch.getProjectId() != null && projectResearch.getPreResearchId() != null) {
            List<ProjectResearch> projectResearchList = projectResearchRepository.findByProjectIdAndPreResearchId
                    (projectResearch.getProjectId(), projectResearch.getPreResearchId());
            if (projectResearchList.size() == 0) {
                jsonResult.setResult(0);
                jsonResult.setMsg("该任务没有存入用户勾选的采集字段");
                return gson.toJson(jsonResult);
            } else {

                jsonResult.setResult(1);
                jsonResult.setMsg("获取该任务用户勾选的的采集字段");
                jsonResult.setData(gson.toJson(projectResearchList));
                return gson.toJson(jsonResult);
            }

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("请传入正确参数");
            return gson.toJson(jsonResult);

        }
    }


    //第十
    @RequestMapping(value = "/inputProject/24/setProjectResearch")
    public String setProjectResearch(@RequestBody String msg) {

        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);

        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        msg = jsonRequest.getData();


        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {

            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);

        }

        Type listType = new TypeToken<List<ProjectResearch>>() {
        }.getType();
        List<ProjectResearch> projectResearchList = gson.fromJson(msg, listType);


        projectResearchRepository.save(projectResearchList);

        jsonResult.setResult(1);
        jsonResult.setMsg("保存该任务采集字段");
        jsonResult.setData("");
        return gson.toJson(jsonResult);


    }

}
