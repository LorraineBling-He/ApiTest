package com.automation.Util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.automation.constants.Constants;
import org.apache.poi.ss.usermodel.*;
import com.automation.pojo.API;
import com.automation.pojo.Case;
import com.automation.pojo.WriteData;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.automation.constants.Constants.*;

public class ExcelUtils {


    public static List<API> apiList = ExcelUtils.read(Constants.EXCEL_PATH, 0,API.class);
    public static List<Case> caseList=ExcelUtils.read(Constants.EXCEL_PATH, 1, Case.class);
    public static List<WriteData> writeDataList =new ArrayList<WriteData>();  //响应

    /**
     * 通过apiId获取相应的接口用例，一个接口的所有用例
     * @param apiId
     * @return
     */
    public static Object[][] getCaseByAPIid(String apiId){
        //获取api
        API wantApi=new API();
        for (API api: apiList){
            if (api.getId().equals(apiId)){
                wantApi=api;
                break;
            }
        }
        //获取case数组
        ArrayList<Case> wantCase=new ArrayList<Case>();
        for(Case cases: caseList){
            if(cases.getApiId().equals(apiId)){
                wantCase.add(cases);
            }
        }
        //返回object[][]
        Object[][] objects = new Object[wantCase.size()][2];
        for(int i=0;i<wantCase.size();i++) {
            objects[i][0] = wantApi;
            objects[i][1] = wantCase.get(i);
        }
        return objects;
    }
    /**
     * 1、导入easypoi坐标，删除poi坐标
     * 2、编写pojo类（API）
     * 3、在API类的字段上使用@Excel注解
     * 4、编写3行导入代码
     * @param address
     */
    public static <E> List<E> read(String address,int StartSheetIndex,Class<E> clazz) {
        FileInputStream fis=null;
        try {
            //1、加载Excel文件
            fis = new FileInputStream(address);
            //2、导入配置，创建空对象相当于用默认配置
            ImportParams params = new ImportParams();
            //针对情况可以去修改默认配置 如：
            params.setStartSheetIndex(StartSheetIndex);
            //导入时需要验证数据，结合实体类上的注解一起使用  如：NotNull
            params.setNeedVerify(false);
            //3、执行导入
            List<E> list = ExcelImportUtil.importExcel(fis, clazz, params);
            return list;
           /* Class clazz3=Class.forName("API");
            Object obj=clazz3.newInstance();
            Method method1=clazz3.getMethod("getName");//若没有参数，不传即可
            Method method2=clazz3.getMethod("setName",String.class);   //有参情况
            Object result=method2.invoke(obj,"登录");  //有参情况，虽然是一个set函数，但是也可以有返回值，null
            Object name=method1.invoke(obj);   //若没有参数，不传即可
            System.out.println(name);
            //（反射的底层）调用属性，由于属性是私有的，所以要用暴力反射
            Field[] fields=clazz3.getFields();//获取所有的属性，普通获取
            Field[] fieldss=clazz3.getDeclaredFields();//暴力反射，获取私有属性A
            for(Field field:fieldss){
                field.setAccessible(true);//拥有获取权限B，与上面的A是配套使用
                System.out.println(field.getName()+"==="+field.getType());//获取单个属性和他的类型
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(fis);
        }
        return null;
    }

    public static void batchWrite(List<WriteData> arrayList) {
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try{
            fis=new FileInputStream(EXCEL_PATH);
            Workbook workbook=WorkbookFactory.create(fis);
            Sheet sheet=workbook.getSheet("接口用例");
            //遍历
            for (WriteData wd:arrayList) {
                //获取行号，获取row对象
                int rowNum=wd.getRowNum();
                Row row=sheet.getRow(rowNum);
                //获取列号，获取cell对象
                int cellNum=wd.getCellNum();
                Cell cell=row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //回写
                cell.setCellType(CellType.STRING);
                String content=wd.getContent();
                cell.setCellValue(content);
            }
            //回写到文件中
            fos=new FileOutputStream(EXCEL_PATH);
            workbook.write(fos);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            close(fis);
            close(fos);
        }
    }
    //入参是FileInputStream fis，由于这个方法还要用在FileOutputStream，所以为满足两者要用到多态，即入参改为他们的父类（这两个没有相同的父类）或者父接口Closeable,正好这个接口中有这个close方法
    //注意：输入输出流没有相同的父类，但继承了相同的接口closeable，还有很多其他的类也继承了这个接口
    private static void close(Closeable stream) {
        if(stream !=null){
            try{
                stream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void addWriteData(int row,int cell,String content){
        WriteData wd=new WriteData(row,cell,content);
        writeDataList.add(wd);
    }


    /**
     * 获取所有的case，代码逻辑不对，不能用hashmap    废弃
     * @param apiList
     * @return
     */
    public static Object[][] getAllcase(List<API> apiList){
        HashMap<API,Case> allCase=new HashMap<API,Case>();
        Object[][] objects=new Object[allCase.size()][2];
        for (API api:apiList) {
            if(api.getId()==null){
                break;
            }
            for (Case cases: caseList) {
                if(api.getId().equals(cases.getApiId())){
                    allCase.put(api,cases);
                }
            }
        }
            for(int i=0;i<allCase.size();i++){
                objects[i][0]=allCase.keySet();
                objects[i][1]=allCase.values();
    }
        return objects;
    }

    /**
     * 废弃
     * @param address
     * @param sheetName
     * @return
     */
    public static Object[][] readold(String address,String sheetName) {
        //1、创建数据流
        FileInputStream fis=null;
        Object[][] datas=null;
        try {
            fis = new FileInputStream(address);//会报异常
            //2、workbook相当于整个Excel
            Workbook workbook = WorkbookFactory.create(fis);//会报异常
            //3、获取sheet
            Sheet sheet = workbook.getSheet(sheetName);
            datas=new Object[sheet.getLastRowNum()][5];
            //4、
            for(int i=1;i<=sheet.getLastRowNum();i++){
                Row row=sheet.getRow(i);
                Cell numCell=row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                numCell.setCellType(CellType.STRING);
                datas[i-1][0]=numCell.getStringCellValue();

                Cell urlCell=row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                urlCell.setCellType(CellType.STRING);
                datas[i-1][1]=urlCell.getStringCellValue();

                Cell paraCell=row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                paraCell.setCellType(CellType.STRING);
                datas[i-1][2]=paraCell.getStringCellValue();

                Cell typeCell=row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                typeCell.setCellType(CellType.STRING);
                datas[i-1][3]=typeCell.getStringCellValue();

                Cell contentTypeCell=row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                contentTypeCell.setCellType(CellType.STRING);
                datas[i-1][4]=contentTypeCell.getStringCellValue();

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return datas;
    }
}
