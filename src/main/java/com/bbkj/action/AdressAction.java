package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Address;
import com.bbkj.domain.Member;
import com.bbkj.service.AdressService;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * @author JJ
 * @version 1.0
 * @description: 地址管理接口
 * @date 2020/9/11 16:42
 */
@Getter
@Setter
@Controller
@ParentPackage("post")
@Namespace("/adress")
public class AdressAction extends BaseAction {
    private long person_id;
    private int postal_code;
    private long tel_number;
    private int checks;
    private String user_name;
    private String province_name;
    private String city_name;
    private String county_name;
    private String detailed_adress;
    @Autowired
    private AdressService adressService;
    private List list = new ArrayList();
    private Address adress = new Address();
    private long id;
    private Page page = new Page();
    private int whichpage;
    private int pagesize;


    /**
     * @description: 创建一个新的地址对象
     * @param: * @param
     * @return: com.beped.member.domain.Adress
     * @author JJ
     * @date: 2020/9/11 16:46
     */
    public Address createAdress() {
        Address adress = new Address();
        adress.setChecks(checks);
        adress.setPerson_id(person_id);
        adress.setPostal_code(postal_code);
        adress.setTel_number(tel_number);
        adress.setUser_name(user_name);
        adress.setProvince_name(province_name);
        adress.setCity_name(city_name);
        adress.setCounty_name(county_name);
        adress.setDetailed_adress(detailed_adress);
        return adress;
    }


    /**
     * @description: 新增一个地址
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/12 14:30
     */
    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            checkChecks();
            Address adress = createAdress();
            adressService.save(adress);
            notifySuccess("add a new address success");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @description: 修改地址，只能有一个是默认地址
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/12 11:47
     */
    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        try {
            checkChecks();
            Address adress = createAdress();
            adress.setId(id);
            adressService.update(adress);
            notifySuccess("true");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //查找地址接口,返回全部
    @Action("getAll")
    public String getAll() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Address.class)
                    .addOrder(Order.desc("id"));
            list = adressService.list(criteria);
            Map data = new HashMap();
            data.put("data", list);
            JSONObject datas = JSONObject.fromObject(data);
            writeJson(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @description: 按personId找到个人对应的地址
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 17:35
     */
    @Action("pagelist")
    public String pagePerson() throws Exception {
        try {
            if (person_id != 0) {
                Page page = adressService.findAllAdressByPersonId(person_id, whichpage, pagesize);
                pageList(page);
                return null;
            }
            notifyError("person_id获取错误！");
        } catch (NullPointerException e) {
            e.printStackTrace();
            responses(301, "空指针异常了");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 检查地址对象的默认字段
     * @param: * @param
     * @return: void
     * @author JJ
     * @date: 2020/9/12 11:53
     */

    public void checkChecks() {
        try {
            if (checks == 1) {
                List<Address> adresses = adressService.findAllDefaultAdress(person_id);
                for (Address adress1 : adresses) {
                    adress1.setChecks(0);
                    adressService.update(adress1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //按id找到实体,再通过实体删除表记录
    @Action("newDelete")
    public String newDelete() throws Exception {
        try {
            List<Address> addresses = adressService.
                    list(DetachedCriteria.forClass(Address.class).
                            add(Restrictions.eq("id", id)));
            if (addresses.size() > 0) {
                adressService.remove(addresses.get(0));
            }
            notifySuccess("true");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    //单个查找
    @Action("find")
    public String find() throws Exception {
        try {
            if (id != 0) {
                adress = adressService.findAdressById(id);
            }
            Map s = new HashMap();
            s.put("data", adress.toString());
            JSONObject data = JSONObject.fromObject(s);
            writeJson(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //加入分页器
    //whichpage取哪一页的数据
    //pagesze设置每一页的数量
    @Action("list")
    public String list() throws Exception {
        try {
            page = adressService.pagelist(whichpage, pagesize);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 查找默认地址，没有则给最新的。
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/11/12 14:58
     */
    @Action("findDefaultAddress")
    public String findDefaultAddress() throws IOException {
        try {
            Address adress = adressService.findDefaultAdress(person_id);
            if (adress != null) {
                returnData(200, "success to find", adress);
                return null;
            }
            responses(206, "您没有添加地址");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
