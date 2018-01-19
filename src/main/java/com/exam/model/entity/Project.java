package com.exam.model.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mac on 2017/3/8.
 */
@Entity

public class Project {
    @Id
    @GeneratedValue
    private Integer projectId;
    private String projectNumber;//项目编号
    private String projectName;
    private String projectResource;//项目来源
    private String hostCompany; //业主单位

    private String projectPollutionType;//污染物类型
    private String projectPollutionTarget;//目标污染物

    private String projectPlanName;//修复技术
    private String projectLocation;//项目地点
    private Double projectArea;//项目面积
    private String projectStartTime;//项目开始时间
    private String projectEndTime;//项目结束时间

    private String projectService;//服务模式id
    private String projectExecuteCompany;//实施单位
    private String projectMonitorCompany;//监管单位

    private String baseInFoManagerId;//该项目的项目信息录入人员userId,权限为21，负责录入项目信息

    private String projectManagerId;//该项目的项目负责人userId,权限为22,负责人员任命

    private String projectSkillManagerId;//该项目的技术人员负责人userId，权限为23，负责该项目方案工序控制点的选择

    private String preSaleEngineerId;//该项目售前工程师userId，权限为24，负责该项目的前期调研

    private String projectNotes;//备注
    @Column(name = "isCompleted")
    private Boolean isCompleted = false;//项目是否完成
    @Column(name = "isCreateCompleted")
    private Boolean isCreateCompleted = false;//项目是否创建完成
    @Column(name = "isPersonCompleted")
    private Boolean isPersonCompleted = false;//人员分配是否完成
    @Column(name = "isProcedureCompleted")
    private Boolean isProcedureCompleted = false;//工艺路线选择是否完成
    @Column(name = "isResearchCompleted")
    private Boolean isResearchCompleted = false;//项目前期调研是否完成


    //额外需求
    @Transient
    private List<ProjectPlan> projectPlanList;
    @Transient
    private List<ProjectQualified> projectQualifiedList;
    @Transient
    private List<ProjectControlPoint> projectControlPointList;

    @Transient
    private List<ResearchData> researchDataList;

    @Transient
    private String baseInFoManagerName;
    @Transient
    private String managerName;
    @Transient
    private String skillManagerName;
    @Transient
    private String preSaleEngineerName;
    @Transient
    private List<ProjectGovernment> projectGovernmentList;

    @Transient
    private List<Field> fieldList;

    @Transient
    private int unCompletedNumber;

    @Transient
    private int completedNumber;

    //用于田块
    @Transient
    private int fieldSize;

    @Transient
    private double rate;


    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectResource() {
        return projectResource;
    }

    public void setProjectResource(String projectResource) {
        this.projectResource = projectResource;
    }

    public String getHostCompany() {
        return hostCompany;
    }

    public void setHostCompany(String hostCompany) {
        this.hostCompany = hostCompany;
    }

    public String getProjectPollutionType() {
        return projectPollutionType;
    }

    public void setProjectPollutionType(String projectPollutionType) {
        this.projectPollutionType = projectPollutionType;
    }

    public String getProjectPollutionTarget() {
        return projectPollutionTarget;
    }

    public void setProjectPollutionTarget(String projectPollutionTarget) {
        this.projectPollutionTarget = projectPollutionTarget;
    }

    public String getProjectPlanName() {
        return projectPlanName;
    }

