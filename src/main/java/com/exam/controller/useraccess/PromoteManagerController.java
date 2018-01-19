package com.exam.controller.useraccess;

import com.exam.MyWebAppConfigurer;
import com.exam.Repository.*;
import com.exam.model.bean.JsonRequest;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dell-ewtu on 2017/5/6.
 */
@RestController
@RequestMapping("/api/promoteManager")
public class PromoteManagerController {

    @Autowired
    private IntroduceRepository introduceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private industryStatusRepository industryStatusRepository;
    @Autowired
    private IndustryContentRepository industryContentRepository;
    @Autowired
    private RepairSituationRepository repairSituationRepository;
    @Autowired
    private RepairContentRepository repairContentRepository;


    //1新增/编辑永清简介，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditIntroduce")
    public String addOrEditIntroduce(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(params.get("UserBean"), User.class);
        Introduce introduce = gson.fromJson(params.get("Introduce"), Introduce.class);


        JsonResult jsonResult = new JsonResult();
        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");


        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (validateLevelByUser(requestUser)) {
            //如果是编辑，删除原来的文件
            if (introduce.getSectionId() != null) {
                deleteFile(MyWebAppConfigurer.IntroducePhotosPath + introduceRepository.findBySectionId(introduce.getSectionId()).getImagePath());
            }
            if (!file.isEmpty()) {
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.IntroducePhotosPath + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonResult.setResult(0);
                    jsonResult.setMsg("上传永清简介图片失败," + e.getMessage());
                    return gson.toJson(jsonResult);
                }

            } else {

                fileName = "NoImage";
            }

            introduce.setImagePath(fileName);
            introduceRepository.save(introduce);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传永清简介图片成功");
            jsonResult.setData(fileName);
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }

    //2删除永清简介，传删除的introduce
    @RequestMapping(value = "/deleteIntroduce")
    public String deleteIntroduce(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        JsonResult jsonResult = new JsonResult();
        Introduce introduce = gson.fromJson(jsonRequest.getData(), Introduce.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            deleteFile(MyWebAppConfigurer.IntroducePhotosPath + introduce.getImagePath());
            introduceRepository.delete(introduce);
            jsonResult.setResult(1);
            jsonResult.setMsg("删除成功！");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);
    }

    //1新增/编辑行业现状，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditIndustryStatus")
    public String addOrEditIndustryStatus(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(params.get("UserBean"), User.class);
        IndustryStatus industryStatus = gson.fromJson(params.get("IndustryStatus"), IndustryStatus.class);
        JsonResult jsonResult = new JsonResult();
        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");

        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        if (validateLevelByUser(requestUser)) {
    /*      //如果是编辑，删除原来的标题和内容
            if (industryStatus.getProfessionId() != null) {
                deleteFile(MyWebAppConfigurer.IndustryStatusPhotosPath +
                        industryStatusRepository.findByProfessionId(industryStatus.getProfessionId()).getThumbImage());
                Sort s = new Sort(Sort.Direction.DESC, "contentId");
                List<IndustryContent> industryContentList = industryContentRepository.findByProfessionId(industryStatus.getProfessionId(), s);
                for (IndustryContent industryContent : industryContentList) {
                    deleteFile(MyWebAppConfigurer.IndustryStatusPhotosPath + industryContent.getImage());
                    industryContentRepository.delete(industryContent);
                }
                industryStatusRepository.delete(industryStatus);
            }*/

            if (!file.isEmpty()) {
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.IndustryStatusPhotosPath
                            + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonResult.setResult(0);
                    jsonResult.setMsg("上传行业现状标题图片失败," + e.getMessage());
                    return gson.toJson(jsonResult);
                }

            } else {
                fileName = "NoImage";
            }

            industryStatus.setThumbImage(fileName);
            industryStatusRepository.save(industryStatus);
            IndustryStatus industryStatus1 = new IndustryStatus();
            industryStatus1.setProfessionId(industryStatusRepository.findByThumbImage(fileName).getProfessionId());
            jsonResult.setResult(1);
            jsonResult.setMsg("上传行业现状标题图片成功");
            jsonResult.setData(gson.toJson(industryStatus1));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }

