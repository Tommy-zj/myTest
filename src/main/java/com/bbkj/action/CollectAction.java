package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.GetLocalTime;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Collect;
import com.bbkj.service.CollectService;
import freemarker.ext.beans.NumberModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;


@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/collect")
@Slf4j
public class CollectAction extends BaseAction {
    @Autowired
    private CollectService collectService;
    private Collect collect = new Collect();
    private long person_id;
    private long product_id;
    private String ctime;
    private long id;
    private String product_name;
    private double product_price;
    private String product_img;
    private Long second_id;
    private int pageNo;
    private int pageSize;
    private Page page = new Page();

    public Collect setcollect() {
        collect.setPerson_id(person_id);
        collect.setProduct_id(product_id);
        collect.setProduct_img(product_img);
        collect.setProduct_price(product_price);
        collect.setProduct_name(product_name);
        collect.setSecond_id(second_id);
        return collect;
    }

    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            List<Collect> collects = collectService.find(person_id, product_id);
            if (collects.size() > 0) {
                //有收藏，删除
                for (Collect collect : collects)
                    collectService.remove(collect);
                responses(202, "删除收藏成功！");
                return null;
            }
            collect.setCtime(GetLocalTime.localTime());
            Collect collect = setcollect();
            collectService.save(collect);
            responses(200, "添加收藏成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        try {
            Collect collect = setcollect();
            collect.setId(id);
            collect.setCtime(ctime);
            collectService.update(collect);
            notifySuccess("success to update!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 带分页器的获取收藏列表接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/15 11:23
     */
    @Action("pagelist")
    public String findByPersonId() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Collect.class)
                    .addOrder(Order.desc("id"))
                    .add(Restrictions.eq("person_id", person_id));
            page = collectService.pageList(criteria, pageNo, pageSize);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @description: 按商品id查找收藏商品，看是否有，有返回true，没有返回null
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/16 11:00
     */
    @Action("find")
    public String findOneCollect() throws Exception {
        try {
            boolean isCollect = collectService.isCollect(product_id, person_id);
            returnData(200, "shop collect?", isCollect);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @description: 按product_id和person_id删除收藏商品
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/15 11:40
     */
    @Action("delete")
    public String delete() throws Exception {
        try {
            List<Collect> collects = collectService.find(person_id, product_id);
            for (Collect collect : collects) {
                collectService.remove(collect);
            }
            notifySuccess("success to delete");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @description: 按id删除对应的收藏商品
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/16 19:15
     */
    @Action(value = "remove")
    public String remove() throws Exception {
        try {
            long[] ids = new long[1];
            ids[0] = id;
            collectService.remove(ids);
            notifySuccess("success to remove!");
        } catch (Exception e) {
            notifyError("error to remove!");
        }
        return null;
    }
}
