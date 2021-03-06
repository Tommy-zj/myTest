package com.bbkj.common;


import com.bbkj.common.utils.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAction extends ActionSupport {
    public String getUploadPath() {
        String realPath = getRealPath(SystemGlobal.UPLOAD_PATH);
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return SystemGlobal.UPLOAD_PATH;
    }

    protected String getRealPath(String url) {
        return getServletContext().getRealPath(url);
    }

    protected void VueNotifySuccess(String msg, String user) throws IOException {
        Map map = new HashMap();
        map.put("success", true);
        map.put("code", 200);
        map.put("data", new String[]{msg});
        map.put("user", new String(user));

        JSONObject object = JSONObject.fromObject(map);
        writeJson(object);
    }

    protected void notifySuccess(String msg) throws IOException {
        Map map = new HashMap();
        map.put("success", true);
        map.put("code", 200);
        map.put("data", new String[]{msg});

        JSONObject object = JSONObject.fromObject(map);
        writeJson(object);
    }

    public static void response(int code, String msg) throws IOException {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        JSONObject jsonObject = JSONObject.fromObject(map);
        writeJson(jsonObject);
    }

    protected void notifyError(String msg) throws IOException {
        Map map = new HashMap();
        map.put("success", false);
        map.put("code", 501);
        map.put("data", new String[]{msg});

        JSONObject object = JSONObject.fromObject(map);
        writeJson(object);
    }

    protected void write(String content) throws IOException {
        getResponse().setCharacterEncoding("utf-8");
        getResponse().setContentType("text/xml");
        getResponse().getWriter().write(content);
    }

    protected static void writeJson(JSON json) throws IOException {
        getResponse().setCharacterEncoding("utf-8");
        getResponse().setContentType("text/html");
        getResponse().getWriter().write(json.toString());
    }

    protected void writeXml(Document doc) throws IOException {
        getResponse().setCharacterEncoding("utf-8");
        getResponse().setContentType("text/xml");
        getResponse().getWriter().write(doc.asXML());
    }

    public static void responses(int code, String msg, Object o) throws IOException {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        map.put("user", o);
        JSONObject jsonObject = JSONObject.fromObject(map);
        writeJson(jsonObject);
    }

    public static void returnData(int code, String msg, Object o) throws IOException {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        map.put("data", o);
        JSONObject jsonObject = JSONObject.fromObject(map);
        writeJson(jsonObject);
    }


    public static void responses(int code, String msg) throws IOException {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        JSONObject jsonObject = JSONObject.fromObject(map);
        writeJson(jsonObject);
    }

    /**
     * @param totalNum ??????
     * @param pageNo   ?????????
     * @param result   ??????
     * @description: ??????????????????????????????
     * @param: * @param totalPage ?????????
     * @return: void
     * @author JJ
     * @date: 2020/10/23 14:31
     */
    public static void pageList(int totalPage, int totalNum, int pageNo, List<Object> result) throws IOException {
        Map data = new HashMap();
        data.put("code", 200);
        data.put("total_pages", totalPage);
        data.put("count_number", totalNum);
        data.put("now_page", pageNo);
        data.put("data", result);
        JSONObject s = JSONObject.fromObject(data);
        writeJson(s);
    }

    public static void pageList(Page page) throws IOException {
        Map data = new HashMap();
        data.put("code", 200);
        data.put("total_pages", page.getTotalPage());
        data.put("count_number", page.getTotalNum());
        data.put("now_page", page.getPageNo());
        data.put("data", page.getPageList());
        JSONObject s = JSONObject.fromObject(data);
        writeJson(s);
    }

    public static void pageList(int totalPage, List<Object> result) throws IOException {
        Map data = new HashMap();
        data.put("code", 200);
        data.put("total_pages", totalPage);
        data.put("data", result);
        JSONObject s = JSONObject.fromObject(data);
        writeJson(s);
    }

    /**
     * @description: ??????????????????
     * @author JJ
     * @date 2021/2/24 15:31
     * @version 1.0
     */
    public static void pageListTwo(int shopTotalNum, int requestTotalNum, List<Object> shopList, List<Object> requestList) throws IOException {
        Map data = new HashMap();
        data.put("shop_count_number", shopTotalNum);
        data.put("request_count_number", requestTotalNum);
        data.put("shop_data", shopList);
        data.put("request_data", requestList);
        JSONObject s = JSONObject.fromObject(data);
        writeJson(s);
    }


    /**
     * ?????????????????????
     *
     * @param param ????????????
     * @return ??????????????????
     */
    public String getParameter(String param) {
        String[] values = (String[]) getParameters().get(param);
        return (values == null ? null : values[0]);
    }

    /**
     * ????????????????????????Map
     *
     * @return ????????????????????????Map
     */
    public Map getParameters() {
        return ActionContext.getContext().getParameters();
    }

    /**
     * ??????????????????
     *
     * @return ????????????Map??????
     */
    public Map getSession() {
        return ActionContext.getContext().getSession();
    }

    public ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    /**
     * ??????????????????
     *
     * @return Http????????????
     */
    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }
}