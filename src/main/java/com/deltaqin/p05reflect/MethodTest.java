package com.deltaqin.p05reflect;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author deltaqin
 * @date 2021/11/27 7:41 下午
 */
public class MethodTest {
    @Test
    public void extracted() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ArrayList<String> array  = new ArrayList<String>();
        array.add("a");
        //反射方式,获取出集合ArrayList类的class文件对象
        Class c = array.getClass();
        //获取ArrayList.class文件中的方法add

        Method method = c.getMethod("add",Object.class);

        //使用invoke运行ArrayList方法add
        method.invoke(array, 150);
        method.invoke(array, 1500);
        method.invoke(array, 15000);
        System.out.println(array);
    }
}
