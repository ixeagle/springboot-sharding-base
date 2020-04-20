package org.yaukie.api.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaukie.api.dto.BaseData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yuenbin
 * @Date :2020/3/2
 * @Time :19:13
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:
 **/
public class DatabaseUtil {
    private   static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/encrypt?useUnicode=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String SQL = "SELECT * FROM ";

    private  static  ThreadLocal<Connection> hold  = new ThreadLocal<Connection>();

    static {
        PropertyConfigurator.configure(Thread.currentThread()
                .getContextClassLoader().getResource("log4j.properties"));
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            if(conn == null ){
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                hold.set(conn);
            }else{
                conn = hold.get();
            }

         } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取表中字段名称
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }
    /**
     * 获取表中字段的注释
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<>();
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }


    public static List<BaseData> getBaseDataList(String tableName){
        List<String> columnNames = getColumnNames(tableName);
        List<String> columnTypes = getColumnTypes(tableName);
        List<String> columnComments = getColumnComments(tableName);
        List<BaseData> list = new ArrayList<BaseData>();
        for (int i = 0 ; i<columnNames.size(); i++){
            BaseData baseData = new BaseData();
            baseData.setColumnName(firstUpperCamelCase(columnNames.get(i)));
            String columnType = null;
            switch (columnTypes.get(i)){
                case "VARCHAR":
                    columnType = "String";
                    break;
                case "DATETIME":
                    columnType = "Date";
                    break;
                case "INT":
                    columnType = "Integer";
                    break;
                case "TINYINT":
                    columnType = "Integer";
                    break;
                case "DECIMAL":
                    columnType = "BigDecimal";
                    break;
                case "TEXT":
                    columnType = "String";
                    break;
                case "TIMESTAMP":
                    columnType = "timestamp";
                    break;
                default:
                    columnType = "未知类型";
                System.out.println("存在不支持类型！请手写。");
                    break;

            }
            baseData.setColumnType(columnType);
            baseData.setColumnComment(columnComments.get(i));
            list.add(baseData);
        }
        return list;
    }




    /**
     *  将字段名称整理成驼峰形式
     * @param str
     * @return
     */
    public static String firstUpperCamelCase(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = str.replace("T_", "");
            str = str.toLowerCase();
            String[] strs = str.split("_");
            if (strs.length == 1) {
                return str.substring(0, 1).toLowerCase()+ str.substring(1);
            } else {
                String convertedStr = "";
                for (int i = 0; i < strs.length; i++) {
                    convertedStr += firstLetterUpper(strs[i]);
                }
                return convertedStr.substring(0, 1).toLowerCase()+ convertedStr.substring(1);
            }
        }
        return str;
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String firstLetterUpper(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = str.replace("T_", "");
            str = str.toLowerCase();
            return str.substring(0, 1).toUpperCase()
                    + str.substring(1, str.length());
        }
        return str;
    }

    public static void main(String[] args) {

         List<BaseData> table = getBaseDataList("t_user");
         for(BaseData baseData:table)
         {
             LOGGER.info(baseData.getColumnName());
         }

    }

}
