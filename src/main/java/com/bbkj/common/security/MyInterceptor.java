package com.bbkj.common.security;

import com.alibaba.fastjson.JSON;
import com.bbkj.common.BaseAction;
import com.bbkj.common.redis.RedisUtil;
import com.bbkj.domain.Openid;
import com.bbkj.domain.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/29 17:32
 */
@Setter
@Getter
@Slf4j

public class MyInterceptor extends BaseAction implements Interceptor {

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Override
    public void destroy() {
        log.info("----destroy()----");
    }

    @Override
    public void init() {
        log.info("-----Init()-------");
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        log.info("----验证中------");
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String role = request.getHeader("role");
        String personId = request.getHeader("personId");
        try {//后台接口验证
            //String test = request.getHeader("user");
            if (role.equals("pc")) {
                //session里保存的用户信息
                Map sessionUser = (Map) session.getAttribute("info");
                log.info("pc端用户查看头部携带信息和session保存新消息");
                log.info(request.getHeader("user"));
                // log.info("session保存的消息");
                // log.info(sessionUser);
                JSONObject jsonObject = JSONObject.fromObject(request.getHeader("user"));
                //请求头的用户信息
                User user = (User) JSONObject.toBean(jsonObject, User.class);
                // log.info("查看请求头的信息");
                // log.info(user);
                // log.info(GetLocalTime.localTime());
                // log.info(user.getPassword());
                // log.info("查看session保存的密码");
                // log.info(sessionUser.getPassword());
                /**
                 log.info(sessionUser.getPassword());
                 log.info(user.getName());
                 log.info(sessionUser.getName());
                 log.info("验证时的sessionId");
                 log.info(session.getId());
                 */
                if (sessionUser != null && user.getPassword().equals(sessionUser.get("password")) && user.getName().equals(sessionUser.get("name"))) {
                    log.info("验证成功");
                    //log.info(session.getAttribute("info"));
                    return actionInvocation.invoke();
                }
                log.info("验证失败");
                response(403, "你不是本系统授权的用户");
                return null;
            }
            if (role.equals("phone")) {
                log.info("手机端用户接口鉴权");
                if (personId != null) {
                    // 使用redis缓存个人信息
                    String info = String.valueOf(redisUtil.get(personId));
                    if (info != null && !info.equals(null) && info != "null" && info.length() > 0) {
                        return actionInvocation.invoke();
                    }
                    response(403, "你不是本系统授权的用户");
                    return null;
                } else {
                    response(417, "没有验证信息");
                    return null;
                }
            }
            // 小程序端跳过接口鉴权
            if (role.equals("wechat")) {
                log.info("手机端用户登录");
                return actionInvocation.invoke();
            }
            // log.info("微信小程序的验证");
            Openid wxSessionUser = (Openid) session.getAttribute("info");
            // session里保存的用户信息
            // log.info("session里面保存的值");
            // log.info(wxSessionUser);
            //获取微信小程序请头信息
            JSONObject jsonObject = JSONObject.fromObject(request.getHeader("user"));
            Openid wxUser = (Openid) JSONObject.toBean(jsonObject, Openid.class); //请求头的用户信息
            // log.info("微信小程序请求头部信息");
            // log.info(wxUser);
            //log.info(GetLocalTime.localTime());
            if (wxSessionUser != null && wxUser.getOpenid().equals(wxSessionUser.getOpenid())) {
                // log.info("验证成功");
                //log.info(session.getAttribute("info"));
                return actionInvocation.invoke();
            }
            log.info("验证失败，非本系统用户！");
            response(403, "你不是本系统授权的用户");
        } catch (Exception e) {
            log.info("没有验证信息");
            e.printStackTrace();
            response(417, "没有验证信息");
        }
        return null;
    }
}
