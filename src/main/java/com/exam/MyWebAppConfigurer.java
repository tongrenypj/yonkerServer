package com.exam;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * Created by dell-ewtu on 2017/3/14.
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    //public static final String baseUrl = "http://172.31.208.35:8081/";
    public static final String baseUrl = "http://115.159.88.237:8081/";


    //web端图片
    public static final String WebPhotosPath = "C:/WebPhotos/";
    //培训资料
    public static final String TrainingFilesPath = "C:/TrainingFiles/";
    //控制点采集图片路径
    public static final String CollectPhotosPath = "C:/CollectPhotos/";
    //行业现状、行业内容图片路径
    public static final String IndustryStatusPhotosPath = "C:/IndustryStatusPhotos/";
    //修复案例、修复内容图片路径
    public static final String RepairSituationPhotosPath = "C:/RepairSituationPhotos/";
    //永清首页图片路径
    public static final String IntroducePhotosPath = "C:/IntroducePhotos/";
    //工序标准文件路径
    public static final String ProcedureStandardFilePath = "C:/ProcedureStandardFile/";
    //田块经纬度图片
    public static final String FieldPositionPath = "C:/FieldPosition/";
    //前期调研图片路径
    public static final String ResearchPhotoFilePath = "C:/ResearchPhoto/";
    //apk路径
    public static final String ApkPath = "C:/ApkPath/";


    private void addFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("文件夹已存在！");
        } else {
            file.mkdir();
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        addFile("C:/WebPhotos");
        addFile("C:/TrainingFiles");
        addFile("C:/CollectPhotos");
        addFile("C:/IndustryStatusPhotos");
        addFile("C:/RepairSituationPhotos");
        addFile("C:/IntroducePhotos");
        addFile("C:/ProcedureStandardFile");
        addFile("C:/FieldPosition");
        addFile("C:/ResearchPhoto");
        addFile("C:/ApkPath");

        //控制点采集图片mac 路径
        //registry.addResourceHandler("/CollectPhoto/**").addResourceLocations("file:/Users/mac/Desktop/photo/");
        //registry.addResourceHandler("/Pics/**").addResourceLocations("file:C:/Pics/");

        //web端图片
        registry.addResourceHandler("/WebPhotos/**").addResourceLocations("file:" + WebPhotosPath);
        //培训资料
        registry.addResourceHandler("/TrainingFiles/**").addResourceLocations("file:" + TrainingFilesPath);
        //控制点采集图片路径
        registry.addResourceHandler("/CollectPhotos/**").addResourceLocations("file:" + CollectPhotosPath);
        //行业现状、行业内容图片路径
        registry.addResourceHandler("/IndustryStatusPhotos/**").addResourceLocations("file:" + IndustryStatusPhotosPath);
        //修复案例、修复内容图片路径
        registry.addResourceHandler("/RepairSituationPhotos/**").addResourceLocations("file:" + RepairSituationPhotosPath);
        //永清首页图片路径
        registry.addResourceHandler("/IntroducePhotos/**").addResourceLocations("file:" + IntroducePhotosPath);
        //工序标准文件路径
        registry.addResourceHandler("/ProcedureStandardFile/**").addResourceLocations("file:" + ProcedureStandardFilePath);
        //田块经纬度图片
        registry.addResourceHandler("/FieldPosition/**").addResourceLocations("file:" + FieldPositionPath);
        //前期调研图片路径
        registry.addResourceHandler("/ResearchPhoto/**").addResourceLocations("file:" + ResearchPhotoFilePath);
        //apk文件夹
        registry.addResourceHandler("/ApkPath/**").addResourceLocations("file:" + ApkPath);


        super.addResourceHandlers(registry);


    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }
}
