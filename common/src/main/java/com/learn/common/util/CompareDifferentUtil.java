package com.xiaoniu.supplier.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author 张伟
 * @projectName supplier-common
 * @title CompareDifferentUtil
 * @package com.xiaoniu.supplier.common.utils
 * @description 对比不同
 * @date 2020/12/14 18:12
 */
@Log4j2
public class CompareDifferentUtil {

    /**
     *比较两个对象中的有哪些字段的属性值不同
     *
     * @param before 比较对象 before
     * @param after 比较对象 after
     * @return 在不为null的情况下，返回值jsonObject中含有三个字段
     *         1. key：differentFields ，value：集合 ，不相同的字段名称。
     *         2. key：beforeJson ，value：jsonObject类型 ，Before对象中字段和值，只含有differentFields中的字段。
     *         3. key：beforeJson ，value：jsonObject类型 ，after 对象中字段和值，只含有differentFields中的字段。
     *         返回null，表示两个对象没有数值不同字段
     * @throws IntrospectionException 反射获取属性异常
     */
    public static JSONObject compare(Serializable before, Serializable after) throws IntrospectionException {

        if (before.getClass() != after.getClass()) {
            log.error("两个对象不是相同的类");
            return null;
        }
        // json后对比是否对象含有不同
        if (JSON.toJSONString(before).equals(JSON.toJSONString(after))) {
            log.warn("两个对象字段一致");
            return null;
        }
        // 获取对象信息
        BeanInfo beforeBeanInfo = Introspector.getBeanInfo(before.getClass());
        BeanInfo afterBeanInfo = Introspector.getBeanInfo(after.getClass());

        //获取对象熟悉信息
        List<PropertyDescriptor> propertyBefore = Arrays.asList(beforeBeanInfo.getPropertyDescriptors());
        List<PropertyDescriptor> propertyAfter = Arrays.asList(afterBeanInfo.getPropertyDescriptors());

        //定义返回结果
        JSONObject result = new JSONObject();
        JSONObject beforeJson = new JSONObject();
        JSONObject afterJson = new JSONObject();
        result.put("beforeJson", beforeJson);
        result.put("afterJson", afterJson);

        propertyBefore.parallelStream().forEach(pb->{
            //获取get方法
            Method readMethodBefore = pb.getReadMethod();
            try {
                //获取字段值
                Object beforeValue = readMethodBefore.invoke(before);
                propertyAfter.parallelStream().forEach(pa->{
                    // 比较是否是相同的字段
                    if (!pb.getName().equals(pa.getName())) {
                        return ;
                    }
                    // 获取只读方法
                    Method readMethodAfter = pa.getReadMethod();

                    try {
                        //获取字段值
                        Object afterValue = readMethodAfter.invoke(after);
                        //json化字符串之后对比是否一致
                        if (!JSON.toJSONString(beforeValue).equals(JSON.toJSONString(afterValue))) {
                            beforeJson.put(pb.getName(), beforeValue);
                            afterJson.put(pa.getName(), afterValue);
                            log.info("字段值不一致：{}",pa.getName());
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        // 把不同的字段的名字方进去
        result.put("differentFields", beforeJson.keySet());
        return result;
    }

//    public static void main(String[] args) throws Exception {
//        SupplierEnterpriseQualificationsBo a = new SupplierEnterpriseQualificationsBo();
//        a.setAddress("123");
//        a.setName("123");
//        SupplierEnterpriseQualificationsBo b = new SupplierEnterpriseQualificationsBo();
//        b.setAddress("123");
//        b.setName("456");
//        b.setCi("456");
//
//        JSONObject compare = compare(a, b);
//        //SerializerFeature.WRITE_MAP_NULL_FEATURES  值为null的字段也输出
//        System.out.println(JSON.toJSONString(compare, SerializerFeature.WRITE_MAP_NULL_FEATURES));
//    }
}
