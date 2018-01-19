package com.exam.model.bean;

import com.exam.model.entity.*;

import java.util.List;

/**
 * Created by mac on 2017/2/27.
 */
public class JsonResult {

    private int result;
    private String msg;
    private String data;

    private User user;
    private List<User> userList;


    private Plan plan;

    private PlanProcedure planProcedure;

    private ResearchData researchData;

    private List<Plan> planList;


    private Project project;
    private List<Project> projectList;

    private List<ProjectPreResearch> projectPreResearchList;

    private Field field;

    private List<FieldExecute> fieldExecuteList;

    private List<Field> fieldList;

    private List<Collect> collectList;

    private List<Research> researchList;

    private List<ResearchPhoto> researchPhotoList;

    private List<ResearchData> researchDataList;

    private List<PreResearch> preResearchList;

    private List<ControlPoint> controlPointList;

    private List<DisplayImage> displayImageList;

    private List<Material> materialList;

    private List<PlanProcedure> planProcedureList;

    private List<ProjectControlPoint> projectControlPointList;

    private List<ProjectQualified> projectQualifiedList;

    private List<ProjectPlan> projectPlanList;

    private List<CollectData> collectDataList;

    private List<CollectPhoto> collectPhotoList;

    private List<FieldPlan> fieldPlanList;


    public JsonResult() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }


    public PlanProcedure getPlanProcedure() {
        return planProcedure;
    }

    public void setPlanProcedure(PlanProcedure planProcedure) {
        this.planProcedure = planProcedure;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Collect> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<Collect> collectList) {
        this.collectList = collectList;
    }

    public List<ControlPoint> getControlPointList() {
        return controlPointList;
    }

    public void setControlPointList(List<ControlPoint> controlPointList) {
        this.controlPointList = controlPointList;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }

    public List<DisplayImage> getDisplayImageList() {
        return displayImageList;
    }

    public void setDisplayImageList(List<DisplayImage> displayImageList) {
        this.displayImageList = displayImageList;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public List<PlanProcedure> getPlanProcedureList() {
        return planProcedureList;
    }

    public void setPlanProcedureList(List<PlanProcedure> planProcedureList) {
        this.planProcedureList = planProcedureList;
    }

    public List<ProjectQualified> getProjectQualifiedList() {
        return projectQualifiedList;
    }

    public void setProjectQualifiedList(List<ProjectQualified> projectQualifiedList) {
        this.projectQualifiedList = projectQualifiedList;
    }

    public List<ProjectPlan> getProjectPlanList() {
        return projectPlanList;
    }

    public void setProjectPlanList(List<ProjectPlan> projectPlanList) {
        this.projectPlanList = projectPlanList;
    }

    public List<CollectData> getCollectDataList() {
        return collectDataList;
    }

    public void setCollectDataList(List<CollectData> collectDataList) {
        this.collectDataList = collectDataList;
    }

    public List<CollectPhoto> getCollectPhotoList() {
        return collectPhotoList;
    }

    public void setCollectPhotoList(List<CollectPhoto> collectPhotoList) {
        this.collectPhotoList = collectPhotoList;
    }

    public List<FieldPlan> getFieldPlanList() {
        return fieldPlanList;
    }

    public void setFieldPlanList(List<FieldPlan> fieldPlanList) {
        this.fieldPlanList = fieldPlanList;
    }

    public List<Research> getResearchList() {
        return researchList;
    }

    public void setResearchList(List<Research> researchList) {
        this.researchList = researchList;
    }

    public List<PreResearch> getPreResearchList() {
        return preResearchList;
    }

    public void setPreResearchList(List<PreResearch> preResearchList) {
        this.preResearchList = preResearchList;
    }

    public List<ResearchPhoto> getResearchPhotoList() {
        return researchPhotoList;
    }

    public void setResearchPhotoList(List<ResearchPhoto> researchPhotoList) {
        this.researchPhotoList = researchPhotoList;
    }

    public List<ResearchData> getResearchDataList() {
        return researchDataList;
    }

    public void setResearchDataList(List<ResearchData> researchDataList) {
        this.researchDataList = researchDataList;
    }

    public ResearchData getResearchData() {
        return researchData;
    }

    public void setResearchData(ResearchData researchData) {
        this.researchData = researchData;
    }

    public List<ProjectPreResearch> getProjectPreResearchList() {
        return projectPreResearchList;
    }

    public void setProjectPreResearchList(List<ProjectPreResearch> projectPreResearchList) {
        this.projectPreResearchList = projectPreResearchList;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<FieldExecute> getFieldExecuteList() {
        return fieldExecuteList;
    }

    public void setFieldExecuteList(List<FieldExecute> fieldExecuteList) {
        this.fieldExecuteList = fieldExecuteList;
    }

    public List<ProjectControlPoint> getProjectControlPointList() {
        return projectControlPointList;
    }

    public void setProjectControlPointList(List<ProjectControlPoint> projectControlPointList) {
        this.projectControlPointList = projectControlPointList;
    }
}
