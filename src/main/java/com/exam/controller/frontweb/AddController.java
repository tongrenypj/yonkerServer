package com.exam.controller.frontweb;

import com.exam.Repository.*;
import com.exam.model.bean.JsonResult;
import com.exam.model.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dell-ewtu on 2017/3/8.
 */
@RestController
@RequestMapping("/api/add")
public class AddController {
    @Autowired
    private ControlPointRepository controlPointRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanProcedureRepository planProcedureRepository;
    @Autowired
    private FieldPlanRepository fieldPlanRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private ProjectControlPointRepository projectControlPointRepository;
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private PreResearchRepository preResearchRepository;


    //第一，新增某个控制点的采集数据的名称和单位

    /**
     * @param msg 控制点id:controlPointId
     * @return
     */
    @RequestMapping(value = "/addCollect")
    public String addCollect(@RequestBody String msg) {
        Gson gson = new Gson();
        Collect newCollectMsg = gson.fromJson(msg, Collect.class);
        JsonResult jsonResult = new JsonResult();

        List<Collect> collectList = collectRepository.findByControlPointId(newCollectMsg.getControlPointId());
        for (Collect collect : collectList) {
            if (newCollectMsg.getCollectionName().equals(collect.getCollectionName())) {
                jsonResult.setResult(0);
                jsonResult.setMsg("该采集控制点的采集数据的名称已存在！请换名");
                return gson.toJson(jsonResult);
            }
        }
        collectRepository.save(newCollectMsg);
        jsonResult.setResult(1);
        jsonResult.setMsg("新增成功");
        return gson.toJson(jsonResult);
    }
    //新增前期调研采集数据的名称


    @RequestMapping(value = "/addAndEditPreResearch")
    public String addPreResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        PreResearch preResearch = gson.fromJson(msg, PreResearch.class);
        JsonResult jsonResult = new JsonResult();

        if (preResearch.getPreResearchId() == null) {
            if (preResearch.getResearchStepName() != null) {
                PreResearch preResearch1 = preResearchRepository.findByResearchStepName(preResearch.getResearchStepName());
                if (preResearch1 == null) {
                    preResearchRepository.save(preResearch);
                    jsonResult.setResult(1);
                    jsonResult.setMsg("新增成功");
                    return gson.toJson(jsonResult);

                } else {
                    jsonResult.setResult(0);
                    jsonResult.setMsg("新增失败，该名称已被使用");
                    return gson.toJson(jsonResult);
                }

            }


        } else {
            if (preResearch.getResearchStepName() != null) {
                PreResearch preResearch1 = preResearchRepository.findByResearchStepName(preResearch.getResearchStepName());
                if (preResearch1 == null) {
                    preResearchRepository.save(preResearch);

                    List<Research> researchList = researchRepository.findByPreResearchId(preResearch.getPreResearchId());
                    for (Research research : researchList) {

                        research.setResearchStepName(preResearch.getResearchStepName());
                        researchRepository.save(research);


                    }
                    jsonResult.setResult(1);
                    jsonResult.setMsg("编辑成功");
                    return gson.toJson(jsonResult);

                } else {
                    jsonResult.setResult(0);
                    jsonResult.setMsg("编辑失败，该名称已被使用");
                    return gson.toJson(jsonResult);
                }
            }

        }


