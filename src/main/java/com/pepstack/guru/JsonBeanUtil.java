/***********************************************************************
* Copyright (c) 2018 pepstack, pepstack.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
*
* 1. The origin of this software must not be misrepresented; you must not
*   claim that you wrote the original software. If you use this software
*   in a product, an acknowledgment in the product documentation would be
*   appreciated but is not required.
*
* 2. Altered source versions must be plainly marked as such, and must not be
*   misrepresented as being the original software.
*
* 3. This notice may not be removed or altered from any source distribution.
***********************************************************************/
/**
 * @file: JsonBeanUtil.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-05-29 16:51:24
 */
package com.pepstack.guru;


import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Method;
import java.text.ParseException;


public final class JsonBeanUtil {

    /**
     * 使用java的newInstance方法创建泛型类的实例
     */
    public static <T> T createInstance(Class<T> cls) {
        T obj = null;
        try {
            obj = cls.newInstance();
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }


    /**
     * java bean to map
     */
    public static Map<String, Object> BeanToMap(Object javaBean) {
        Map<String, Object> resultMap = new HashMap<>();

        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                String methodName = method.getName();

                if (methodName.startsWith("get")) {
                    // methodName = getAttributeValue
                    String attribute = methodName.substring(methodName.indexOf("get") + 3);

                    // attribute = attributeValue
                    attribute = attribute.toLowerCase().charAt(0) + attribute.substring(1);

                    // call getAttributeValue
                    Object value = method.invoke(javaBean, (Object[]) null);

                    // put into map
                    resultMap.put(attribute, value);
                }
            } catch (Exception ex) {
            }
        }

        return resultMap;
    }


    /**
     * A single json object to Map<String, Object>
     */
    public static Map<String, Object> JsonToMap(JSONObject jsonObject) {
        Map<String, Object> resultMap = new HashMap<>();

        Iterator<String> iterator = jsonObject.keys();

        String key = null;
        Object value = null;

        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.get(key);

            resultMap.put(key, value);
        }

        return resultMap;
    }


    public static <T> T MapToBean(Map<String, Object> mapData, Class<T> beanCls) {
        T javaBean = JsonBeanUtil.createInstance(beanCls);

        try {
            Method[] methods = javaBean.getClass().getDeclaredMethods();

            for (Method method : methods) {
                String methodName = method.getName();

                if (methodName.startsWith("set")) {
                    String attribute = methodName.substring(methodName.indexOf("set") + 3);

                    attribute = attribute.toLowerCase().charAt(0) + attribute.substring(1);

                    method.invoke(javaBean, new Object[] { mapData.get(attribute) });
                }
            }
        } catch (Exception e) {
        }

        return javaBean;
    }


    /**
     * 下面代码演示了如何从 json 字符串 (存储了对象字典的json字符串) 转换到 json 对象：
     *  对象是一个 java bean。
     *
        String jsonMapString = "{"+
            "    '80c42cdba55d859e15482e351bdf3147':{"+
            "        'realIp':'127.0.0.1',"+
            "        'validThru':1526009698288,"+
            "        'deviceTag':'UNKNOWN-UNKNOWN',"+
            "        'lastUpdate':1524713698288,"+
            "        'validFrom':1524713698288,"+
            "        'loginCount':1"+
            "    },"+
            "    '90c42cdba55d859e15482e351bdf3189':{"+
            "        'realIp':'127.0.0.2',"+
            "        'validThru':1526009698289,"+
            "        'deviceTag':'UNKNOWN-UNKNOWN2',"+
            "        'lastUpdate':1524713698288,"+
            "        'validFrom':1524713698288,"+
            "        'loginCount':3"+
            "    }"+
            "}";

        Map<String, AgentDevice> deviceMap = JsonMapToBeanMap(jsonMapString, AgentDevice.class);

        JSONObject jsonObject = JSONObject.fromObject(deviceMap);

        String jsonMapStringOut = jsonObject.toString();
    */
    public static <T> Map<String, T> JsonMapToBeanMap(String jsonMapString, Class<T> beanCls) {
        JSONObject jsonObjMapColl = JSONObject.fromObject(jsonMapString);

        Map<String, T> beanMap = new HashMap<>();

        Iterator<String> iterator = jsonObjMapColl.keys();

        String key = null;
        String value = null;

        while (iterator.hasNext()) {
            key = iterator.next();

            // value is a map string
            value = jsonObjMapColl.getString(key);

            JSONObject jsonObj = JSONObject.fromObject(value);

            Map<String, Object> valueMap = JsonBeanUtil.JsonToMap(jsonObj);

            T newBean = JsonBeanUtil.MapToBean(valueMap, beanCls);

            beanMap.put(key, newBean);
        }

        return beanMap;
    }
}
