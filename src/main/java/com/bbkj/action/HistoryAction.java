package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.GetLocalTime;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.History;
import com.bbkj.service.HistoryService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: 浏览历史接口汇总
 * @date 2020/9/14 16:50
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/history")@Slf4j
public class HistoryAction extends BaseAction {
    @Autowired
    private HistoryService historyService;
    private History history = new History();
    private Page page = new Page();
    private long id;
    private long person_id;
    private long product_id;
    private int pageNo; //分页器，定位到哪一页
    private int pageSize; // 设置一页有多少项

    private String product_name;
    private double product_price;
    private String product_img;
    private long second_id;
    private long catalog_id;

    /**
     * @description: 创建一个新的历史记录对象
     * @param: * @param
     * @return: com.beped.member.domain.History
     * @author JJ
     * @date: 2020/9/14 17:08
     */
    public History createHistory() {
        history.setCtime(GetLocalTime.localTime());
        history.setPerson_id(person_id);
        history.setProduct_id(product_id);
        history.setProduct_img(product_img);
        history.setProduct_name(product_name);
        history.setProduct_price(product_price);
        history.setSecond_id(second_id);
        return history;
    }

    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            if (person_id > 0 && product_id > 0) {
                List<History> histories = historyService.find(person_id, product_id);
                if (histories != null) {
                    log.info("有相同的商品--更新时间和时间戳");
                    History history = histories.get(0);
                    history.setTimestamp(System.currentTimeMillis());
                    history.setCtime(GetLocalTime.localTime());
                    historyService.update(history);
                    responses(200, "浏览记录已经有了!");
                    return null;
                }
                log.info("没有相同的商品--添加商品");
                historyService.save(createHistory());
                responses(200, "第一次查看此商品!");
                return null;
            }
            responses(301, "信息不全，person_id或product_id为空");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 加入分页器的获取商品接口
     * @param: * @param whichpage:哪一页，pagesize:一页的数据量
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 17:05
     */
    @Action("pagelist")
    public String list() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(History.class)
                    .add((Restrictions.eq("person_id", person_id)))
                    .addOrder(Order.desc("id"));
            page = historyService.pageList(criteria, pageNo, pageSize);
            pageList(page.getTotalNum(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Action("delete")
    public String delete() throws Exception {
        try {
            historyService.remove(historyService.findAdressById(id));
            notifySuccess("success to delete!");
        } catch (Exception e) {
            notifyError("undefine error!");
        }
        return null;
    }


}
