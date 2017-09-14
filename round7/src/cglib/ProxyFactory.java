package cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * 代理创建工厂类
 *
 * @author fengzhen
 * @version v1.0, 2017/9/13 11:53
 */
public class ProxyFactory {

    /**
     * 创建代理类
     *
     * @author fengzhen
     * @version v1.0, 2017/9/13 11:53
     */
    public static <T> T getInstance(Class<T> target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        MethodInterceptorImpl callback = new MethodInterceptorImpl();
        callback.addInterceptor(new DefaultInterceptor("1"));
        callback.addInterceptor(new DefaultInterceptor("2"));
        callback.addInterceptor(new DefaultInterceptor("3"));
        enhancer.setCallback(callback);
        return (T) enhancer.create();
    }
}
