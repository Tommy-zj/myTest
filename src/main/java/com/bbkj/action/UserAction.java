package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.GenericRoles;
import com.bbkj.domain.User;
import com.bbkj.service.GenericRolesService;
import com.bbkj.service.UserService;
import lombok.Getter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/login")
@Slf4j
public class UserAction extends BaseAction {
    private User user = new User();
    @Autowired
    private GenericRolesService genericRolesService;
    @Autowired
    private UserService userService;
    private String name;
    private String password;
    private String email;
    private String avatar;
    private int permissionId;
    private int whichpage;
    private int pagesize;
    private long id;
    private long[] ids;

    private User setUsers() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setName(name);
        return user;
    }


    /**
     * @description: TODO 注册账号
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/10/12 9:07
     */
    @Action(value = "register", interceptorRefs = @InterceptorRef("json"))
    public String register() throws Exception {
        try {
            Number users = userService.findByNames(name);
            if (users.equals(0l)) {
                GenericRoles genericRoles = new GenericRoles();
                genericRolesService.save(genericRoles);
                long id = genericRoles.getId();
                //将基础表的主键id作为新表的person_id;
                User user = setUsers();
                user.setId(id);
                userService.save(user);
                response(200, "注册成功");
                return null;
            }
            //名字已经被注册
            response(201, "名字已被注册");
        } catch (Exception e) {
            e.printStackTrace();
            responses(500, "程序出错了！");
        }
        //要先判断名字是否被注册
        return null;
    }

    @Action(value = "/dit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        userService.update(setUsers());
        responses(200, "修改成功！");
        return null;
    }

    @Action("pagelist")
    public String list() throws Exception {
        try {
            Page page = userService.list(whichpage, pagesize);
            pageList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Action(value = "delete", interceptorRefs = @InterceptorRef("json"))
    public String delete() throws Exception {
        try {
            User user = userService.findAdressById(id);
            userService.remove(user);
            responses(200, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author JJ
     * @Description 批量删除
     * @Date 2021/4/13 17:17
     * @Param []
     * @Return java.lang.String
     **/
    @Action(value = "remove", interceptorRefs = @InterceptorRef("json"))
    public String remove() throws Exception {
        try {
            userService.remove(ids);
            responses(200, "success to remove");
        } catch (Exception e) {
            e.printStackTrace();
            responses(301, "error");
        }
        return null;
    }

}
