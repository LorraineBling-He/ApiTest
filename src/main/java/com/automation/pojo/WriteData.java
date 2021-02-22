package com.automation.pojo;

/**
 * excell回写封装对象
 */
public class WriteData {
    //回写行号
    private int rowNum;
    //回写列号
    private int cellNum;
    //回写内容
    private String content;


    public WriteData(int rowNum, int cellNum, String content) {
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.content = content;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
