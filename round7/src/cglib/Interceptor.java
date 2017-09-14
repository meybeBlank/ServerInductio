package cglib;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 拦截器
 *
 * @author fengzhen
 * @version v1.0, 2017/9/13 11:24
 */
public interface Interceptor {
    /**
     * 方法之前,对参数进行预处理
     *
     * @author fengzhen
     * @version v1.0, 2017/9/13 11:25
     */
    Object befor(Object o, Method method, Object[] objects, MethodProxy methodProxy);

    /**
     * 方法执行之后,对结果进行操作
     *
     * @author fengzhen
     * @version v1.0, 2017/9/13 11:25
     */
    Object after(Object result);

    /**
     * 方法执行之后,出现异常
     *
     * @author fengzhen
     * @version v1.0, 2017/9/13 11:25
     */
    Object throwErr(Throwable throwable);
}
