package com.bbkj.common.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 通用工具
 * @date 2020/12/15 16:50
 */
public class CommonUtils {
    /**
     *@author JJ
     * @Description 对象变Map
     * @Date 2022/1/6 15:15
     * @param obj
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> convertToMap(Object obj) {
        try {
            if (obj instanceof Map) {
                return (Map) obj;
            }
            Map<String, Object> returnMap = BeanUtils.describe(obj);
            returnMap.remove("class");
            return returnMap;
        } catch (IllegalAccessException e1) {
            e1.getMessage();
        } catch (InvocationTargetException e2) {
            e2.getMessage();
        } catch (NoSuchMethodException e3) {
            e3.getMessage();
        }
        return new HashMap();
    }

    /**
     * @param s
     * @author JJ
     * @Description 时间戳转日期
     * @Date 2021/3/31 15:22
     * @Param [s]
     * @Return java.lang.String
     */
    public static String stampToTime(Long s) throws Exception {
        String res;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long lt = new Long(s);

//将时间戳转换为时间

        Date date = new Date(lt);

//将时间调整为yyyy-MM-dd HH:mm:ss时间样式

        res = simpleDateFormat.format(date);

        return res;

    }
}
