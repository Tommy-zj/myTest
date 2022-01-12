package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Certificate;
import com.bbkj.domain.GenericRoles;
import com.bbkj.domain.Member;
import com.bbkj.service.CertificateService;
import com.bbkj.service.CollectService;
import com.bbkj.service.GenericRolesService;
import com.bbkj.service.MemberService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Hibernate;
import org.hibernate.ObjectDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/mechanic")@Slf4j
public class MemberAction extends BaseAction {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CollectService collectService;
    private long person_id;
    private String name;
    private String mail;
    private long phone;
    private String introduce;
    private String province_name;
    private String city_name;
    private String county_name;
    private String avatar;
    private int gender;
    private int pageNo;
    private int pageSize;
    private String password;
    @Autowired
    private CertificateService certificateService;
    private long id;
    private String tag;
    Member member = new Member();
    @Autowired
    private GenericRolesService genericRolesService;


    private Page page = new Page();
    private String which;
    private String openid;

    public Member setCollect(Member member) {
        member.setName(name);
        member.setMail(mail);
        member.setPhone(phone);
        member.setIntroduce(introduce);
        member.setProvince_name(province_name);
        member.setCity_name(city_name);
        member.setCounty_name(county_name);
        member.setTag(tag);
        member.setGender(gender);
        return member;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 手机端注册新用户
     * @Date 2021/12/17 17:05
     **/
    @Action(value = "phoneAdd", interceptorRefs = @InterceptorRef("json"))
    public String phoneAdd() throws Exception {
        try {
            Boolean isNew = memberService.checkRegister(phone);
            if (isNew) {
                GenericRoles genericRoles = new GenericRoles();
                genericRolesService.save(genericRoles);
                long id = genericRoles.getId();
                Member member = new Member();
                member.setName(name);
                member.setPhone(phone);
                member.setAvatar("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F655ba5b31bc6243428a306b10a7f895b36d3d3d35a1e-5phgk4_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642388313&t=d26990c9133fce6e6ece9d6db8dc6901");
                member.setPassword(password);
                member.setId(id);
                memberService.save(member);
                responses(200, "yes");
                return null;
            }
            responses(200, "no");

        } catch (Exception e) {
            e.printStackTrace();
            responses(500, e.getMessage());
        }
        return null;
    }

    /**
     * @description: 新增
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/11 10:46
     */
    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            Member member1 = memberService.findById(person_id);
            member1.setAvatar(avatar);
            Member members = setCollect(member1);
            Hibernate.initialize(members);
            memberService.update(members);
            responses(200, "已经有记录了且导入新数据");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            responses(301, e.getMessage());
        }
        return null;
    }

    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        try {
            Member member1 = memberService.findById(person_id);
            setCollect(member1);
            memberService.update(member1);
            responses(200, "已经有记录了且导入新数据");
            return null;
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "phoneLogin", interceptorRefs = @InterceptorRef("json"))
    public String H5WebLogin() throws Exception {
        Member member = memberService.phoneLogin(phone, password);
        if (member != null) {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession sessions = request.getSession();
            sessions.setAttribute("user", member);
            return null;
        }
        response(205, "账号不存在或密码错误!");
        return null;
    }

    /**
     * @description: 同步小程序的个人信息
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/10/28 17:12
     */
    @Action(value = "synchronous", interceptorRefs = @InterceptorRef("json"))
    public String synchronous() throws Exception {
        //先判断是否为第一次登录
        Member member1 = memberService.findById(person_id);
        if (member1.getAvatar() != null && member1.getProvince_name() != null) {
            //已经同步过信息
            response(206, "已经同步过信息");
            return null;
        }
        setCollect(member1);
        //第一次同步信息
        memberService.save(member1);
        returnData(200, "消息同步成功", member1);
        return null;
    }

    /**
     * @author JJ
     * @Description 判断用户是不是注册技工
     * @Date 2021/4/11 10:52
     * @Param []
     * @Return java.lang.String
     **/
    @Action(value = "isMechanic", interceptorRefs = @InterceptorRef("json"))
    public String isMechanic() throws Exception {
        Member members = memberService.findById(person_id);
        if (members.getIntroduce() != null) {
            responses(200, "此用户是在注册技工", members.getId());
            return null;
        }
        responses(206, "不是注册技工", members.getId());
        return null;
    }


    /**
     * @description: 获取全部技工信息
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/10/24 10:09
     */
    @Action("pagelist")
    public String pagelist() throws Exception {
        try {
            Page page = memberService.pageList(pageNo, pageSize);
            List<Member> members = page.getPageList();
            List result = new ArrayList();
            for (Member member : members) {
                List certificates = certificateService.findByPerson(member.getId());
                Map memberMap = spliceMap(member, certificates);
                String create_time_string = CommonUtils.stampToTime(member.getCreate_time());
                Map map = new HashMap();
                map.put("create_time", create_time_string);
                memberMap.putAll(map);
                result.add(memberMap);
            }
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 统计全部技工的收藏商品
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/10/26 12:02
     */
    @Action("collects")
    public String mechanic_collect() throws Exception {
        try {
            Page page = memberService.pageList(pageNo, pageSize);
            List<Member> members = page.getPageList();
            List list = new ArrayList<Object>(); //个人收藏列表集
            for (Member member : members) {
                //创建一个新的技工收藏对象
                Map memberMap = new HashMap();
                memberMap.put("name", member.getName());
                //按用户的person_id获取到对应的收藏
                Number number = collectService.count(member.getId());
                memberMap.put("collectNum", number);
                memberMap.put("person_id", member.getId());
                list.add(memberMap);
            }
            pageList(page.getTotalPage(), page.getTotalNum(), pageNo, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 删除技工接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 11:19
     */
    @Action("delete")
    public String delete() throws Exception {
        Member entity = memberService.findById(id);
        memberService.remove(entity);
        notifySuccess("success to delete it!");
        return null;
    }

    /**
     * @author JJ
     * @Description 通过openid找到对应的技工
     * @Date 2021/4/11 11:31
     * @Param []
     * @Return java.lang.String
     **/
    @Action("findMember")
    public String findMember() throws ObjectDeletedException {
        try {
            Member member = memberService.findByOpenid(openid);
            if (member != null) {
                if (member.getProvince_name() != null) {
                    responses(200, "success to find!", member);
                    return null;
                }
                responses(201, "未绑定技工数据");
                return null;
            }
            responses(302, "未绑定技工数据");
        } catch (ObjectDeletedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 通过person_id来获取对应的技工信息
     * @param: * @param
     * id对应的是个人的person_id
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 11:21
     */
    @Action("personInfo")
    public String personInfo() throws ObjectDeletedException {
        try {
            Member member = memberService.findById(id);
            if (member.getOpenid() != null) {
                List list = new ArrayList();
                List<Certificate> certificates = certificateService.findByPerson(member.getId());
                if (certificates != null) {
                    for (Certificate certificate : certificates) {
                        list.add(certificate);
                    }
                }
                Map memberMap = spliceMap(member, list);
                returnData(200, "success", memberMap);
                return null;
            }
            responses(302, "没有其他技工数据");
        } catch (ObjectDeletedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 通过hql语句获取对应工的技数据，带分页器
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 11:22
     */
    @Action("pagecount")
    public String findByHql() throws Exception {
//判断which是否为空，为空则执行获取全部需求的操作，不为空则按照传值的条件找到符合条件的需求
        try {
            Page page = memberService.findByCondition(pageNo, pageSize, which, province_name, city_name, county_name);
            List<Member> members = page.getPageList();
            List result = new ArrayList();
            //把技工对应的证书放到技工数据中
            for (Member member : members) {
                List<Certificate> certificates = certificateService.findByPersonIdPass(member.getId());
                List list1 = new ArrayList();
                if (certificates != null) {
                    for (Certificate certificate1 : certificates) {
                        list1.add(certificate1);
                    }
                }
                Map memberMap = spliceMap(member, list1);
                result.add(memberMap);
            }
            countByWhich(memberService.countRecent(), memberService.countByWhich("all", "all"), result,
                    memberService.countByWhich(province_name, "province_name"), memberService.countByWhich(city_name, "city_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param member
     * @param certificates
     * @author JJ
     * @Description 拼接用户和用户证书
     * @Date 2021/3/25 11:47
     * @Param
     * @Return java.util.Map
     **/
    public Map spliceMap(Member member, List certificates) {
        Map memberMap = CommonUtils.convertToMap(member);
        Map certificateMap = new HashMap();
        certificateMap.put("certificates", certificates);
        memberMap.putAll(certificateMap);
        return memberMap;
    }

    /**
     * @param recent   近期
     * @param all      全部
     * @param result   结果集
     * @param province 省级
     * @param city     市级
     * @author JJ
     * @Description 卡片消息
     * @Date 2021/4/30 11:26
     **/
    public static void countByWhich(Number recent, Number all, Object result, Number province, Number city) throws IOException {
        Map returnMap = new HashMap();
        returnMap.put("code", 200);
        returnMap.put("recentNum", recent);
        returnMap.put("allNum", all);
        returnMap.put("data", result);
        returnMap.put("provinceNum", province);
        returnMap.put("cityNum", city);
        JSONObject source = JSONObject.fromObject(returnMap);
        writeJson(source);
    }
}
