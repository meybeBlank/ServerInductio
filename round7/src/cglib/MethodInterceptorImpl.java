package cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * cglib方法拦截实现
 *
 * @author fengzhen
 * @version v1.0, 2017/9/12 18:39
 */
public class MethodInterceptorImpl implements MethodInterceptor {

    // 队列标识
    private int i = 0;
    // 拦截器队列
    private List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        Object invoke = null;
        // 预处理
        for (Interceptor inter :
                interceptors) {
            inter.befor(o, method, objects, methodProxy);
            i++;
        }

        try {
            invoke = methodProxy.invokeSuper(o, objects);
            System.out.println(invoke.toString());

            // 返回结果处理
            for (int j = i - 1; j > -1; j--) {
                invoke = interceptors.get(j).after(invoke);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return invoke;
    }
}
