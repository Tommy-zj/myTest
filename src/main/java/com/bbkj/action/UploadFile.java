package com.bbkj.action;

import com.bbkj.common.BaseAction;
import com.bbkj.common.SystemGlobal;
import com.bbkj.common.utils.ImgUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/4/17 10:59
 */
@Controller
@ParentPackage("post")
@Namespace("/up")
@Setter
@Slf4j
@Getter
public class UploadFile extends BaseAction {
    private File file;
    private String destPath = SystemGlobal.imgUrl;
    private String host = SystemGlobal.domain_img;
    protected String imgUrls = "";
    private String fileContentType;
    private String fileFileName;

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 上传图片
     * @Date 2021/4/17 14:38
     **/
    @Action(value = "upload", interceptorRefs = {
            @InterceptorRef(value = "json"),
            @InterceptorRef(value = "basicStack"),
            @InterceptorRef(value = "fileUpload",
                    params = {"allowedTypes", "image/jpeg,image/gif,image/png", "maximumSize", "5242880"}),
            @InterceptorRef(value = "defaultStack")
    })
    public String upload() throws Exception {

        if (file != null) {
            try {
                int imageSize = (int) file.length();
                String random = RandomStringUtils.randomAlphanumeric(5);
                String name = System.currentTimeMillis() + "_" + imageSize + "_" + random + ".jpg";
                String mkdir = ImgUtils.createDir(destPath);
                log.info(mkdir);
                File destFile = new File(mkdir, name);
                FileUtils.copyFile(file, destFile);
                imgUrls = ImgUtils.time + "/" + name;
                Map map = new HashMap();
                map.put("code", 200);
                map.put("imgUrl", imgUrls);
                map.put("fileRealName", this.fileFileName);
                map.put("imageSize", imageSize);
                JSONObject jsonObject = JSONObject.fromObject(map);
                writeJson(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                notifyError("添加失败");
            }
            return null;
        }
        log.info("--所需参数为空");
        return null;
    }
}
