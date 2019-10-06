package com.yufan.task.service.impl;

import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.addr.IAddrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/6 19:28
 * 功能介绍: ydui.citys.js  生成收货地址静态文件(前端)
 */
@Service("ydui_citys ")
public class CreateCity implements IResultOut {


    @Autowired
    private IAddrDao iAddrDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
        //保存数据
        List<Map<String, Object>> list1 = new ArrayList<>();//省
        List<Map<String, Object>> list2 = new ArrayList<>();//市
        List<Map<String, Object>> list3 = new ArrayList<>();//县
        List<Map<String, Object>> list4 = new ArrayList<>();//镇

        return null;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        return true;
    }

    public static void writeFile(String str) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\admin\\Desktop\\ydui.citys.my.min.js"));
            BufferedOutputStream buf = new BufferedOutputStream(outputStream);
            buf.write(str.getBytes());
            buf.flush();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
