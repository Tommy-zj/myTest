package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.GetLocalTime;
import com.bbkj.common.utils.NoticeUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.ControlCenter;
import com.bbkj.domain.Message;
import com.bbkj.domain.Demand;
import com.bbkj.service.ControlCenterService;
import com.bbkj.service.DemandService;
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

import java.io.IOException;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 需求订单控制中心模块
 * @date 2020/10/30 15:00
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/system")
@Slf4j
public class ControlCenterAction extends BaseAction {
    @Autowired
    private ControlCenterService controlCenterService;
    @Autowired
    private DemandService demandService;
    private long orders_id; //接单人的id
    private long billing_id; // 发布需求人id
    private String create_time = GetLocalTime.localTime();
    private long create_timestamp = System.currentTimeMillis();
    private String pay_time;
    private String end_time;
    private int bounty;
    private int status;
    private long request_id; //需求的id
    private long pay_timestamp;
    private long id;
    private int pageNo;
    private int pageSize;
    private String content;
    private String billing_name;
    private String address;


    @Action(value = "add",interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        //增加一个订单后需要提示发布需求的人缴费
        //创建前先查看有没记录，防止重复接单
        if (NoticeUtils.isOrder(request_id) == false) {  //未被接单
            NoticeUtils.modifyState(); //修改状态id
            if (isExist() == false) {
                ControlCenter controlCenter = createControlCenter();
                controlCenterService.save(controlCenter);
                responses(200, "新增需求订单成功");
                //后台向发起需求的人发送被接单消息
                Message message = createMessage(billing_id, orders_id); //创建新消息
                message.setMsg("您发布的需求被技工接单，请按时付佣金，以便技工帮您解决问题！");
                NoticeUtils.notice(message, message.getOther_id(), message.getCreate_id());
            } else {
                responses(206, "订单已存在");
            }
        } else { //已被接单
            responses(206, "订单存已在");
        }
        return null;
    }

    /**
     * @description: 订单支付功能
     * @param: * @param 需求的id，接单人id，发需求人id
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/7 14:36
     */
    @Action("pay")
    public String pay() throws IOException {
        //改变需求的state_id;
        if (request_id > 0) {
            Demand request = demandService.findById(request_id);
            request.setStatus(4);
            //改好需求状态后通知接单人出发
            editOrders();
            String adress = request.getProvince_name() + request.getCity_name() + request.getCounty_name() + request.getAddress();
            Message message = createMessage(orders_id, billing_id);
            message.setMsg("用户:" + request.getName() + "的需求:" + request.getContent() + "订单已支付，请前往" + adress + "提供技术支持！");
            NoticeUtils.notice(message, message.getOther_id(), message.getCreate_id());
            responses(200, "支付完成");
        }
        return null;
    }

