package bo;

import annotation.InjectAnno;
import dao.UserMapper;
import test.DBHelper;

import java.util.List;
import java.util.Map;

/**
 * 业务处理类，模拟注入dao操作
 *
 * @author fengzhen
 * @version v1.0, 2017/9/12 16:21
 */
public class BusinessController {

    @InjectAnno
    UserMapper userMapper;

    /**
     * 进行数据操作
     *
     * @author fengzhen
     * @version v1.0, 2017/9/12 16:38
     */
    public void todoBusiness() {
        try {
            List<Map<String, Object>> names1 = userMapper.getUserMapList("43628","252936", DBHelper.DB_USER_NAME);
            System.out.println(names1.get(0).get("USER_NAME"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
