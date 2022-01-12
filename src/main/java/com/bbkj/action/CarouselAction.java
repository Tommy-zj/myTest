package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Carousel;
import com.bbkj.service.CarouselService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Controller
@ParentPackage("post")
@Namespace("/carousel")
@Slf4j
public class CarouselAction extends BaseAction {
    @Autowired
    private CarouselService carouselService;
    private List list = new ArrayList();
    private Carousel carousel = new Carousel();
    private Long id;
    private long[] ids;
    private int pageSize;
    private int pageNo;
    private String image_url;
    private int image_size;
    private String image_name;

    public void createCarousel() {
        carousel.setImage_name(image_name);
        carousel.setImage_size(image_size);
        carousel.setImage_url(image_url);
    }

    @Action("test")
    public String test() throws IOException {
        notifySuccess("hello world!");
        return null;
    }

    //新增图片接口
    @Action(value = "add", interceptorRefs = {@InterceptorRef("json")})
    public String add() throws Exception {
        if (image_url != null) {
            createCarousel();
            carouselService.save(carousel);
            notifySuccess("add image success");
            return null;
        }
        responses(500, ".....");
        return null;
    }

    @Action(value = "delete")
    public String delete() throws Exception {
        Carousel entity = carouselService.findById(id);
        carouselService.remove(entity);
        String tips = "delete img entity succeed";
        notifySuccess(tips);
        return null;
    }

    //批量删除（根据传入的ids）
    @Action("manyDelete")
    public String manyDelete() throws Exception {
        try {
            carouselService.remove(ids);
        } catch (Exception e) {
            notifyError("failed to delete!");
        }
        return null;
    }

    //加入分页器
    //whichpage取哪一页的数据
    //pagesze设置每一页的数量
    @Action(value = "pagelist")
    public String list() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Carousel.class);
            criteria.addOrder(Order.desc("id"));
            Page page = carouselService.pageList(criteria, 1, 7);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Action(value = "deletes", interceptorRefs = @InterceptorRef("json"))
    public String deletes() throws Exception {
        if (ids != null) {
            carouselService.remove(ids);
        } else {
            log.info("ids为空");
        }
        return null;
    }

    //查找图片接口，返回全部
    @Action("getAll")
    public String getAll() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(Carousel.class);
        list = carouselService.list(criteria);
        Map data = new HashMap();
        data.put("data", list);
        JSONObject datas = JSONObject.fromObject(data);
        writeJson(datas);
        return null;
    }

}
