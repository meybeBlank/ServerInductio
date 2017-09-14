import annotation.DBField;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * 数据库帮助类
 *
 * @author fengzhen
 * @version v1.0, 2017/9/14 10:52
 */
public class DBHelper {

    static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String SQLSERVER_URL = "jdbc:sqlserver://192.168.11.106:33434;databaseName=";
    public static final String DB_USER_NAME = "hft_erpdb_CD";
    public static final String DB_USER_NAME2 = "hft_erpdb_ZZ";
    static final String USER = "user_fafa";
    static final String PASSWORD = "user_fafa123456";

    public static final String SQL = "SELECT USER_NAME,ARCHIVE_ID FROM FUN_USERS WHERE COMP_ID = 43628 AND GR_ID = 252936";
    public static final String SQL_PREPARED = "SELECT USER_NAME,ARCHIVE_ID FROM FUN_USERS WHERE COMP_ID = ? AND GR_ID = ?";

    private Map<String, Connection> connectionMap;

    private DBHelper() {
        try {
            Class.forName(SQLSERVER_DRIVER);
            connectionMap = new HashMap<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class InstanceHolder {
        private static DBHelper instance = new DBHelper();
    }

    public static DBHelper getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 根据表名建立数据库连接
     *
     * @author fengzhen
     * @version v1.0, 2017/9/4 10:40
     */
    private Connection getConDb(String dbName) throws SQLException, ClassNotFoundException {
        Connection conn = connectionMap.get(dbName);
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(SQLSERVER_URL + dbName, USER, PASSWORD);
            connectionMap.put(dbName, conn);
        }
        return conn;
    }


    /**
     * 关闭所有Connection
     *
     * @author fengzhen
     * @version v1.0, 2017/9/6 10:18
     */
    public void closeAllConn() throws SQLException {
        Collection<Connection> values = connectionMap.values();
        for (Connection conn :
                values) {
            conn.close();
            connectionMap.remove(conn);
        }
    }

    /**
     * 查询公用方法
     *
     * @param sql     查询sql
     * @param objects sql中的参数
     * @author fengzhen
     * @version v1.0, 2017/9/4 14:05
     */
    public List<Map<String, Object>> query(String dbName, String sql, Object[] objects)
            throws SQLException, ClassNotFoundException {

        Connection conn = getConDb(dbName);

        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 1, j = objects.length; i <= j; i++) {
                pstmt.setObject(i, objects[i - 1]);
            }
            resultSet = pstmt.executeQuery();
            return resultToMap(resultSet);
        } finally {
            close(conn, resultSet, pstmt);
        }
    }

    /**
     * 查询公用方法
     *
     * @param sql     查询sql
     * @param objects sql中的参数
     * @author fengzhen
     * @version v1.0, 2017/9/4 14:05
     */
    public <T> List<T> query(String dbName, String sql, Object[] objects, Class<T> clazz)
            throws SQLException, ClassNotFoundException {

        Connection conn = getConDb(dbName);

        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<T> ts = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 1, j = objects.length; i <= j; i++) {
                pstmt.setObject(i, objects[i - 1]);
            }
            resultSet = pstmt.executeQuery();
            ts = resultToBean(resultSet, clazz);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            close(conn, resultSet, pstmt);
        }
        return ts;
    }

    /**
     * 查询结果转换为List
     *
     * @author fengzhen
     * @version v1.0, 2017/9/4 15:10
     */
    private List<Map<String, Object>> resultToMap(ResultSet resultSet)
            throws SQLException {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            HashMap<String, Object> map = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(metaData.getColumnName(i), resultSet.getString(i));
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 查询结果转换为bean
     *
     * @author fengzhen
     * @version v1.0, 2017/9/4 15:10
     */
    public <T> List<T> resultToBean(ResultSet resultSet, Class<T> clazz)
            throws SQLException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        Field[] fields = clazz.getDeclaredFields();

        List<Map<String, Integer>> maps = getMapper(fields, metaData);

        while (resultSet.next()) {
            T t = clazz.newInstance();
            for (Map<String, Integer> map :
                    maps) {
                Integer field = map.get("field");
                Integer column = map.get("column");
                Field field1 = fields[field];
                field1.setAccessible(true);
                field1.set(t,resultSet.getString(column));
            }
            result.add(t);
        }
        return result;
    }

    /**
     * 遍历整个bean，获取映射关系
     *
     * @author fengzhen
     * @version v1.0, 2017/9/14 11:50
     */
    private List<Map<String, Integer>> getMapper(Field[] fields,ResultSetMetaData metaData) throws SQLException {
        // 遍历所有属性，获取注解是否有对应的列名一致，进行缓存位置
        List<Map<String, Integer>> maps = new ArrayList<>();
        int columnCount = metaData.getColumnCount();
        for (int k = 0, j = fields.length; k < j; k++) {
            DBField annotation = fields[k].getAnnotation(DBField.class);
            if (annotation != null) {
                String[] value = annotation.value();
                for (String va :
                        value) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (va.equals(metaData.getColumnName(i))) {
                            HashMap map = new HashMap();
                            map.put("field", k);
                            map.put("column", i);
                            maps.add(map);
                        }
                    }
                }
            }
        }
        return maps;
    }

    /**
     * 释放链接
     *
     * @author fengzhen
     * @version v1.0, 2017/9/4 14:59
     */
    private void close(Connection conn, ResultSet rs, PreparedStatement ps)
            throws SQLException {
        if (ps != null) {
            ps.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (conn != null) {
            conn.close();
            connectionMap.remove(conn);
        }
    }

}
