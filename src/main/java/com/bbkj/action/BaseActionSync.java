package com.bbkj.action;

import com.alibaba.fastjson.JSON;
import com.bbkj.common.BaseAction;
import com.bbkj.common.redis.RedisUtil;
import com.bbkj.service.impl.AsynTaskService;
import com.bbkj.common.utils.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2022/1/4 20:19
 */
@Getter
@Setter
@Controller
@ParentPackage("post")
@Namespace("/base")
@Slf4j
public class BaseActionSync extends BaseAction {
    @Autowired
    private AsynTaskService asynTaskService;

    @Autowired
    private RedisUtil redisUtil;

    @Action("index-data")
    public String indexData() {
        Map map = new HashMap();
        try {
            String indexData = redisUtil.get("indexData");
            if (indexData != null && indexData.length() > 0 && !indexData.isEmpty()) {
                // redis缓存里面有值，使用缓存的值
                log.info("redis缓存里有值： " + indexData);
                Map result = JSON.parseObject(indexData, Map.class);
                returnData(200, "true", result);
                return null;
            }
            log.info("redis没有缓存数据，把数据找出来，再缓存到redis,方便下次快速获取！");
            Future<Page> carouselPage = asynTaskService.getCarousel();
            Future<Page> recommendPage = asynTaskService.getRecommend();
            Future<Page> catalogPage = asynTaskService.getCatalog();
            map.put("carousel", carouselPage.get().getPageList());
            map.put("recommend", recommendPage.get().getPageList());
            map.put("catalog", catalogPage.get().getPageList());
            JSONObject result = JSONObject.fromObject(map);
            redisUtil.set("indexData", result.toString(), 1, "d");
            returnData(200, "true", result);
            //log.info("全部数据获取完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
