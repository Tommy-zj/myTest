package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.Page;
import com.bbkj.common.utils.Util;
import com.bbkj.domain.Catalog;
import com.bbkj.domain.Image;
import com.bbkj.domain.Product;
import com.bbkj.domain.ProductExtra;
import com.bbkj.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;


/**
 * @author JJ
 * @version 1.0
 * @description: 商品模块
 * @date 2020/9/14 11:09
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/product")
@Slf4j
public class ProductAction extends BaseAction {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private ProductExtraService productExtraService;
    @Autowired
    private DemandService demandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImgService imgService;
    private Product product = new Product();
    private ProductExtra productExtra = new ProductExtra();
    private Image newImage = new Image();
    private List list = new ArrayList();
    private long id;
    private long[] ids;
    private boolean disable;
    private Long second_catalog_id = 0L;
    private Long first_catalog_id = 0L;
    private String name;
    private String shopName;
    private double price;
    private int count;
    private String numbering;
    private String weight;
    private String province_name;
    private String city_name;
    private String county_name;
    private long phone;
    private Long product_id;
    private String local;
    private String[] imgs;
    private Image[] shopImgs; //图片对象数组
    //前端传来的多个img
    private Page page = new Page();
    private int whichpage;
    //分页器，定位到哪一页
    private int pagesize;
    // 设置一页有多少项
    private String img;
    //删除的图片
    private long extra_id;
    private String detail;
    private long person_id;
    private String remark;
    private int status;

    public Product setShop() {
        product.setSecond_catalog_id(second_catalog_id);
        product.setName(name);
        product.setPrice(price);
        product.setPhone(phone);
        product.setNumbering(numbering);
        product.setFirst_catalog_id(first_catalog_id);
        product.setDisable(disable);
        product.setCreate_time(new Date().getTime());
        //将第一张图片当作展示的那张图片
        if (imgs != null && imgs.length > 0) {
            product.setCover_img(imgs[0]);
        }
        // 兼容穿过来的数组是图片对象和字符串
        if (shopImgs != null && shopImgs.length > 0) {
            product.setCover_img(shopImgs[0].getImage_url());
        }
        return product;
    }

    public ProductExtra setProductExtra() {
        if (local != null) {
            String[] locals = local.split(" ");
            log.info("分割地址");
            log.info(locals.toString());
            if (locals.length == 3) {
                province_name = locals[0];
                city_name = locals[1];
                county_name = locals[2];
            }
            productExtra.setCount(count);
            productExtra.setProvince_name(province_name);
            productExtra.setCounty_name(county_name);
            productExtra.setCity_name(city_name);
            productExtra.setDetail(detail);
        }
        return productExtra;
    }


    /**
     * @description: TODO 管理员或普通用户添加商品
     * @author JJ
     * @date 2022/1/8 15:00
     * @version 1.0
     */
    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws IOException {
        try {
            if (name != null && phone != 0 && price != 0) {
                //保存商品主要信息
                product = setShop();
                product.setPerson_id(person_id);
                // 如果不是管理添加的商品，就需要管理员审核
                // 判断是不是管理员
                Boolean admin = userService.admin(person_id);
                if (admin) {
                    product.setStatus(1);
                } else {
                    product.setStatus(0);
                }
                productService.save(product);
                //保存商品扩展信息
                productExtra = setProductExtra();
                productExtra.setProduct_id(product.getId());
                productExtraService.save(productExtra);
                //保存商品图片
                if (imgs != null) {
                    for (String img : imgs) {
                        Image newImgs = new Image();
                        newImgs.setProduct_id(product.getId());
                        newImgs.setImage_url(img);
                        imgService.save(newImgs);
                    }
                }
                if (shopImgs != null) {
                    for (Image img : shopImgs) {
                        Image newImgs = new Image();
                        newImgs.setProduct_id(product.getId());
                        newImgs.setImage_url(img.getImage_url());
                        imgService.save(newImgs);
                    }
                }
                notifySuccess("success");
            } else {
                log.info("此次是空请求");
                responses(301, "此次是空请求");
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifyError("error" + e.getMessage());
        }
        return null;
    }

    @Action("delete")
    public String delete() throws Exception {
        try {
            //删除商品额外信息
            List<ProductExtra> productExtras = productExtraService.findByProductIdAll(id);
            if (productExtras != null) {
                for (ProductExtra index : productExtras) {
                    productExtraService.remove(index);
                }
            }
            //删除商品基本信息
            productService.remove(productService.findById(id));

            //删除图片
            List<Image> images = imgService.findByProductId(id);
            for (Image image : images) {
                imgService.remove(image);
            }
            notifySuccess("delete success");
        } catch (Exception e) {
            e.printStackTrace();
            notifyError("Error" + e.getMessage());
        }
        return null;
    }

    @Action(value = "deletes", interceptorRefs = @InterceptorRef("json"))
    public String deletes() throws Exception {
        //删除主要信息
        try {
            if (ids.length > 0) {
                productService.remove(ids);
            }
            //删除额外信息
            for (Long id : ids) {
                //删除商品额外信息
                List<ProductExtra> productExtras = productExtraService.findByProductIdAll(id);
                if (productExtras.size() > 0) {
                    for (ProductExtra productExtra : productExtras) {
                        productExtraService.remove(productExtra);
                    }
                }
                //删除图片信息
                List<Image> images = imgService.findByProductId(id);
                if (images.size() > 0) {
                    for (Image image : images) {
                        imgService.remove(image);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responses(300, "程序内部错误", e.getMessage());
        }
        return null;
    }


    /**
     * @description: 修改商品的信息，包括图片
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:06
     */
    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        try {
            if (imgs != null) {
                //修改图片
                List<Image> image2 = imgService.findByProductId(id);
                if (image2 != null) {
                    for (Image image1 : image2) {
                        imgService.remove(image1);
                    }
                }
                for (String img : imgs) {
                    Image image1 = new Image();
                    image1.setProduct_id(id);
                    image1.setImage_url(img);
                    imgService.save(image1);
                }
            }
            //修改主要信息
            product = setShop();
            product.setId(id);
            productService.update(product);
            //修改额外信息
            try {
                setProductExtra();
                productExtra.setProduct_id(id);
                productExtra.setId(extra_id);
                productExtraService.update(productExtra);
                notifySuccess("success");
            } catch (Exception e) {
                e.printStackTrace();
                //找不到就重新建立一个新的商品拓展表
                setProductExtra();
                productExtra.setProduct_id(id);
                productExtra.setId(extra_id);
                productExtraService.save(productExtra);
                notifySuccess("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            notifyError("error" + e.getMessage());
        }
        return null;
    }


    /**
     * @description: 按分页器获取各个分类的商品
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:04
     */
    @Action("pagelist")
    public String pages() throws Exception {
        try {
            //page为取出的那页数据
            page = productService.findByCatalog(second_catalog_id, first_catalog_id, whichpage, pagesize);
            pageList(page);
        } catch (Exception e) {
            e.printStackTrace();
            responses(200, "error", e.getMessage());
        }

        return null;
    }

    /**
     * @description: 获取单个商品拓展信息信息接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/15 17:50
     */
    @Action("productExtraAdmin")
    public String getOneShopAdmin() throws Exception {
        try {
            Map map = findExtraAndImg();
            returnData(200, "success to find", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 微信小程按分页获取商品信息
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/12/19 11:46
     */
    @Action("WxPageList")
    public String WxPages() throws Exception {
        try {
            page = productService.findByCatalogWx(second_catalog_id, whichpage, pagesize);
            pageList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description
     * @Date 2021/12/4 15:04 按shopId查找商品全部信息
     **/
    @Action("getShopByShopId")
    public String getShopByShopId() throws IOException {
        try {
            // Product product = productService.findById(id);
            List<Product> products = productService.
                    list(DetachedCriteria.forClass(Product.class).
                            add(Restrictions.eq("id", id)));
            if (products.size() > 0) {
                Product product = products.get(0);
                Map map = findExtraAndImg();
                Map productMap = new HashMap();
                Util.beanToMap(product, productMap);
                map.putAll(productMap);
                product.setBrowsing_history(product.getBrowsing_history() + 1);
                productService.update(product);
                responses(200, "true", map);
                return null;
            }
            responses(300, "此shopId没有对应的商品");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 获取单个商品拓展信息信息接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/15 17:50
     */
    @Action("productExtra")
    public String getOneShop() throws Exception {
        try {
            //历史浏览加一
            Map map = findExtraAndImg();
            Product product = productService.findById(id);
            product.setBrowsing_history(product.getBrowsing_history() + 1);
            productService.update(product);
            returnData(200, "success to find", map);
        } catch (Exception e) {
            response(300, "此商品没有拓展信息");
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @author JJ
     * @Description 找到对应商品的附加信息和图片
     * @Date 2021/4/13 12:36
     * @Param []
     * @Return java.util.Map
     **/
    public Map findExtraAndImg() {
        ProductExtra productExtra = productExtraService.findByProductId(id);
        List<Image> images = imgService.findByProductId(id);
        List resultImage = new ArrayList();
        Map map = new HashMap();
        if (images != null) {
            for (Image image : images) {
                resultImage.add(image.getImage_url());
            }
        }
        map.put("imgs", resultImage);
        if (productExtra != null) {
            map.put("count", productExtra.getCount());
            map.put("province_name", productExtra.getProvince_name());
            map.put("city_name", productExtra.getCity_name());
            map.put("county_name", productExtra.getCounty_name());
            map.put("detail", productExtra.getDetail());
            map.put("extra_id", productExtra.getId());
        }
        return map;
    }

    /**
     * @description: 挑选出推荐商品
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:03
     */
    @Action("recommend")
    public String pagesRecommend() throws Exception {
        //page为取出的那页数据
        try {
            page = productService.findByRecommend(1, whichpage, pagesize);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 获取全部未推荐商品接口
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 11:41
     */
    @Action("shopNotRecommend")
    public String pagesNotRecommend() throws Exception {
        //page为取出的那页数据
        page = productService.findByRecommend(0, whichpage, pagesize);
        List source = page.getPageList();
        pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), source);
        return null;
    }

    /**
     * @description: 删除推荐商品，把推荐id从1变成0
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:41
     */
    @Action(value = "deleteRecommendShop", interceptorRefs = @InterceptorRef("json"))
    public String deleteRecommend() throws Exception {
        try {
            if (ids.length > 0) {
                for (Long id : ids) {
                    product = productService.findById(id);
                    product.setRecommend(0);
                    productService.update(product);
                    notifySuccess("true");
                }
            }

        } catch (Exception e) {
            notifyError("false");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @description: 增加推荐商品，多选情况下
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:47
     */
    @Action(value = "addRecommend", interceptorRefs = @InterceptorRef("json"))
    public String addRecommend() throws Exception {
        if (ids.length > 0) {
            try {
                for (Long id : ids) {
                    product = productService.findById(id);
                    product.setRecommend(1);
                    productService.update(product);
                }
                notifySuccess("success");
            } catch (Exception e) {
                notifyError("error");
                e.printStackTrace();
            }
        } else {
            notifyError("ids is null");
        }
        return null;
    }

    /**
     * @description: 删除商品对应的图片
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/14 10:05
     */
    @Action(value = "deleteImage", interceptorRefs = @InterceptorRef("json"))
    public String delShowImg() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Image.class).add(Restrictions.eq("img", img));
            List<Image> deleteImage = imgService.list(criteria);
            for (Image item : deleteImage) {
                //删除
                imgService.remove(item);
            }
            notifySuccess("删除成功！" + img);
        } catch (Exception e) {
            notifyError("删除失败！");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author JJ
     * @Description 按名字查找商品
     * @Date 2021/3/17 18:04
     * @Param [shopName]
     * @Return java.lang.StringfindReviewProduct
     **/
    @Action("findShopByName")
    public String shopFindByName() throws Exception {
        try {
            page = productService.findByName(shopName, whichpage, pagesize);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        } catch (Exception e) {
            responses(300, "error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 按关键字查询商品和需求
     * @author JJ
     * @date 2021/2/24 15:27
     * @version 1.0
     */
    @Action("findShopAndRequest")
    public String findShopAndRequest() throws Exception {
        try {
            page = productService.findByName(shopName, whichpage, pagesize);
            Page page1 = demandService.findByContent(shopName, whichpage, pagesize);
            pageListTwo(page.getTotalNum(), page1.getTotalNum(), page.getPageList(), page1.getPageList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @return java.lang.String
     * @author JJ
     * @Description 获取个人的商品信息
     * @Date 2022/1/8 16:48
     **/
    @Action("findPersonProduct")
    public String findPersonProduct() throws Exception {
        try {
            Page page = productService.findPersonProduct(person_id, whichpage, pagesize, status);
            pageList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 找到未审核商品接口
     * @Date 2021/4/28 16:11
     **/
    @Action("findReviewProduct")
    public String findReviewProduct() {
        try {
            Page page = productService.findReviewProduct(whichpage, pagesize);
            List<Product> products = page.getPageList();
            List result = new ArrayList();
            for (Product product : products) {
                long secondSortId = product.getSecond_catalog_id();
                long firstSortId = product.getFirst_catalog_id();
                Catalog first_catalog = catalogService.findById(firstSortId);
                Catalog second_catalog = catalogService.findById(secondSortId);
                Hibernate.initialize(second_catalog);
                Map map = new HashMap();
                map.put("id", product.getId());
                map.put("phone", product.getPhone());
                map.put("price", product.getPrice());
                map.put("second_catalog", second_catalog.getCatalog_name());
                map.put("first_catalog", first_catalog.getCatalog_name());
                map.put("name", product.getName());
                map.put("numbering", product.getNumbering());
                map.put("cover_img", product.getCover_img());
                map.put("person_id", product.getPerson_id());
                result.add(map);
            }
            page.setPageList(result);
            pageList(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 审核商品
     * @Date 2021/12/1 9:25
     **/
    @Action(value = "passReview", interceptorRefs = {@InterceptorRef("json")})
    public String PassReview() throws Exception {
        try {
            Product product = productService.findById(id);
            product.setStatus(status);
            if (status == 2) {
                product.setRemark(remark);
            }
            productService.update(product);
            responses(200, "审核成功！");
        } catch (ObjectNotFoundException e) {
            responses(201, "审核的商品找不到了");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
