package com.yufan.task.dao.addr;

import com.yufan.pojo.TbRegion;
import com.yufan.pojo.TbUserAddr;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 11:47
 * 功能介绍:
 */
public interface IAddrDao {


    /**
     * 查询用户取货地址
     *
     * @param userId
     * @param addrType
     * @return
     */
    public List<Map<String, Object>> queryUserAddrListMap(int userId, Integer addrType);


    /**
     * 查询平台地址
     *
     * @param addrType
     * @return
     */
    public List<Map<String, Object>> queryPlatformListMap(Integer addrType);

    /**
     * 保存用户地址
     *
     * @param userAddr
     */
    public void saveUserAddr(TbUserAddr userAddr);

    public void updateUserAddr(TbUserAddr userAddr);

    /**
     * 查询全国地址
     *
     * @param parentCode
     * @return
     */
    public List<Map<String, Object>> queryCountryAddrList(String parentCode);
    public List<Map<String, Object>> queryCountryAddrListByLevel(int regionLevel);//层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村

    /**
     * 更新用户默认地址
     *
     * @param id
     * @param userId
     */
    public void updateAddrDefaul(int id, int userId);

}