    //2删除行业现状，传删除的industryStatus
    @RequestMapping(value = "/deleteIndustryStatus")
    public String deleteIndustryStatus(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        JsonResult jsonResult = new JsonResult();
        IndustryStatus industryStatus = gson.fromJson(jsonRequest.getData(), IndustryStatus.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            //删除标题下的所有内容
            Sort s = new Sort(Sort.Direction.DESC, "contentId");
            List<IndustryContent> industryContentList = industryContentRepository.findByProfessionId(industryStatus.getProfessionId(), s);
            for (IndustryContent industryContent : industryContentList) {
                deleteFile(MyWebAppConfigurer.IndustryStatusPhotosPath + industryContent.getImage());
                industryContentRepository.delete(industryContent);
            }
            deleteFile(MyWebAppConfigurer.IndustryStatusPhotosPath + industryStatus.getThumbImage());
            industryStatusRepository.delete(industryStatus);
            jsonResult.setResult(1);
            jsonResult.setMsg("删除行业现状成功！");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);
    }

    //3新增/编辑行业现状内容，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditIndustryContent")
    public String addOrEditIndustryStatusContent(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(params.get("UserBean"), User.class);
        IndustryContent industryContent = gson.fromJson(params.get("IndustryContent"), IndustryContent.class);
        JsonResult jsonResult = new JsonResult();

        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_sss");
        //String currentTime= simpleDateFormat.format(new Date());
//        String currentTime = String.valueOf(System.currentTimeMillis());
        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");

        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (validateLevelByUser(requestUser)) {
            if (!file.isEmpty()) {
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.IndustryStatusPhotosPath
                            + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonResult.setResult(0);
                    jsonResult.setMsg("上传行业现状内容图片失败," + e.getMessage());
                    return gson.toJson(jsonResult);
                }

            } else {
                fileName = "NoImage";
            }
            industryContent.setImage(fileName);
            industryContentRepository.save(industryContent);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传行业现状内容图片成功");
            jsonResult.setData(fileName);
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }

