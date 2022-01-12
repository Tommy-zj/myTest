package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Catalog;
import com.bbkj.service.CatalogService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 商品分类
 * @date 2021/3/11 20:42
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/sort")
@Slf4j
public class CatalogAction extends BaseAction {
    private String catalog_name;
    private long paren_id;
    private long[] parens;
    private String icon;
    private String image_name;
    private int image_size;
    private Catalog catalog = new Catalog();
    @Autowired
    private CatalogService catalogService;
    private long[] ids;
    private long id;
    private int pageSize;
    private int pageNo;

    public void setSort() {
        catalog.setIcon(icon);
        catalog.setCatalog_name(catalog_name);
        catalog.setParen_id(paren_id);
        catalog.setImage_name(image_name);
        catalog.setImage_size(image_size);
    }

    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        try {
            setSort();
            catalogService.save(catalog);
            responses(200, "新的分类添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Action(value = "delete", interceptorRefs = @InterceptorRef("json"))
    public String delete() throws Exception {
        try {
            if (ids.length > 0) {
                catalogService.remove(ids);
                responses(200, "删除成功！");
                return null;
            }
            //删除对应的子分类
            if (parens.length > 0) {
                for (long id : parens) {
                    List<Catalog> catalogs = catalogService.findChild(id);
                    for (Catalog catalog : catalogs) {
                        catalogService.remove(catalog);
                    }
                }
            }
            responses(201, "ids为空");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        try {
            if (id != 0l) {
                setSort();
                catalog.setId(id);
                catalogService.update(catalog);
                responses(200, "修改成功！");
                return null;
            }
            responses(201, "缺少主键");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @description: TODO 获取分类的接口，分两种情况，带参数和不带参数
     * @author JJ
     * @date 2021/3/12 16:40
     * @version 1.0
     */
    @Action("list")
    public String list() throws Exception {
        try {
            List result = new ArrayList();
            if (pageSize > 0) {
                //带分页器
                Page page = catalogService.findFather(pageNo, pageSize);
                List<Catalog> catalogs = page.getPageList();
                setSort(catalogs, result);
                pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);
                return null;
            }
            //不带分页器,返回全部
            List<Catalog> catalogs = catalogService.findFather();
            setSort(catalogs, result);
            returnData(200, "success", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //包装分类,sorts1是二级分类，sort是操作的一级分类
    public List setSort(List<Catalog> catalogs, List result) {
        for (Catalog item : catalogs) {
            List<Catalog> sorts1 = catalogService.findChild(item.getId());
            Map map = new HashMap();
            map.put("catalogs", sorts1);
            map.put("icon", item.getIcon());
            map.put("catalog_name", item.getCatalog_name());
            map.put("paren_id", item.getParen_id());
            map.put("id", item.getId());
            result.add(map);
        }
        return result;
    }

    /**
     * @description: TODO 只返回id和类名，节约带宽
     * @author JJ
     * @date 2021/3/12 15:34
     * @version 1.0
     */
    @Action("listOnlyFont")
    public String listOnlyFont() throws Exception {
        try {
            List<Catalog> catalogs = catalogService.findFather();
            List result = new ArrayList();
            for (Catalog catalog : catalogs) {
                Map map = new HashMap();
                map.put("catalog_name", catalog.getCatalog_name());
                map.put("id", catalog.getId());

                List<Catalog> sorts1 = catalogService.findChild(catalog.getId());
                List secondSorts = new ArrayList();
                for (Catalog catalog1 : sorts1) {
                    Map secondMap = new HashMap();
                    secondMap.put("catalog_name", catalog1.getCatalog_name());
                    secondMap.put("id", catalog1.getId());
                    secondSorts.add(secondMap);
                }
                map.put("catalogs", secondSorts);
                result.add(map);
            }
            returnData(200, "success", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Action("listFirstSortOnlyFont")
    public String listFirstSortOnlyFont() throws Exception {
        try {
            returnData(200, "success to find", catalogService.findFather());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
