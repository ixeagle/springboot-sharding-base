package org.yaukie.api.dto;

/**
 * @Author: yuenbin
 * @Date :2020/3/2
 * @Time :19:23
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 用于存储数据库基础数据
 **/
public class BaseData {

    private String columnComment;

    private String columnType;

    private String columnName;

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
