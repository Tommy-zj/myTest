package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.DemandImg;
import com.bbkj.domain.Member;
import com.bbkj.domain.Demand;
import com.bbkj.service.DemandImgService;
import com.bbkj.service.DemandService;
import com.bbkj.service.MemberService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
public class
DemandActionWechat extends BaseAction {
    @Autowired
    private DemandService demandService;
    private long person_id;
    @Autowired
    private DemandImgService demandImgService;
    private long id;
    private String which;
    private String province_name;
    private String city_name;
    private String county_name;
    private int whichpage;
    private int pagesize;
    private int status;
    @Autowired
    private MemberService memberService;
    public static String hql;
    private int pageNo;
    private int pageSize;

    /**
     * @author JJ
     * @Description 微信小程序获取需求接口，包含不通过，未审核，未解决，已解决四个状态，一级分类可解决，考虑到个人的需求数量不多，不用使用分页器
     * 按person_id找到个人的全部需求
     * @Date 2021/3/17 11:49
     * @Param []
     * @Return java.lang.String
     **/
    @Action("wechatRequest")
    public String findByPersonId() throws Exception {
        try {
            Page page = demandService.findByPerson(person_id, status, pageNo, pageSize);
            List<Demand> requests = page.getPageList();
            List<Demand> list = new ArrayList();
            dealDemandImg(requests, list);
            returnData(200, "success", list);
        } catch (Exception e) {
            e.printStackTrace();
            response(500, e.getMessage());
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
        for (Demand demand : demands) {
            List images = new ArrayList();
            List<DemandImg> demandImgs = demandImgService.findByDemandId(demand.getId());
            if (demandImgs != null) {
                for (DemandImg img : demandImgs) {
                    images.add(img.getImage_url());
                }
            }
            Map map = CommonUtils.convertToMap(demand);
            Map requestImgMap = new HashMap();
            requestImgMap.put("request_img", images);
            map.putAll(requestImgMap);
            result.add(map);
        }
        return result;
    }

    /**
     * @description: 通过需求id获取单个的需求详情，和需求图片
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/9 16:45
     */
    @Action("details")
    public String findRequestById() throws Exception {
        try {
            if (id > 0) {
                Demand demand = demandService.findByDemandId(id);
                if (demand != null) {
                    log.info(demand.toString());
                    List demand_images = demandImgService.findByDemandId(id);
                    Map map = CommonUtils.convertToMap(demand);
                    Map imgMap = new HashMap();
                    imgMap.put("request_img", demand_images);
                    map.putAll(imgMap);
                    responses(200, "success to return request!", map);
                    return null;
                }
                response(206, "未找到此需求");
            }
        } catch (NullPointerException e) {
            response(206, "空指针异常");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author JJ
     * @Description 问题解决接口--小程序
     * @Date 2021/3/17 11:49
     * @Param []
     * @Return java.lang.String
     **/
    @Action("resolve")
    public String resolve() throws Exception {
        try {
            Demand request = demandService.findById(id);
            request.setStatus(2);
            demandService.update(request);
            notifySuccess("问题已解决！");
        } catch (ObjectNotFoundException e) {
            response(206, "找不到此需求！");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author JJ
     * @Description 删除问题接口，是彻底删除
     * @Date 2021/3/17 11:47
     * @Param []
     * @Return java.lang.String
     **/
    @Action("todelete")
    public String todelete() throws Exception {
        try {
            Demand request = demandService.findById(id);
            demandService.remove(request);
            notifySuccess("问题已删除！");
        } catch (ObjectNotFoundException e) {
            response(206, "找不到此需求！");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author JJ
     * @Description 获取到status为1的全部需求数据，并且统计不同地区的人数
     * 判断which是否为空，为空则执行获取全部需求的操作，不为空则按照传值的条件找到符合条件的需求
     * @Date 2021/3/17 11:46
     * @Param []
     * @Return java.lang.String
     **/
    @Action("pagecount")
    public String findByHqlCar() throws Exception {
        try {
            Page page = demandService.findDemandByCard(which, province_name, city_name, county_name, whichpage, pagesize);
            List<Demand> demands = page.getPageList();
            List result = new ArrayList();
            dealDemandImg(demands, result);
            MemberAction.countByWhich(demandService.countRecent(),
                    demandService.countByWhich("all", "all"), result,
                    demandService.countByWhich(province_name, "province_name"),
                    demandService.countByWhich(city_name, "city_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 查找最近发布的需求
     * @Date 2021/11/22 16:23
     **/
    @Action("findByRecent")
    public String findByRecent() throws Exception {
        try {
            Page page = demandService.findByRecent();
            returnData(200, "recent!", page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @author JJ
     * @Description 按需求的标签找到符合的技工
     * @Date 11:44
     * @Param []
     * @Return java.lang.String
     **/
    @Action("findByTag")
    public String findByTag() throws Exception {
        try {
            Demand request = demandService.findById(id);
            List<Member> members = memberService.count();
            List<Member> mechanics1 = new ArrayList<Member>();
            String[] tags = request.getTag().split(",");
            if (members != null) {
                for (String tag : tags) {
                    if (tag != null) {
                        for (Member member : members) {
                            String MTags = member.getTag();
                            if (mechanics1.indexOf(member) == -1 && pagesize * whichpage > mechanics1.size() && MTags.indexOf(tag) != -1) {
                                mechanics1.add(member);
                            }
                        }
                    }
                }
                responses(200, "找到匹配的技工", mechanics1);
                return null;
            }
            responses(206, "未找到匹配的技工");
        } catch (ObjectNotFoundException e) {
            responses(206, "未找到匹配的技工");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
