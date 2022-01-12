package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.domain.Merchant;
import com.bbkj.service.MerchantService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/4/22 11:27
 */
@Controller
@ParentPackage("post")
@Namespace("/merchant")
@Setter
@Getter@Slf4j
public class MerchantAction extends BaseAction {
    @Autowired
    private MerchantService merchantService;
    private long person_id;
    private String mail;
    private long phone;
    private String password;
    private Merchant merchant = new Merchant();

    public Merchant setMerchant() {
        merchant.setMail(mail);
        merchant.setPhone(phone);
        merchant.setPassword(password);
        merchant.setPerson_id(person_id);
        return merchant;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 新增商户
     * @Date 2021/5/7 9:19
     **/
    @Action(value = "add", interceptorRefs = {@InterceptorRef("json")})
    public String add() throws Exception {
        try {
            if (person_id != 0l && password != null && phone != 0) {
                merchant = merchantService.findById(person_id);
                responses(200, "有记录了");
                return null;
            }
            responses(301, "必要参数为空！");
        } catch (ObjectNotFoundException e) {
            //没有相同的行
            Merchant merchant = setMerchant();
            merchantService.save(merchant);
            responses(200, "add success!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }


}
