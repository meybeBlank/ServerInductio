package cglib;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的拦截器
 *
 * @author fengzhen
 * @version v1.0, 2017/9/13 15:49
 */
public class DefaultInterceptor implements Interceptor {

    private String Name;

    public DefaultInterceptor(String name) {
        Name = name;
    }

    @Override
    public Object befor(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        System.out.println("befor : " + Name);
//        if ("2".equals(Name)) {
//            int i = 1 / 0;
//        }
        return null;
    }

    @Override
    public Object after(Object result) {
        System.out.println("after : " + Name);
        List<Map<String, Object>> names1 = (List<Map<String, Object>>) result;
        HashMap<String, Object> map = new HashMap<>();
        map.put(Name,"第一次处理");
        names1.add(map);
        System.out.println("after : " + names1.toString());
        return names1;
    }

    @Override
    public Object throwErr(Throwable throwable) {
        System.out.println("err : " + Name);
        return null;
    }
}
