package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.redis.RedisUtil;
import com.bbkj.domain.Member;
import com.bbkj.domain.Merchant;
import com.bbkj.domain.User;
import com.bbkj.service.MemberService;
import com.bbkj.service.MerchantService;
import com.bbkj.service.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/4/19 11:32
 */

@Setter
@Getter
@Controller
@Slf4j
@ParentPackage("post")
@Namespace("/register")
public class LoginAction extends BaseAction {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    private String name;
    private String password;
    private long phone;
    private long person_id;
    private String oldPassword;


    /**
     * @return java.lang.String
     * @author JJ
     * @Description 手机网站用户登录
     * @Date 2021/11/30 11:01
     **/
    @Action(value = "phoneLogin", interceptorRefs = {@InterceptorRef("json")})
    public String phoneLogin() throws Exception {
        if (phone != 0L) {
            try {
                Member member = memberService.phoneLogin(phone, password);
                //log.info("空你吉瓦");
                if (member != null) {
                    // 有对应的用户
                    //密码正确
                    storageSession(member.getPhone(), member.getPassword(), member.getId());
                    responses(200, "yes", createMember(member));
                    return null;
                }
                responses(200, "no");
            } catch (Exception e) {
                response(204, "账号不存在");
                e.printStackTrace();
            }
            return null;
        }
        response(204, "账号为空");
        return null;
    }

    @Action("logout")
    public String phoneLogout() {
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            String personId = request.getHeader("personId");
            redisUtil.set(personId, "");
            response(200, "ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map createMember(Member member) {
        Map resultMap = new HashMap();
        resultMap.put("id", member.getId());
        resultMap.put("name", member.getName());
        resultMap.put("mail", member.getMail());
        resultMap.put("phone", member.getPhone());
        resultMap.put("introduce", member.getIntroduce());
        resultMap.put("province_name", member.getProvince_name());
        resultMap.put("city_name", member.getCity_name());
        resultMap.put("county_name", member.getCounty_name());
        resultMap.put("create_time", member.getCreate_time());
        resultMap.put("avatar", member.getAvatar());
        resultMap.put("tag", member.getTag());
        resultMap.put("gender", member.getGender());
        return resultMap;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 管理员登陆
     * @Date 2021/11/24 17:34
     **/
    @Action(value = "login",
            interceptorRefs = {@InterceptorRef("json")})
    public String login() throws Exception {
        try {
            if (name != null || name.equals("")) {
                List<User> users = userService.findByName(name);
                if (users.size() > 0) {
                    for (User user : users) {
                        if (user.getPassword().equals(password)) {
                            /**log.info("注意注意，这是登陆成功的信号，后台登录成功!");
                             log.info("查看保存的用户信息");
                             log.info(user);
                             //将个人信息保存到session
                             **/
                            this.storageSession(name, password, user.getId());
                            responses(200, "welcome", user);
                            return null;
                        }
                    }
                    responses(501, "账号密码错误");
                    return null;
                }
                responses(300, "该用户不存在");
                return null;
            }
            responses(206, "必要参数为空！");
            return null;
        } catch (NumberFormatException e) {
            responses(206, "输入格式错误！");
        } catch (Exception e) {
            responses(300, "该用户不存在");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 商户用户登陆
     * @Date 2021/4/22 16:09
     **/
    @Action(value = "merchant", interceptorRefs = {@InterceptorRef("json")})
    public String merchant() throws Exception {
        try {
            Merchant merchant = merchantService.findByPhone(phone);
            if (merchant != null) {
                if (merchant.getPassword().equals(password)) {
                    //验证成功
                    Map map = new HashMap();
                    map.put("name", phone);
                    map.put("password", password);
                    map.put("id", merchant.getPerson_id());
                    storageSession(phone, password, merchant.getPerson_id());
                    responses(200, "welcome", map);
                    return null;
                }
                responses(501, "账号密码错误");
                return null;
            }
            responses(300, "该用户不存在");
        } catch (NumberFormatException e) {
            responses(206, "参数格式错误!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 修改密码
     * @Date 2021/4/30 17:04
     **/
    @Action(value = "editPassword", interceptorRefs = @InterceptorRef("json"))
    public String editPassword() throws Exception {
        try {
            Merchant merchant = merchantService.findById(person_id);
            if (oldPassword.equals(merchant.getPassword())) {
                if (password.equals(merchant.getPassword())) {
                    responses(206, "新密码与原密码一致!");
                    return null;
                }
                merchant.setPassword(password);
                merchantService.update(merchant);
                responses(200, "密码修改成功！");
                return null;
            }
            responses(206, "原密码错误！");
        } catch (ObjectNotFoundException e) {
            responses(206, "账号不存在！");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param name
     * @param password
     * @param person_id
     * @author JJ
     * @Description 保存用户的信息并返回session
     * @Date 2021/4/22 16:01
     **/
    private Map storageSession(Object name, String password, long person_id) {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        /**
         log.info("登录时的sessionId");
         log.info(session.getId());
         log.info("保存了一个用户的信息");
         **/
        Map map = new HashMap();
        map.put("name", name);
        map.put("password", password);
        map.put("id", person_id);
        session.setAttribute("info", map);
        JSONObject jsonObject = JSONObject.fromObject(map);
        redisUtil.set(String.valueOf(person_id), jsonObject.toString());
        // String info = redisDao.get(String.valueOf(person_id));
        // log.info("登录后获取redis中的个人数据: " + info);
        return map;
    }
}
