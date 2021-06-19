package com.yufan.kc.service.impl.store;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbStoreInout;
import com.yufan.kc.dao.store.StoreInOutDao;
import com.yufan.kc.service.impl.param.GenerateShopCode;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:25
 * 功能介绍:
 */
@Service("kc_add_store")
public class AddStoreInOut implements IResultOut {

    private Logger LOG = Logger.getLogger(AddStoreInOut.class);

    @Autowired
    private StoreInOutDao storeInOutDao;

    @Autowired
    private GenerateShopCode generateShopCode;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            // 相同商品入库数量
            Integer store = data.getInteger("store");
            Integer warning = data.getInteger("warning");
            Integer endTimeType = data.getInteger("endTimeType");//0 结束日期1年2月3天
            Integer year = data.getInteger("year");
            Integer month = data.getInteger("month");
            TbStoreInout storeInout = JSONObject.toJavaObject(data, TbStoreInout.class);
            storeInout.setLastUpdateTime(new Timestamp(new Date().getTime()));
            // 计算截止天数
            if (endTimeType == 0) {
                // 计算有效天数
                Long day = (storeInout.getEffectToTime().getTime() - storeInout.getMakeDay().getTime()) / (1000 * 60 * 60 * 24) + 1;
                storeInout.setEffectDay(day.intValue());
            } else if (endTimeType == 1) {
                Date date = DatetimeUtil.convertStrToDate(DatetimeUtil.timeStamp2Date(storeInout.getMakeDay().getTime(), null));
                storeInout.setEffectToTime(new Timestamp(DatetimeUtil.addYears(date, year).getTime()));
            } else if (endTimeType == 2) {
                Date date = DatetimeUtil.convertStrToDate(DatetimeUtil.timeStamp2Date(storeInout.getMakeDay().getTime(), null));
                storeInout.setEffectToTime(new Timestamp(DatetimeUtil.addMonths(date, month).getTime()));
            } else {
                Date date = DatetimeUtil.convertStrToDate(DatetimeUtil.timeStamp2Date(storeInout.getMakeDay().getTime(), null));
                storeInout.setEffectToTime(new Timestamp(DatetimeUtil.addDays(date, storeInout.getEffectDay()).getTime()));
            }

            // 查询相同商品条形码的商品是否是相同规格和规格数据
            Map<String, Object> checkMap = storeInOutDao.findOneStoreByGoodsCode(storeInout.getGoodsCode());
            if (null == warning && null != checkMap) {
                String goodsUnit = String.valueOf(checkMap.get("goods_unit"));
                int unitCount = Integer.parseInt(String.valueOf(checkMap.get("unit_count")));
                if (!goodsUnit.equals(storeInout.getGoodsUnit()) || unitCount != storeInout.getUnitCount()) {
                    return packagMsg(ResultCode.THE_SAME_GOODS_WARING.getResp_code(), dataJson);
                }
            }

            if (StringUtils.isEmpty(storeInout.getShopCode())) {
                String code = generateShopCode.generate(Constants.GENERATE_TYPE, 1);
                code = Constants.SHOP_CODE_MARK + code;
                LOG.info("code1=" + code);
                storeInout.setShopCode(code);
                storeInout.setShopCodeDate(getShopCodeDate(code));
            }
            if (storeInout.getIncomeId() > 0) {
                // 如果商品对应库存数据信息的商品码修改，则需要判断原对应商品是否只有一条，如果只有一条，也要做删除处理
                storeInOutDao.deleteGoods(storeInout.getIncomeId());
                storeInOutDao.updateStoreInOut(storeInout);

            } else {
                // 判断入库商品是否已匹配
                boolean flag = storeInOutDao.checkGoodsExist(storeInout.getGoodsCode());
                Integer isMatching = flag ? 1 : 0;
                if (store == 1) {
                    if (StringUtils.isEmpty(storeInout.getShopCode())) {
                        String code = generateShopCode.generate(Constants.GENERATE_TYPE, 1);
                        code = Constants.SHOP_CODE_MARK + code;
                        LOG.info("code2=" + code);
                        storeInout.setShopCode(code);
                        storeInout.setShopCodeDate(getShopCodeDate(code));
                    }
                    storeInout.setIsMatching(isMatching.byteValue());
                    storeInout.setCreateTime(new Timestamp(new Date().getTime()));
                    storeInout.setLastUpdateTime(new Timestamp(new Date().getTime()));
                    storeInOutDao.saveStoreInOut(storeInout);
                } else {
                    // 设置商品店铺编号日期 AUTO20201030234738-1008
                    String code = generateShopCode.generate(Constants.GENERATE_TYPE, store);
                    String pefx = code.split("-")[0];
                    int endNum = Integer.parseInt(code.split("-")[1]);
                    for (int i = 0; i < store; i++) {
                        TbStoreInout st = JSONObject.toJavaObject(data, TbStoreInout.class);
                        code = Constants.SHOP_CODE_MARK + pefx + endNum;
                        LOG.info("code3=" + code);
                        st.setShopCode(code);
                        st.setShopCodeDate(getShopCodeDate(code));
                        st.setCreateTime(new Timestamp(new Date().getTime()));
                        st.setIsMatching(isMatching.byteValue());
                        st.setLastUpdateTime(new Timestamp(new Date().getTime()));
                        storeInOutDao.saveStoreInOut(st);
                        endNum++;
                    }
                }
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    public String getShopCodeDate(String code) {
        return code.substring(5, 13);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            TbStoreInout storeInout = JSONObject.toJavaObject(data, TbStoreInout.class);
            Integer store = data.getInteger("store");
            Integer endTimeType = data.getInteger("endTimeType");//0 结束日期1年2月3天
            Integer year = data.getInteger("year");
            Integer month = data.getInteger("month");
            Integer effectDay = data.getInteger("effectDay");
            if (null == store || store == 0) {
                return false;
            }
            if (null == endTimeType || (endTimeType != 0 && endTimeType != 1 && endTimeType != 2 && endTimeType != 3)) {
                return false;
            }
            if (storeInout.getEffectToTime() == null && year == null && month == null && effectDay == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}