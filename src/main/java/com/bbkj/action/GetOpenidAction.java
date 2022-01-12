package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.HttpRequest;
import com.bbkj.domain.GenericRoles;
import com.bbkj.domain.Member;
import com.bbkj.domain.Merchant;
import com.bbkj.domain.Openid;
import com.bbkj.service.GenericRolesService;
import com.bbkj.service.MemberService;
import com.bbkj.service.MerchantService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/wechat")@Slf4j
public class GetOpenidAction extends BaseAction {
    private String code;
    private Member member = new Member();

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GenericRolesService genericRolesService;
    public static long person_id;


    /**
     * 获取微信小程序的openid,并将openid保存到数据库，session_key保存到session
     *
     * @return
     * @throws IOException
     */
    @Action(value = "wx")
    public String execute() throws IOException {
        //获取到openid并录入数据库
        String appId = "wx646b7726a09a0aed";
        String appsecret = "c5645cb41a3822daa4885e8cdf932517";
        String params = "appid=" + appId + "&secret=" + appsecret +
                "&js_code=" + code + "&grant_type=authorization_code";
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession sessions = request.getSession();
        String openid = null;
        try {
            String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
            JSONObject jsonObject = JSONObject.fromObject(sr);
            openid = jsonObject.getString("openid");
            Openid openid1 = new Openid();
            openid1.setOpenid(openid);
            sessions.setAttribute("info", openid1);
            Member member = memberService.findByOpenid(openid);
            if (member != null) {
                person_id = member.getId();
                //同步过了
                if (StringUtils.isEmpty(member.getName())) {
                    //未绑定个人信息
                    //log.info("将信息保存在session");
                    //同步通用的id
                    response(openid, person_id, sessions.getId());
                    return null;
                }
                merchantService.findById(person_id);
                response(openid, person_id, sessions.getId(), member, true);
                return null;
            }
            //未同步
            Member member1 = new Member();
            GenericRoles genericRoles = new GenericRoles();
            genericRolesService.save(genericRoles);
            person_id = genericRoles.getId();
            member1.setOpenid(openid);
            member1.setId(person_id);
            memberService.save(member1);
            log.info(sessions.getId());
            response(openid, person_id, sessions.getId());
        } catch (ObjectNotFoundException e) {
            //未注册商户
            response(openid, person_id, sessions.getId(), member, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            responses(204, "获取openid失败,空指针异常");
        } catch (Exception e) {
            e.printStackTrace();
            responses(204, "获取openid失败,空指针异常");
        }
        return null;
    }

    public void response(String openid, Long person_id, String sessionsId, Member member, boolean isMerchant) throws IOException {
        Map data = new HashMap();
        data.put("openid", openid);
        data.put("person_id", person_id);
        data.put("session_id", sessionsId);
        data.put("user", member);
        if (isMerchant) {
            data.put("isMerchant", true);
        }
        JSONObject jsonObject = JSONObject.fromObject(data);
        writeJson(jsonObject);
    }

    public void response(String openid, Long person_id, String sessionsId) throws IOException {
        Map data = new HashMap();
        data.put("openid", openid);
        data.put("person_id", person_id);
        data.put("session_id", sessionsId);
        JSONObject jsonObject = JSONObject.fromObject(data);
        writeJson(jsonObject);
    }
}
