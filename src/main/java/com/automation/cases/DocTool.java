package com.automation.cases;

import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.pojo.API;
import com.automation.pojo.Case;

import static com.automation.Util.ExcelUtils.*;

public class DocTool extends BaseCase {
    //这里虽然没有beforesuite，aftersuite，但由于继承了BaseCase，注解也一并继承了，二注解不同于方法，子类要想用父类的方法，需要手动调用，但注解是自动的，所以在执行@test之前会自动执行父类中的beforesuite，aftersuite

    @Test(dataProvider = "tablesDataExchangeData",description = "")
    @Description("")
    public void tablesDataExchangeData(API api, Case cases)  {
        excute(api, cases,"tablesDataExchangeData");
    }
    @DataProvider(name = "tablesDataExchangeData")
    public Object[][] tablesDataExchangeData(){
         Object[][] objects=getCaseByAPIid("1");
        return objects;
    }


    @Test(dataProvider = "getdatasourceDataSourceData")
    public void getdatasourceDataSourceData(API api, Case cases) {
        excute(api, cases,"getdatasourceDataSourceData");
    }
    @DataProvider(name = "getdatasourceDataSourceData")
    public Object[][] getdatasourceDataSourceData(){
        Object[][] objects=getCaseByAPIid("2");
        return objects;
    }

    @Test(dataProvider = "postdatasourceDataSource")
    public void postdatasourceDataSource(API api, Case cases) {
        excute(api, cases,"postdatasourceDataSource");
    }
    @DataProvider(name = "postdatasourceDataSource")
    public Object[][] postdatasourceDataSource(){
        Object[][] objects=getCaseByAPIid("3");
        return objects;
    }

    @Test(dataProvider = "dictionaryDicCtrl")
    public void dictionaryDicCtrl(API api, Case cases) {
        excute(api, cases,"dictionaryDicCtrl");
    }
    @DataProvider(name = "dictionaryDicCtrl")
    public Object[][] dictionaryDicCtrl(){
        Object[][] objects=getCaseByAPIid("4");
        return objects;
    }

    @Test(dataProvider = "AccessNumberDocCtrl")
    public void AccessNumberDocCtrl(API api, Case cases) {
        excute(api, cases,"AccessNumberDocCtrl");
    }
    @DataProvider(name = "AccessNumberDocCtrl")
    public Object[][] AccessNumberDocCtrl(){
        Object[][] objects=getCaseByAPIid("5");
        return objects;
    }

    @Test(dataProvider = "postsubSubProject")
    public void postsubSubProject(API api, Case cases) {
        excute(api, cases,"postsubSubProject");
    }
    @DataProvider(name = "postsubSubProject")
    public Object[][] postsubSubProject(){
        Object[][] objects=getCaseByAPIid("6");
        return objects;
    }
}
