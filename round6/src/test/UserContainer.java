package test;

import annotation.InjectAnno;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserInfo注入容器
 *
 * @author fengzhen
 * @version v1.0, 2017/9/12 15:10
 */
public class UserContainer {

    public static UserInfo injectUser() {

        // 装配的元素
        Map map = parseXml();

        // 装配的目标
        UserInfo userInfo = new UserInfo();

        return inject(map, userInfo);
    }

    private static UserInfo inject(Map map, UserInfo userInfo) {
        Class<? extends UserInfo> aClass = userInfo.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field :
                declaredFields) {
            InjectAnno anno = field.getDeclaredAnnotation(InjectAnno.class);
            if (anno != null){
                field.setAccessible(true);
                try {
                    field.set(userInfo,map.get(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return userInfo;
    }

    private static Map parseXml(){
        // 读取配置内容
        Map<String, String> map = new HashMap<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document userMapper = null;
        try {
            userMapper = saxBuilder.build(new File("src/test/UserInfoConfig.xml"));
            Element rootElement = userMapper.getRootElement();
            List<Element> children = rootElement.getChildren();
            for (Element element :
                    children) {
                map.put(element.getName(), element.getAttributeValue("value"));
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
