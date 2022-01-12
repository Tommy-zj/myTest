package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Return;
import com.bbkj.domain.ReturnImg;
import com.bbkj.domain.ShopOrders;
import com.bbkj.service.ReturnImgService;
import com.bbkj.service.ReturnService;
import com.bbkj.service.ProductOrdersService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 商品退款模块
 * @date 2020/11/16 10:21
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/orders")@Slf4j
public class ShopReturnAction extends BaseAction {
    private long shop_id;
    private String[] return_img;
    private int shop_status;
    private String return_reason;
    private double return_money;
    private int return_mode;
    private long id;
    private int isPass;
    private long person_id;
    @Autowired
    private ReturnImgService returnImgService;
    @Autowired
    private ReturnService returnService;
    @Autowired
    private ProductOrdersService productOrdersService;
    private int pageNo;
    private int pageSize;

    @Action(value = "return", interceptorRefs = @InterceptorRef("json"))
    public String add() {
        if (shop_id > 0) {
            ShopOrders shopOrders = productOrdersService.findAdressById(shop_id);
            Return r1 = createReturn();
            //将下单的时间和订单单号添加进撤销订单中
            r1.setOrder_number(shopOrders.getOrder_number());
            r1.setPay_time(shopOrders.getPay_time());
            r1.setShop_status(shopOrders.getStatus());
            r1.setPhone(shopOrders.getPhone());
            returnService.save(r1);
            for (String img : return_img) {
                ReturnImg returnImg = createReturnImg(img, r1.getId());
                returnImgService.save(returnImg);
            }
            //修改商品订单的状态
            shopOrders.setStatus(3);
            productOrdersService.update(shopOrders);

        }
        return null;
    }

    public Return createReturn() {
        Return r1 = new Return();
        r1.setPerson_id(person_id);
        r1.setShop_id(shop_id);
        r1.setShop_status(shop_status);
        r1.setReturn_reason(return_reason);
        r1.setReturn_money(return_money);
        r1.setReturn_mode(return_mode);
        return r1;
    }

    public ReturnImg createReturnImg(String img, long id) {
        ReturnImg returnImg = new ReturnImg();
        returnImg.setReturn_id(id);
        returnImg.setReturn_img(img);
        return returnImg;
    }

    /**
     * @description: 处理撤销订单
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/17 18:09
     */
    @Action("dealReturn")
    public String dealReturn() throws Exception {
        if (id > 0) {
            ShopOrders orders = productOrdersService.findAdressById(id);
            if (isPass == 0) {
                orders.setStatus(6);
            } else if (isPass == 1) {
                orders.setStatus(4);
            }
            productOrdersService.update(orders);
        }
        return null;
    }

    /**
     * @description: 获取撤销订单列表
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/18 10:25
     */
    @Action("returnList")
    public String list() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(Return.class);
        if (person_id > 0) {
            criteria.add(Restrictions.eq("person_id", person_id));
        }
        try {
            criteria.addOrder(Order.desc("id"));
            Page page = returnService.pageList(criteria, pageNo, pageSize);
            List<Return> returns = page.getPageList();
            DetachedCriteria criteria1 = DetachedCriteria.forClass(ReturnImg.class);
            List result = new ArrayList();
            for (Return item : returns) {
                criteria1.add(Restrictions.eq("return_id", item.getId()));
                List<ReturnImg> returnImgs = returnImgService.list(criteria1);
                Map map = CommonUtils.convertToMap(item);
                Map imgMap = new HashMap();
                imgMap.put("return_imgs", returnImgs);
                map.putAll(imgMap);
                result.add(map);
            }
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);
        } catch (Exception e) {
            response(200, e.getMessage());
        }

        return null;
    }

}