        jsonResult.setResult(0);
        jsonResult.setMsg("请传入参数");
        return gson.toJson(jsonResult);


    }


    @RequestMapping(value = "/addAndEditResearch")
    public String addResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        Research research = gson.fromJson(msg, Research.class);
        JsonResult jsonResult = new JsonResult();

        //(researchId，preResearchId，（污染数据收集与现场污染数据采样、监测、）researchStepName，researchName)

        if (research.getResearchId() == null) {
            researchRepository.save(research);
            jsonResult.setResult(1);
            jsonResult.setMsg("新增成功");
            return gson.toJson(jsonResult);
        } else {
            researchRepository.save(research);
            jsonResult.setResult(1);
            jsonResult.setMsg("编辑成功");
            return gson.toJson(jsonResult);
        }
    }

    //第二，新增方案

    /**
     * @param msg 方案名：planName
     * @return
     */

    @RequestMapping(value = "/addPlan")
    public String addPlan(@RequestBody String msg) {
        Gson gson = new Gson();
        Plan plan = gson.fromJson(msg, Plan.class);
        JsonResult jsonResult = new JsonResult();
        if (planRepository.findByPlanName(plan.getPlanName()) == null) {
            planRepository.save(plan);
            jsonResult.setResult(1);
            jsonResult.setMsg("新建方案成功");
            jsonResult.setPlan(plan);
            return gson.toJson(jsonResult);
        } else {
            jsonResult.setResult(0);
            jsonResult.setMsg("方案名已存在");
            return gson.toJson(jsonResult);
        }
    }

    //已经测试
    //第三，新增工序

    /**
     * @param msg 必须传入 planId , isSerial , procedureName 参数
     * @return
     */
    @RequestMapping(value = "/newPlanProcedure")
    public String addProcedure(@RequestBody String msg) {
        Gson gson = new Gson();
        PlanProcedure newProcedureMsg = gson.fromJson(msg, PlanProcedure.class);
        JsonResult jsonResult = new JsonResult();
        if (planProcedureRepository.findByProcedureName(newProcedureMsg.getProcedureName()) != null) {
            jsonResult.setMsg("工序名已存在，请重新输入");
            jsonResult.setResult(0);
            return gson.toJson(jsonResult);
        } else {
            newProcedureMsg.setUpload(false);
            planProcedureRepository.save(newProcedureMsg);
            jsonResult.setResult(1);
            jsonResult.setMsg("新增成功");
            return gson.toJson(jsonResult);
        }

    }

    //修改工序,修改field_plan,project_plan,controlpoint表中的procedureName字段
    @RequestMapping(value = "/editProcedure")
    public String editProcedure(@RequestBody String msg) {
        Gson gson = new Gson();
        PlanProcedure newProcedureMsg = gson.fromJson(msg, PlanProcedure.class);
        JsonResult jsonResult = new JsonResult();
        if (planProcedureRepository.findByProcedureName(newProcedureMsg.getProcedureName()) != null) {
            jsonResult.setMsg("工序名已存在，请重新编辑");
            jsonResult.setResult(0);
            return gson.toJson(jsonResult);
        } else {
            planProcedureRepository.save(newProcedureMsg);
            List<ProjectPlan> projectPlanList = projectPlanRepository.findByProcedureId(newProcedureMsg.getProcedureId());
            for (ProjectPlan projectPlan : projectPlanList) {
                projectPlan.setProcedureName(newProcedureMsg.getProcedureName());
                projectPlanRepository.save(projectPlan);
            }
            List<FieldPlan> fieldPlanList = fieldPlanRepository.findByProcedureId(newProcedureMsg.getProcedureId());
            for (FieldPlan fieldPlan : fieldPlanList) {
                fieldPlan.setProcedureName(newProcedureMsg.getProcedureName());
                fieldPlanRepository.save(fieldPlan);
            }
            List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(newProcedureMsg.getProcedureId());
            for (ControlPoint controlPoint : controlPointList) {
                controlPoint.setProcedureName(newProcedureMsg.getProcedureName());
                controlPointRepository.save(controlPoint);
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("新增成功");
            return gson.toJson(jsonResult);
        }

    }


    //第四，新增以及编辑控制点接口，新增控制点只需新增控制点名称

    /**
     * @param msg 传入procedureId与控制点名称，控制点名称可以重名
     * @return
     */

    @RequestMapping(value = "/addAndEditControlPoint")
    public String addControlPointMsg(@RequestBody String msg) {
        JsonResult jsonResult = new JsonResult();
        Gson gson = new Gson();
        ControlPoint newUserMsg = gson.fromJson(msg, ControlPoint.class);

        if (newUserMsg.getControlPointId() != null) {

            ControlPoint controlPoint = controlPointRepository.findByControlPointId(newUserMsg.getControlPointId());
            controlPoint.setControlPointName(newUserMsg.getControlPointName());
            controlPointRepository.save(controlPoint);
            //修改project_control_point表中control_point_name字段
            List<ProjectControlPoint> projectControlPointList = projectControlPointRepository.findByControlPointId(newUserMsg.getControlPointId());
            for (ProjectControlPoint projectControlPoint : projectControlPointList) {
                projectControlPoint.setControlPointName(newUserMsg.getControlPointName());
                projectControlPointRepository.save(projectControlPoint);
            }
            jsonResult.setResult(1);
            jsonResult.setMsg("编辑控制点成功");
            return gson.toJson(jsonResult);

        } else {
            controlPointRepository.save(newUserMsg);
            jsonResult.setResult(1);
            jsonResult.setMsg("新增控制点成功");
            return gson.toJson(jsonResult);
        }
    }
}
