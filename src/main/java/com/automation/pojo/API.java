package com.automation.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.NotNull;

/**
 * pojo是存放实体类，即这个包中的类只有属性和构造函数和get set方法
 */
public class API {
    //接口编号
    @Excel(name="接口编号")
    @NotNull
    private String id;//mapping  映射关系    notnull如果false，一行的数据都不会读进来
    //接口描述
    @Excel(name="接口描述")
    private String apiDescription;
    //接口名称
    @Excel(name="接口名称")
    private String name;
    //接口提交方式
    @Excel(name="接口提交方式")
    private String type;
    //接口地址
    @Excel(name="接口地址")
    private String url;
    //参数类型
    @Excel(name="参数类型")
    private String contentType;

    @Override
    public String toString() {
        return "API{" +
                "id='" + id + '\'' +
                "apiDescription='" + apiDescription + '\'' +
                ", name='" + name + '\'' +
                ", method='" + type + '\'' +
                ", url='" + url + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }
}
