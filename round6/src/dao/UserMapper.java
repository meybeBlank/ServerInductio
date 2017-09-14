package dao;

import test.DBHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserMapper {

//    private UserMapper() {
//    }

    private static class Handler {
        private static UserMapper newInstance = new UserMapper();
    }

    public static UserMapper getInstance() {
        return Handler.newInstance;
    }

    /**
     * 根据公司和分组查询user
     *
     * @author fengzhen
     * @version v1.0, 2017/9/4 14:05
     */
    public List<Map<String, Object>> getUserMapList(String companyId, String groupId, String dbName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT USER_NAME,ARCHIVE_ID FROM FUN_USERS WHERE COMP_ID = ? AND GR_ID = ?";
        return DBHelper.getInstance().query(dbName, sql, new Object[]{companyId, groupId});
    }
}
