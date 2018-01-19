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
 * Created by dell-ewtu on 2017/3/9.
 */
@RestController
@RequestMapping("/api/delete")

public class DeleteController {
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private ControlPointRepository controlPointRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanProcedureRepository procedureRepository;
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private PreResearchRepository preResearchRepository;


    //第一，删除收集的数据

    /**
     * @param msg
     * @return
     */
    @RequestMapping(value = "/deleteCollect")
    public String deleteCollect(@RequestBody String msg) {
        Gson gson = new Gson();
        Collect collect = gson.fromJson(msg, Collect.class);
        JsonResult jsonResult = new JsonResult();
        if (collect.getCollectionId() != null) {
            collectRepository.delete(collect);
        }

        jsonResult.setMsg("删除成功");
        jsonResult.setResult(1);
        return gson.toJson(jsonResult);
    }

    @RequestMapping(value = "/deleteResearch")
    public String deleteResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        Research research = gson.fromJson(msg, Research.class);
        JsonResult jsonResult = new JsonResult();
        if (research.getResearchId() != null) {
            researchRepository.delete(research);
        }
        jsonResult.setMsg("删除成功");
        jsonResult.setResult(1);
        return gson.toJson(jsonResult);
    }


    @RequestMapping(value = "/deletePreResearch")
    public String deletePreResearch(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        PreResearch preResearch = gson.fromJson(msg, PreResearch.class);

        if (preResearch.getPreResearchId() != null) {
            PreResearch preResearch1 = preResearchRepository.findByPreResearchId(preResearch.getPreResearchId());

            preResearchRepository.delete(preResearch1);

            List<Research> researchList = researchRepository.findByPreResearchId(preResearch.getPreResearchId());

            researchRepository.delete(researchList);

            jsonResult.setMsg("删除成功");
            jsonResult.setResult(1);
            return gson.toJson(jsonResult);
        } else {

            jsonResult.setMsg("参数有误");
            jsonResult.setResult(0);
            return gson.toJson(jsonResult);

        }

    }

    //第二，删除控制点

    /**
     * @param msg
     * @return
     */

    @RequestMapping(value = "/deleteControlPoint")
    public String deleteControlPoint(@RequestBody String msg) {
        Gson gson = new Gson();
        ControlPoint newControlPoint = gson.fromJson(msg, ControlPoint.class);

        Integer controlPointId = newControlPoint.getControlPointId();

        //删除控制点
        ControlPoint controlPoint = controlPointRepository.findByControlPointId(controlPointId);
        controlPointRepository.delete(controlPoint);

        //删除采集控制点单位列表
        List<Collect> collectList = collectRepository.findByControlPointId(controlPointId);
        collectRepository.delete(collectList);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg("删除成功");
        jsonResult.setResult(1);
        return gson.toJson(jsonResult);
    }


    //第三，删除方案

    /**
     * @param msg
     * @return
     */

    @RequestMapping(value = "/deletePlan")
    public String deletePlan(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        Plan newPlanMsg = gson.fromJson(msg, Plan.class);
        List<PlanProcedure> procedureList = procedureRepository.findByPlanId(newPlanMsg.getPlanId());

        for (PlanProcedure planProcedure : procedureList) {

            List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(planProcedure.getProcedureId());
            for (ControlPoint controlPoint : controlPointList) {

                List<Collect> collectList = collectRepository.findByControlPointId(controlPoint.getControlPointId());
                //删除采集控制点单位列表
                collectRepository.delete(collectList);

            }
            //删除该工序下的控制点列表
            controlPointRepository.delete(controlPointList);

            //删除工序
            procedureRepository.delete(planProcedure);

        }
        //删除工艺路线
        planRepository.delete(newPlanMsg.getPlanId());
        jsonResult.setResult(1);
        jsonResult.setMsg("删除成功");
        return gson.toJson(jsonResult);
    }

    //第四，删除某个方案的工序

    /**
     * @param msg
     * @return
     */

    @RequestMapping(value = "/deleteProcedure")
    public String deleteProcedure(@RequestBody String msg) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();

        PlanProcedure newPlanProcedure = gson.fromJson(msg, PlanProcedure.class);

        //删除工序
        procedureRepository.delete(newPlanProcedure.getProcedureId());
        List<ControlPoint> controlPointList = controlPointRepository.findByProcedureId(newPlanProcedure.getProcedureId());
        for (ControlPoint controlPoint : controlPointList) {

            List<Collect> collectList = collectRepository.findByControlPointId(controlPoint.getControlPointId());
            //删除采集控制点单位列表
            collectRepository.delete(collectList);

        }
        //删除该工序下的控制点列表
        controlPointRepository.delete(controlPointList);
        jsonResult.setMsg("删除成功");
        jsonResult.setResult(1);
        return gson.toJson(jsonResult);
    }

}
