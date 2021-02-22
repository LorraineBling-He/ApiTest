package com.automation.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class Case {
    @Excel(name="用例编号")
    private String id;
    @Excel(name="用例描述")
    private String descript;
    @Excel(name="参数")
    private String param;
    @Excel(name="URL参数")
    private String urlParam;
    @Excel(name="接口编号")
    private String apiId;
    @Excel(name="期望返回值")
    private String expectValue;
    @Excel(name="前置sql语句")
    private String beforeSql;
    @Excel(name="后置sql语句")
    private String afterSql;
/*    @Override
    public String toString() {
        return "Case{" +
                "id='" + id + '\'' +
                ", descript='" + descript + '\'' +
                ", param='" + param + '\'' +
                ", urlParam='" + urlParam + '\'' +
                ", apiId='" + apiId + '\'' +
                '}';
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    public String getBeforeSql() {
        return beforeSql;
    }

    public void setBeforeSql(String beforeSql) {
        this.beforeSql = beforeSql;
    }


    public String getAfterSql() {
        return afterSql;
    }

    public void setAfterSql(String afterSql) {
        this.afterSql = afterSql;
    }
}
