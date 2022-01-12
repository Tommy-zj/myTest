package com.bbkj.service.impl;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Carousel;
import com.bbkj.domain.Catalog;
import com.bbkj.service.CarouselService;
import com.bbkj.service.CatalogService;
import com.bbkj.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 新建异步方法
 * @date 2022/1/5 9:28
 */
@Slf4j
@Service
public class AsynTaskService {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CatalogService catalogService;

    /*
    这里可以注入spring中管理的其他bean，这也是使用spring来实现多线程的一大优势
    @Async 标注此任务为异步任务，在执行此方法的时候，会单独开启线程来执行
     */
    @Async
    public Future<Page> getCarousel() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Carousel.class);
        Page page = carouselService.pageList(criteria, 1, 8);
        log.info("轮播图获取完成");
        return new AsyncResult<>(page);
    }

    @Async
    public Future<Page> getRecommend() throws Exception {
        //page为取出的那页数据
        try {
            Page page = productService.findByRecommend(1, 1, 5);
            log.info("推荐商品获取完成");
            return new AsyncResult<>(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.util.concurrent.Future<com.bbkj.common.utils.Page>
     * @author JJ
     * @Description 有返回值
     * @Date 2022/1/6 15:57
     **/
    @Async
    public Future<Page> getCatalog() throws Exception {
        try {
            List result = new ArrayList();
            //带分页器
            Page page = catalogService.findFather(1, 5);
            List<Catalog> catalogs = page.getPageList();
            setSort(catalogs, result);
            log.info("商品分类获取完成");
            return new AsyncResult<>(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //包装分类,sorts1是二级分类，sort是操作的一级分类
    public List setSort(List<Catalog> catalogs, List result) {
        for (Catalog item : catalogs) {
            List<Catalog> sorts1 = catalogService.findChild(item.getId());
            Map map = new HashMap();
            map.put("catalogs", sorts1);
            map.put("icon", item.getIcon());
            map.put("catalog_name", item.getCatalog_name());
            map.put("paren_id", item.getParen_id());
            map.put("id", item.getId());
            result.add(map);
        }
        return result;
    }
}