    public void setProjectPlanName(String projectPlanName) {
        this.projectPlanName = projectPlanName;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public Double getProjectArea() {
        return projectArea;
    }

    public void setProjectArea(Double projectArea) {
        this.projectArea = projectArea;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public String getProjectService() {
        return projectService;
    }

    public void setProjectService(String projectService) {
        this.projectService = projectService;
    }

    public String getProjectExecuteCompany() {
        return projectExecuteCompany;
    }

    public void setProjectExecuteCompany(String projectExecuteCompany) {
        this.projectExecuteCompany = projectExecuteCompany;
    }

    public String getProjectMonitorCompany() {
        return projectMonitorCompany;
    }

    public void setProjectMonitorCompany(String projectMonitorCompany) {
        this.projectMonitorCompany = projectMonitorCompany;
    }

    public String getBaseInFoManagerId() {
        return baseInFoManagerId;
    }

    public void setBaseInFoManagerId(String baseInFoManagerId) {
        this.baseInFoManagerId = baseInFoManagerId;
    }

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public String getProjectSkillManagerId() {
        return projectSkillManagerId;
    }

    public void setProjectSkillManagerId(String projectSkillManagerId) {
        this.projectSkillManagerId = projectSkillManagerId;
    }

    public String getPreSaleEngineerId() {
        return preSaleEngineerId;
    }

    public void setPreSaleEngineerId(String preSaleEngineerId) {
        this.preSaleEngineerId = preSaleEngineerId;
    }

    public String getProjectNotes() {
        return projectNotes;
    }

    public void setProjectNotes(String projectNotes) {
        this.projectNotes = projectNotes;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getCreateCompleted() {
        return isCreateCompleted;
    }

    public void setCreateCompleted(Boolean createCompleted) {
        isCreateCompleted = createCompleted;
    }

    public Boolean getPersonCompleted() {
        return isPersonCompleted;
    }

    public void setPersonCompleted(Boolean personCompleted) {
        isPersonCompleted = personCompleted;
    }

    public Boolean getProcedureCompleted() {
        return isProcedureCompleted;
    }

    public void setProcedureCompleted(Boolean procedureCompleted) {
        isProcedureCompleted = procedureCompleted;
    }

    public Boolean getResearchCompleted() {
        return isResearchCompleted;
    }

    public void setResearchCompleted(Boolean researchCompleted) {
        isResearchCompleted = researchCompleted;
    }

    public List<ProjectPlan> getProjectPlanList() {
        return projectPlanList;
    }

    public void setProjectPlanList(List<ProjectPlan> projectPlanList) {
        this.projectPlanList = projectPlanList;
    }

    public List<ProjectQualified> getProjectQualifiedList() {
        return projectQualifiedList;
    }

    public void setProjectQualifiedList(List<ProjectQualified> projectQualifiedList) {
        this.projectQualifiedList = projectQualifiedList;
    }

    public String getBaseInFoManagerName() {
        return baseInFoManagerName;
    }

    public void setBaseInFoManagerName(String baseInFoManagerName) {
        this.baseInFoManagerName = baseInFoManagerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSkillManagerName() {
        return skillManagerName;
    }

    public void setSkillManagerName(String skillManagerName) {
        this.skillManagerName = skillManagerName;
    }

    public String getPreSaleEngineerName() {
        return preSaleEngineerName;
    }

    public void setPreSaleEngineerName(String preSaleEngineerName) {
        this.preSaleEngineerName = preSaleEngineerName;
    }

    public List<ProjectControlPoint> getProjectControlPointList() {
        return projectControlPointList;
    }

    public void setProjectControlPointList(List<ProjectControlPoint> projectControlPointList) {
        this.projectControlPointList = projectControlPointList;
    }

    public List<ProjectGovernment> getProjectGovernmentList() {
        return projectGovernmentList;
    }

    public void setProjectGovernmentList(List<ProjectGovernment> projectGovernmentList) {
        this.projectGovernmentList = projectGovernmentList;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<ResearchData> getResearchDataList() {
        return researchDataList;
    }

    public void setResearchDataList(List<ResearchData> researchDataList) {
        this.researchDataList = researchDataList;
    }


    public int getUnCompletedNumber() {
        return unCompletedNumber;
    }

    public void setUnCompletedNumber(int unCompletedNumber) {
        this.unCompletedNumber = unCompletedNumber;
    }

    public int getCompletedNumber() {
        return completedNumber;
    }

    public void setCompletedNumber(int completedNumber) {
        this.completedNumber = completedNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!projectId.equals(project.projectId)) return false;
        return projectNumber.equals(project.projectNumber);
    }

    @Override
    public int hashCode() {
        int result = projectId.hashCode();
        result = 31 * result + projectNumber.hashCode();
        return result;
    }
}