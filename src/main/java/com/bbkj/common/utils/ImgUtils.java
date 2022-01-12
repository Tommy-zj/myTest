package com.bbkj.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/10/16 20:40
 */
@Slf4j
public class ImgUtils {
    public static String time;

    /**
     * @description: 按日期新建文件夹，如果存在则不创建
     * @param: * @param destPath  图片根目录
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/10/17 9:28
     */
    public static String createDir(String destPath) {
        LocalDate date = LocalDate.now(); // get the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        time = date.format(formatter);
        log.info(time);
        new File(destPath + time);
        return destPath + time;
    }


}
