package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbParam;
import com.yufan.utils.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/4
 */
@Controller
@RequestMapping(value = "/file/")
public class ImgUploadController {

    private Logger LOG = Logger.getLogger(ImgUploadController.class);


    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @param from     图片来源 用于控制不同图片大小和尺寸
     *                 闲菜：from=xiancai
     * @param imgType  图片类型 用于控制不同图片大小和尺寸
     *                 闲菜：xiancaiGoodsImg(闲菜商品主图)  xiancaiGoodsInfoImg(闲菜商品介绍)
     * @param file     上传的文件名称
     */
    @RequestMapping("uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response, String from, String imgType, MultipartFile file) {
        LOG.info("------文件上传------");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = new JSONObject();
            out.put("from", from);
            out.put("imgType", imgType);
            out.put("url", "");
            out.put("imgPath", "");
            out.put("status", "failed");////uploading 表示上传中，failed 表示上传失败，done 表示上传完成
            if (null == file) {
                LOG.info("--------------响应结果" + out);
                writer.print(out);
                writer.close();
                return;
            }

            String path = "";
            //img_save_local

            boolean flagLocal = false;
            List<TbParam> list = CacheData.PARAMLIST;
            for (int i = 0; i < list.size(); i++) {
                TbParam param = list.get(i);
                String code = param.getParamCode();
                String key = param.getParamKey();
                String value = param.getParamValue();
                if (!"sys_code".equals(code)) {
                    continue;
                }
                if ("img_save_local".equals(key) && "1".equals(value)) {
                    flagLocal = true;
                    break;
                }
            }

            if (flagLocal) {
                //保存到本地
                String root = Constants.IMG_SAVE_ROOT_PATH;//本地根目录
                LOG.info("----保存本地路径----" + path);
                // 文件按 年/月/日    目录保存
                String savePath = DatetimeUtil.getNow("yyyy/MM/dd");
                String filename = DatetimeUtil.getNow("yyyyMMdd") + System.currentTimeMillis() + ".jpg";
                String localSavePath = root + "\\" + savePath;//本地完整路径
                localSavePath = localSavePath.replace("\\", "/").replace("\\\\", "/").replace("//", "/");
                LOG.info("----localSavePath---->" + localSavePath);
                LOG.info("----filename---->" + filename);
                boolean flag = ImageUtil.getInstance().saveFile(file.getInputStream(), localSavePath, filename);//保存到本地
                if (flag) {
                    path = savePath + "/" + filename;
                }
            }

            if (!StringUtils.isEmpty(path)) {
                path = path.replace("\\", "/");
                out.put("url", Constants.IMG_WEB_URL + "" + path);//图片访问地址
                out.put("imgPath", path);
                out.put("status", "done");
                LOG.info("--------------响应结果" + out);
            }
            out.put("from", from);
            out.put("imgType", imgType);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
