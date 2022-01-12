package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.GetIp;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.DemandImg;
import com.bbkj.domain.Demand;
import com.bbkj.service.DemandImgService;
import com.bbkj.service.DemandService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/request")
@Slf4j
public class DemandAction extends BaseAction {
    @Autowired
    private DemandService demandService;
    @Autowired
    private DemandImgService demandImgService;
    private Demand demand = new Demand();
    private DemandImg demandImg = new DemandImg();
    private String img;
    private long demand_id;
    private List request_img;
    private long person_id;
    private String name;
    private long phone;
    private String content;
    private String shop_type;
    private String province_name;
    private String city_name;
    private String remark;
    private String description;
    private String county_name;
    private String address;
    private DemandImg[] imgs; //前端传来的图片；
    private long id;
    private int whichpage; //分页器，定位到哪一页
    private int pagesize; // 设置一页有多少项
    private int status = 0; //需求的状态id
    private String which; //小程序获取需求的不同地区要求
    private long[] ids; //批量删除的id数组
    private double bounty;
    private String tag;
    private boolean disable;


    public static String hql;

    /**
     * @description: 构造一个新的对象
     * @param: * @param
     * @return: com.beped.request.domain.Request
     * @author JJ
     * @date: 2020/9/11 10:27
     */
    public Demand createRequest() {
        Demand request1 = new Demand();
        request1.setPerson_id(person_id);
        request1.setName(name);
        request1.setPhone(phone);
        request1.setContent(content);
        request1.setShop_type(shop_type);
        request1.setProvince_name(province_name);
        request1.setCity_name(city_name);
        request1.setCounty_name(county_name);
        request1.setDescription(description);
        request1.setStatus(status);
        request1.setAddress(address);
        request1.setBounty(bounty);
        request1.setTag(tag);
        return request1;
    }

    @Action(value = "add", interceptorRefs = {@InterceptorRef(value = "json")})
    public String addNew() throws Exception {
        try {
            demand = createRequest();
            HttpServletRequest request = ServletActionContext.getRequest();
            String remote_user = GetIp.getIpAddress(request);
            demand.setRemote_user(remote_user);
            demandService.save(demand);
            if (imgs != null) {
                for (DemandImg img : imgs) {
                    //每次添加新的图片都new一个新的对象，不然每次都是只保存最后一个
                    img.setDemand_id(demand.getId());
                    demandImgService.save(img);
                }
            }
            notifySuccess("success to save");
        } catch (Exception e) {
            e.printStackTrace();
            responses(500, e.getMessage());
        }
        return null;
    }

    @Action(value = "edit", interceptorRefs = {@InterceptorRef(value = "json")})
    public String edit() throws Exception {
        //编辑后，要重新审核才能被发布
        //request = createRequest();
        demand.setId(id);
        //设置需求的状态码为0;
        demand.setStatus(0);
        demandService.update(demand);
        if (imgs.length > 0) {
            //更改，先删后保存新的
            List<DemandImg> delete = demandImgService.findByRequestId(id);
            for (DemandImg deleteImg : delete) {
                demandImgService.remove(deleteImg);
            }
            for (DemandImg img : imgs) {
                DemandImg demandImg = new DemandImg();
                demandImg.setImage_url(img.getImage_url());
                demandImg.setImage_size(img.getImage_size());
                demandImg.setImage_name(img.getImage_name());
                demandImg.setDemand_id(id);
                demandImgService.save(demandImg);
            }
        }
        return null;
    }

    @Action(value = "delete")
    public String delete() throws Exception {
        demand = demandService.findById(id);
        demandService.remove(demand);
        List<DemandImg> imgs1 = demandImgService.findByRequestId(id);
        for (DemandImg demandImg : imgs1) {
            demandImgService.remove(demandImg);
        }
        notifySuccess("success to delete");
        return null;
    }


    /**
     * @description: 审核需求接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/3 14:39
     */
    @Action(value = "review", interceptorRefs = @InterceptorRef("json"))
    public String review() throws Exception {
        try {
            Demand request = demandService.findById(id);
            request.setStatus(status);
            //改变状态id，并且提交不通过的原因
            if (remark != null) {
                request.setRemark(remark);
            }
            demandService.update(request);
            notifySuccess("success to review");
        } catch (Exception e) {
            notifyError("undefine condition" + e.getMessage());
        }

        return null;
    }

    @Action(value = "deletes", interceptorRefs = @InterceptorRef("json"))
    public String deletes() throws Exception {
        if (ids.length > 0) {
            demandService.remove(ids);
            log.info("删除成功");
        } else {
            log.info("ids为空");
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 将需求设置为不可用
     * @Date 2021/11/22 16:27
     **/
    @Action("disable")
    public String disable() throws Exception {
        try {
            Demand demand = demandService.findById(id);
            demand.setDisable(!disable);
            demandService.update(demand);
            responses(200, "修改成功！");
        } catch (Exception e) {
            responses(200, "禁用成功", e.getMessage());
        }
        return null;
    }

    /**
     * @description: 按不同的状态id获取不同的需求
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/21 10:47
     */
    @Action(value = "list")
    public String pages() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
            if (status <= 5) {
                criteria.add(Restrictions.eq("status", status));
            }
            Page page = demandService.pageList(criteria, whichpage, pagesize);
            List<Demand> requests = page.getPageList();
            List last = new ArrayList();
            dealDemandImg(requests, last);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), last);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param demands 需要处理的需求数组
     * @param result  装返回的结果，为List
     * @return java.util.List
     * @author JJ
     * @Description 处理每个需求的图片
     * @Date 2021/4/19 15:25
     **/
    public List dealDemandImg(List<Demand> demands, List result) {
        for (Demand request : demands) {
            long id = request.getId();
            List<DemandImg> demandImgs = demandImgService.findByDemandId(id);
            List requestImgs = new ArrayList();
            for (DemandImg demandImg : demandImgs) {
                requestImgs.add(demandImg.getImage_url());
            }
            Map map = CommonUtils.convertToMap(request);
            Map requestImgMap = new HashMap();
            requestImgMap.put("request_img", requestImgs);
            map.putAll(requestImgMap);
            result.add(map);
        }
        return result;
    }

    //通过状态id获取到相应的数据--全部需求
    @Action("pagelist")
    public String findByHql() throws Exception {
        try {
            List<Demand> list = demandService.findByHql(status, whichpage, pagesize);
            List result = new ArrayList();
            dealDemandImg(list, result);
            Map data = new HashMap();
            data.put("data", result);
            JSONObject source = JSONObject.fromObject(data);
            writeJson(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