    /**
     * @description: 同步订单的支付时间，并通知一下接单人。
     * @param: * @param
     * @return: void
     * @author JJ
     * @date: 2020/11/7 15:06
     */
    public void editOrders() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ControlCenter.class);
        criteria.add(Restrictions.eq("request_id", request_id));
        List<ControlCenter> controlCenters = controlCenterService.list(criteria);
        if (controlCenters.size() > 0) {
            ControlCenter controlCenter = controlCenters.get(0);
            controlCenter.setPay_timestamp(System.currentTimeMillis());
            controlCenter.setPay_time(GetLocalTime.localTime());
            controlCenterService.update(controlCenter);
        }
    }


    /**
     * @description: 解决需求，需求的状态，订单的状态，系统通知用户
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/10 15:44
     */
    @Action("resolve")
    public String resolve() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.add(Restrictions.eq("id", id));
        List<Demand> demands = demandService.list(criteria);
        if (demands.size() > 0) {
            Demand request = demands.get(0);
            request.setStatus(5);
            demandService.update(request);
            editEnd();
        } else {
            responses(400, "找不到此需求");
        }
        return null;
    }

    /**
     * @description: 设置订单的结束时间，也就是发订单的人点击需求已完成
     * @param: * @param
     * @return: void
     * @author JJ
     * @date: 2020/11/10 15:06
     */
    public void editEnd() throws IOException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ControlCenter.class);
        criteria.add(Restrictions.eq("request_id", id));
        List<ControlCenter> controlCenters = controlCenterService.list(criteria);
        if (controlCenters.size() > 0) {
            ControlCenter controlCenter = controlCenters.get(0);
            controlCenter.setEnd_time(GetLocalTime.localTime());
            controlCenterService.update(controlCenter);
            //在这里通知接单人，订单已完成
            Message message = createMessage(controlCenter.getOrders_id(), controlCenter.getBilling_id());
            message.setMsg("用户:" + controlCenter.getBilling_name() + "的需求:" + controlCenter.getContent() + "已经确认完成，费用会在核实后发往您的微信");
            NoticeUtils.notice(message, message.getOther_id(), message.getCreate_id());
            //在这里通知自己，自己点击订单完成
            Message message1 = createMessage(controlCenter.getBilling_id(), controlCenter.getOrders_id());
            message1.setMsg("您的需求：" + controlCenter.getContent() + "已经确认完成！");
            NoticeUtils.notice(message1, message.getOther_id(), message.getCreate_id());
        } else {
            responses(200, "下单用户未等其他用户下单便解决了需求");
        }
    }
    @Action("delete")
    public String delete() throws Exception {
        if (id != 0) {
            try {
                ControlCenter controlCenter = controlCenterService.findAdressById(id);
                controlCenterService.remove(controlCenter);
            } catch (NullPointerException e) {
                responses(500, "没找到对应的数据");
            }
        } else {
            responses(500, "id为空");
        }
        return null;
    }

    @Action("edit")
    public String edit() throws Exception {
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setId(id);
        controlCenterService.update(controlCenter);
        return null;
    }

    @Action("list")
    public String list() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(ControlCenter.class);
        criteria.addOrder(Order.desc("id"));
        criteria.addOrder(Order.desc("pay_timestamp"));
        if (orders_id != 0) {
            //返回给接单人
            criteria.add(Restrictions.eq("orders_id", orders_id));
        }
        if (billing_id != 0) {
            //返回给发单人
            criteria.add(Restrictions.eq("billing_id", billing_id));
        }
        Page page = controlCenterService.pageList(criteria, pageNo, pageSize);
        pageList(page.getTotalPage(), page.getTotalNum(), pageNo, page.getPageList());
        return null;
    }


    /**
     * @description: 创建一个新的订单
     * @param: * @param
     * @return: com.beped.controlCenter.domain.ControlCenter
     * @author JJ
     * @date: 2020/11/2 14:53
     */
    public ControlCenter createControlCenter() {
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setOrders_id(orders_id);
        controlCenter.setBilling_id(billing_id);
        controlCenter.setPay_time(pay_time);
        controlCenter.setEnd_time(end_time);
        controlCenter.setBounty(bounty);
        controlCenter.setStatus(status);
        controlCenter.setPay_time(pay_time);
        controlCenter.setRequest_id(request_id);
        controlCenter.setContent(content);
        controlCenter.setBilling_name(billing_name);
        controlCenter.setAddress(address);
        return controlCenter;
    }

    /**
     * @description: 创建一条新的消息
     * @param: * @param
     * @return: com.beped.socket.Message
     * @author JJ
     * @date: 2020/11/2 14:53
     */
    public Message createMessage(long billing_id, long orders_id) {
        //叫发单的用户给钱
        Message message = new Message();
        message.setOther_id(billing_id); //发布需求人的id
        NoticeUtils.person_id = String.valueOf(billing_id);
        message.setCreate_id(0);
        message.setIsSystem(1);
        message.setTimestamp(System.currentTimeMillis());
        message.setCtime(GetLocalTime.localTime());
        message.setOrders_id(orders_id); //接单人的id
        return message;
    }

    /**
     * @description: 检查订单是否存在
     * @param: * @param
     * @return: boolean
     * @author JJ
     * @date: 2020/11/2 14:39
     */
    public boolean isExist() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ControlCenter.class);
        criteria.add(Restrictions.eq("orders_id", orders_id));
        criteria.add(Restrictions.eq("request_id", request_id));
        List<ControlCenter> controlCenters = controlCenterService.list(criteria);
        if (controlCenters.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
