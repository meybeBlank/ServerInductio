import annotation.InjectAnno;
import bo.BusinessController;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) {
        try {
            BusinessController bo = new BusinessController();

            Class<? extends BusinessController> aClass = bo.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field :
                    fields) {
                InjectAnno anno = field.getDeclaredAnnotation(InjectAnno.class);
                if (anno != null) {
                    Class<?> type = field.getType();
                    Object o = type.newInstance();
                    field.setAccessible(true);
                    field.set(bo, o);
                }
            }
            // 通过容器注入的方式添加依赖对象
            bo.todoBusiness();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
