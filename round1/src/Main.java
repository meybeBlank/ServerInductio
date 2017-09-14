import annotation.DBField;
import bean.UserInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String SQLSERVER_URL = "jdbc:sqlserver://192.168.11.106:33434;databaseName=";
    public static final String DB_USER_NAME = "hft_erpdb_CD";
    public static final String DB_USER_NAME2 = "hft_erpdb_ZZ";
    static final String USER = "user_fafa";
    static final String PASSWORD = "user_fafa123456";

    public static final String SQL = "SELECT USER_NAME,ARCHIVE_ID FROM FUN_USERS WHERE COMP_ID = 43628 AND GR_ID = 252936";

    public static final String SQL_PREPARED = "SELECT USER_NAME,ARCHIVE_ID FROM FUN_USERS WHERE COMP_ID = ? AND GR_ID = ?";

    public static void main(String[] args) {

//        connectionDB();
//
//        preparedSql();
//
//        List<bean.UserInfo> beans = getBeans();
//        for (bean.UserInfo user :
//                beans) {
//            System.out.println(user.toString());
//        }

        try {
//            List<Map<String, Object>> query = DBHelper.getInstance()
//                    .query(DBHelper.DB_USER_NAME, DBHelper.SQL_PREPARED, new String[]{"43628", "252936"});
//            // 验证结果
//            for (Map<String, Object> map :
//                    query) {
//                System.out.println("USER_NAME：" + map.get("USER_NAME"));
//                System.out.println("ARCHIVE_ID：" + map.get("ARCHIVE_ID"));
//            }

            List<UserInfo> query = DBHelper.getInstance()
                    .query(DBHelper.DB_USER_NAME, DBHelper.SQL_PREPARED, new String[]{"43628", "252936"}, UserInfo.class);
            for (UserInfo user :
                    query) {
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最基本的数据库jdbc连接查询操作
     *
     * @author fengzhen
     * @version v1.0, 2017/9/14 10:31
     */
    public static void connectionDB(){
        try {
            // 加载jdbc引擎
            Class.forName(SQLSERVER_DRIVER);
            // 获取到数据库连接
            Connection connection = DriverManager.getConnection(SQLSERVER_URL + DB_USER_NAME, USER, PASSWORD);
            // 创建报表对象
            Statement statement = connection.createStatement();
            // 执行sql
            ResultSet resultSet = statement.executeQuery(SQL);
            // 获取结果
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 查询结果
            ArrayList<Map<String, String>> resultList = new ArrayList<>();
            while (resultSet.next()) {
                HashMap<String, String> map = new HashMap<>();
                // 下标从1开始
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                resultList.add(map);
            }
            // 验证结果
            for (Map<String, String> map :
                    resultList) {
                System.out.println("USER_NAME：" + map.get("USER_NAME"));
                System.out.println("ARCHIVE_ID：" + map.get("ARCHIVE_ID"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * PreparedStatement:预编译sql
     * 查阅预编译sql的优点
     *
     * @author fengzhen
     * @version v1.0, 2017/9/14 10:31
     */
    public static void preparedSql(){
        try {
            Class.forName(SQLSERVER_DRIVER);
            Connection connection = DriverManager.getConnection(SQLSERVER_URL + DB_USER_NAME, USER, PASSWORD);
            // 创建预编译报表对象
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PREPARED);
            preparedStatement.setObject(1,"43628");
            preparedStatement.setObject(2,"252936");
            // 后续操作一致
            ResultSet resultSet = preparedStatement.executeQuery();
            // 获取结果
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 查询结果
            ArrayList<Map<String, String>> resultList = new ArrayList<>();
            while (resultSet.next()) {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                resultList.add(map);
            }
            // 验证结果
            for (Map<String, String> map :
                    resultList) {
                System.out.println("USER_NAME：" + map.get("USER_NAME"));
                System.out.println("ARCHIVE_ID：" + map.get("ARCHIVE_ID"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询结果以java bean的形式返回
     * 注意这里数据库字段名与属性名可能存在一对多的映射
     *
     * @author fengzhen
     * @version v1.0, 2017/9/14 10:44
     */
    public static List<UserInfo> getBeans(){
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        try {
            Class.forName(SQLSERVER_DRIVER);
            Connection connection = DriverManager.getConnection(SQLSERVER_URL + DB_USER_NAME, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PREPARED);
            preparedStatement.setObject(1,"43628");
            preparedStatement.setObject(2,"252936");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                UserInfo userInfo = new UserInfo();
                for (int i = 1; i <= columnCount; i++) {
                    // 使用名字匹配
                    switch (metaData.getColumnName(i)) {
                        case "USER_NAME":
                        case "user_name":
                            userInfo.setUserName(resultSet.getString(i));
                            break;
                        case "ARCHIVE_ID":
                        case "archive_id":
                            userInfo.setArchiveID(resultSet.getString(i));
                            break;
                    }
                }
                userInfos.add(userInfo);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return userInfos;
    }
}
