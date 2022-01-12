package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.GetLocalTime;
import com.bbkj.common.utils.NoticeUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.ProductExtra;
import com.bbkj.domain.ShopMessage;
import com.bbkj.domain.ShopOrders;
import com.bbkj.service.ProductExtraService;
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

import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 商品订单模块
 * @date 2020/11/11 10:24
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/orders")
@Slf4j
public class ShopOrdersAction extends BaseAction {
    private long id;
    private String order_number; //订单号
    private String buyer_name; //购买人姓名
    private long buyer_id; //购买人的id
    private long billing_id; // 发布需求人id
    private String end_time; //结束时间
    private int num;
    private double amount; //金额
    private int status; //0为支付了定金，待发货，1.待收货，2待签收
    private long secondSort_id;//商品二级放哪儿了
    private long shop_id; //商品的id
    private String shop_name; //商品名称
    private String shop_numbering; //商品的型号
    private String address; //收货地址
    private long phone; // 联系电话
    @Autowired
    private ProductOrdersService productOrdersService;
    @Autowired
    private ProductExtraService productExtraService;
    private int pageNo;
    private int pageSize;
    private String delivery;//交货方式
    private String remarks;//备注
    private String shop_img;
    private long[] ids;


    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            if (buyer_id > 0) {
                ShopOrders orders = createOrders(); //新建订单
                productOrdersService.save(orders); //报存订单
                ShopMessage message = createMessage(buyer_id); //新建消息
                message.setMsg("您已经成功支付购买商品:" + orders.getShop_name() + "的定金。共" + num + "件，总金额为:" + num * amount);
                NoticeUtils.notice(message, buyer_id, 0); //发送消息
                //商品的销量+1
                calculateSales(shop_id, num);
                responses(200, "add success!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "deletes", interceptorRefs = @InterceptorRef("json"))
    public String delete() throws Exception {
        try {
            if (ids.length > 0) {
                productOrdersService.remove(ids);
                responses(200, "success to delete!");
                return null;
            }
            responses(201, "传值为空");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "edit")
    public String edit() throws Exception {
        try {
            ShopOrders orders = createOrders();
            orders.setId(id);
            productOrdersService.update(orders);
            responses(200, "success to update");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action("list")
    public String list() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(ShopOrders.class);
            criteria.addOrder(Order.desc("id"));
            if (buyer_id > 0) {
                criteria.add(Restrictions.eq("buyer_id", buyer_id));
            }
            criteria.add(Restrictions.eq("status", status));
            Page page = productOrdersService.pageList(criteria, pageNo, pageSize);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
            responses(500, e.getMessage());
        }

        return null;
    }


    public ShopOrders createOrders() {
        ShopOrders orders = new ShopOrders();
        orders.setOrder_number(NoticeUtils.createOrderNumber());
        orders.setBuyer_id(buyer_id);
        orders.setBilling_id(billing_id);
        orders.setEnd_time(end_time);
        orders.setAmount(amount);
        orders.setStatus(status);
        orders.setShop_id(shop_id);
        orders.setShop_name(shop_name);
        orders.setShop_numbering(shop_numbering);
        orders.setAddress(address);
        orders.setPhone(phone);
        orders.setNum(num);
        orders.setDelivery(delivery);
        orders.setSecondSort_id(secondSort_id);
        orders.setBuyer_name(buyer_name);
        orders.setRemarks(remarks);
        orders.setShop_img(shop_img);
        return orders;
    }

    public ShopMessage createMessage(long buyer_id) {
        //告诉购买的用户你给了钱，等着发货
        ShopMessage message = new ShopMessage();
        NoticeUtils.person_id = String.valueOf(buyer_id);
        message.setBuyer_id(buyer_id);
        message.setCreate_id(0);
        message.setTimestamp(System.currentTimeMillis());
        message.setCtime(GetLocalTime.localTime());
        return message;
    }

    /**
     * @param num
     * @description: 计算销量
     * @param: * @param shop_id
     * @return: void
     * @author JJ
     * @date: 2020/11/13 9:30
     */
    private void calculateSales(long shop_id, int num) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(ProductExtra.class).add(Restrictions.eq("product_id", shop_id));
        List<ProductExtra> productExtras = productExtraService.list(criteria1);
        if (productExtras.size() > 0) {
            ProductExtra productExtra = productExtras.get(0);
            productExtra.setCount(productExtra.getCount() + num);
            productExtraService.update(productExtra);
        }
    }

}