    /**
     * zgs修改    当没有图片的时候
     *
     * @param string
     * @return
     */
    //3新增/编辑行业现状内容，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditIndustryContentNoFile")
    public String addOrEditIndustryStatusContentNoFile(@RequestBody String string) {
        Gson gson = new Gson();
        JsonRequest request = gson.fromJson(string, JsonRequest.class);

        IndustryContent industryContent = gson.fromJson(request.getData(), IndustryContent.class);
        JsonResult jsonResult = new JsonResult();
        User user = new User();
        user.setUserLevel(Integer.valueOf(request.getUserLevel()));
        user.setUserId(request.getUserId());

        if (validateLevelByUser(user)) {


            industryContent = industryContentRepository.save(industryContent);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传行业现状内容图片成功");
            jsonResult.setData(gson.toJson(industryContent));
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }

    /**
     * zgs修改    当没有图片的时候
     *
     * @param string
     * @return
     */
    //3新增/编辑修复案例内容
    @RequestMapping(value = "/addOrEditRepairContentNoFile")
    public String addOrEditRepairContentNoFile(@RequestBody String string) {
        Gson gson = new Gson();
        JsonRequest request = gson.fromJson(string, JsonRequest.class);

        RepairContent repairContent = gson.fromJson(request.getData(), RepairContent.class);
        JsonResult jsonResult = new JsonResult();
        User user = new User();
        user.setUserLevel(Integer.valueOf(request.getUserLevel()));
        user.setUserId(request.getUserId());


        if (validateLevelByUser(user)) {

            repairContent.setImage("NoI" +
                    "mage");
            repairContent = repairContentRepository.save(repairContent);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传修复案例内容图片成功");
            jsonResult.setData(gson.toJson(repairContent));
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }


    //1新增/编辑修复案例，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditRepairSituation")
    public String addOrEditRepairSituation(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(params.get("UserBean"), User.class);
        RepairSituation repairSituation = gson.fromJson(params.get("RepairSituation"), RepairSituation.class);
        JsonResult jsonResult = new JsonResult();
        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");


        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (validateLevelByUser(requestUser)) {
            //如果是编辑，删除原来的文件
            if (repairSituation.getSituationId() != null) {
                deleteFile(MyWebAppConfigurer.RepairSituationPhotosPath +
                        repairSituationRepository.findBySituationId(repairSituation.getSituationId()).getThumbImage());
                List<RepairContent> repairContentList = repairContentRepository.findBySituationId(repairSituation.getSituationId());
                for (RepairContent repairContent : repairContentList) {
                    deleteFile(MyWebAppConfigurer.RepairSituationPhotosPath + repairContent.getImage());
                    repairContentRepository.delete(repairContent);
                }
                repairSituationRepository.delete(repairSituation);
            }
            if (!file.isEmpty()) {
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.RepairSituationPhotosPath
                            + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonResult.setResult(0);
                    jsonResult.setMsg("上传修复案例标题图片失败," + e.getMessage());
                    return gson.toJson(jsonResult);

                }
            } else {
                fileName = "NoImage";
            }
            repairSituation.setThumbImage(fileName);
            repairSituationRepository.save(repairSituation);
            RepairSituation repairSituation1 = new RepairSituation();
            repairSituation1.setSituationId(repairSituationRepository.findByThumbImage(fileName).getSituationId());
            jsonResult.setResult(1);
            jsonResult.setMsg("上传修复案例标题图片成功");
            jsonResult.setData(gson.toJson(repairSituation1));
            return gson.toJson(jsonResult);

        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }

    //2删除修复案例，传删除的repairSituation
    @RequestMapping(value = "/deleteRepairSituation")
    public String deleteRepairSituation(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(msg, JsonRequest.class);
        JsonResult jsonResult = new JsonResult();
        RepairSituation repairSituation = gson.fromJson(jsonRequest.getData(), RepairSituation.class);
        if (validateLevelJsonRequest(jsonRequest)) {
            deleteFile(MyWebAppConfigurer.RepairSituationPhotosPath + repairSituation.getThumbImage());
            //删除修复案例标题下所有内容
            List<RepairContent> repairContentList = repairContentRepository.findBySituationId(repairSituation.getSituationId());
            for (RepairContent repairContent : repairContentList) {
                deleteFile(MyWebAppConfigurer.RepairSituationPhotosPath + repairContent.getImage());
                repairContentRepository.delete(repairContent);
            }
            repairSituationRepository.delete(repairSituation);
            jsonResult.setResult(1);
            jsonResult.setMsg("删除修复案例成功！");
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("您没有操作权限！");
        }
        return gson.toJson(jsonResult);
    }


    //3新增/编辑修复案例内容，传multipartFile文件及参数
    @RequestMapping(value = "/addOrEditRepairContent")
    public String addOrEditRepairContent(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(params.get("UserBean"), User.class);
        RepairContent repairContent = gson.fromJson(params.get("RepairContent"), RepairContent.class);
        JsonResult jsonResult = new JsonResult();
        //String currentTime = String.valueOf(System.currentTimeMillis());

        String currentTime = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = currentTime + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        if (validateLevelByUser(requestUser)) {
            if (!file.isEmpty()) {

                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(MyWebAppConfigurer.RepairSituationPhotosPath
                            + fileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonResult.setResult(0);
                    jsonResult.setMsg("上传修复案例内容图片失败," + e.getMessage());
                    return gson.toJson(jsonResult);
                }

            } else {
                fileName = "NoImage";
            }

            repairContent.setImage(fileName);
            repairContentRepository.save(repairContent);
            jsonResult.setResult(1);
            jsonResult.setMsg("上传修复案例内容图片成功");
            jsonResult.setData(fileName);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("你没有该操作的权限");
            return gson.toJson(jsonResult);
        }

    }


    private boolean validateLevelByUser(User user) {
        String uuid = user.getUserId();
        int userLevel = user.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLevelJsonRequest(JsonRequest jsonRequest) {
        String uuid = jsonRequest.getUserId();
        int userLevel = jsonRequest.getUserLevel();
        if (userRepository.findByUserId(uuid) == null || userRepository.findByUserId(uuid).getUserLevel() != userLevel) {
            return false;
        } else {
            return true;
        }
    }

    private void deleteFile(String path) {
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            deleteFile.delete();
        } else {
            System.out.print("文件不存在！");
        }

    }

}

