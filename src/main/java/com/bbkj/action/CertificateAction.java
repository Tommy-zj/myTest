package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Certificate;
import com.bbkj.domain.Member;
import com.bbkj.service.CertificateService;
import com.bbkj.service.MemberService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 技能管理接口
 * @date 2020/9/11 10:47
 */
@Getter
@Setter
@Controller
@ParentPackage("post")
@Namespace("/certificate")
@Slf4j
public class CertificateAction extends BaseAction {
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private MemberService memberService;
    private String image_url;
    private String image_name;
    private int image_size;
    private long person_id;
    private String name;
    private long id;
    private int state;//审核证书的id
    private String remark;
    private int pageNo;
    private int pageSize;

    /**
     * @description: 构造技能对象函数
     * @param: * @param
     * @return: com.beped.mechanic.domain.Certificate
     * @author JJ
     * @date: 2020/9/11 10:48
     */
    public Certificate createCertificate() {
        Certificate certificate = new Certificate();
        certificate.setImage_url(image_url);
        certificate.setImage_name(image_name);
        certificate.setImage_size(image_size);
        certificate.setName(name);
        certificate.setPerson_id(person_id);
        return certificate;
    }

    /**
     * @description: 新增技能接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 10:48
     */
    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            certificateService.save(createCertificate());
            notifySuccess("success");
        } catch (Exception e) {
            notifyError("error");
        }
        return null;
    }

    /**
     * @description: 删除技能接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/11 10:48
     */
    @Action("delete")
    public String delete() throws Exception {
        Certificate certificate = certificateService.findById(id);
        certificateService.remove(certificate);
        notifySuccess("success");
        return null;
    }

    /**
     * @description: 审核证书接口
     * @param: * @param remark:不为空说明有不正常的图片信息，不通过,设置状态id为2，为空说明正常，通过
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/24 11:31
     */
    @Action(value = "reviewCertificate", interceptorRefs = @InterceptorRef("json"))
    public String editCertificate() throws Exception {
        if (id != 0) {
            if (remark != null) {
                Certificate certificate = certificateService.findById(id);
                certificate.setRemark(remark);
                certificate.setState(2);
                certificateService.update(certificate);
                responses(200, "证书不通过");
            } else {
                Certificate certificate = certificateService.findById(id);
                certificate.setState(1);
                certificateService.update(certificate);
                responses(200, "通过");
            }

        }
        return null;
    }

    /**
     * @description: 查找未被审核的证书，不包括被否定的证书
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/24 11:38
     */
    @Action("findNotReviewCertificate")
    public String findNotReviewCertificate() throws Exception {
        try {
            Page page = certificateService.findNotReview(pageNo, pageSize);
            //还需拼接申请人的姓名
            List<Certificate> certificates = page.getPageList();
            List result = new ArrayList();
            for (Certificate certificate : certificates) {
                Map certificateMap = CommonUtils.convertToMap(certificate);
                long person_id = certificate.getPerson_id();
                Member member = memberService.findById(person_id);
                Map personNameMap = new HashMap();
                personNameMap.put("person_name", member.getName());
                certificateMap.putAll(personNameMap);
                result.add(certificateMap);
            }
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("报错信息" + e.getMessage());
        }
        return null;
    }

    /**
     * @author JJ
     * @Description 按person_id找到符合的证书
     * @Date 2021/4/11 15:28
     * @Param []
     * @Return java.lang.String
     **/
    @Action("findCertificate")
    public String findCertificate() throws Exception {
        try {
            List result = certificateService.findByPerson(person_id);
            returnData(200, "success", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
