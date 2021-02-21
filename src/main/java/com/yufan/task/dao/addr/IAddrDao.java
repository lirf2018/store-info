package com.yufan.task.dao.addr;

import com.yufan.pojo.TbPlatformAddr;
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
    public void updateAddrDefaul(Integer id, int userId);

    /**
     * 删除用户地址
     *
     * @param id
     * @param userId
     */
    public void deleteUserAddr(Integer id, int userId);

    /**
     * 查询运费
     *
     * @param regionCode
     * @return
     */
    public List<Map<String, Object>> queryAddrByRegionCode(String regionCode,Integer status);
    public List<Map<String, Object>> queryAddrByRegionIds(String regionIds,Integer status);

    /**
     * 查询用户收货地址
     *
     * @param userAddrId
     * @return
     */
    public TbUserAddr loadUserAddrById(int userAddrId);


    /**
     * 查询平台地址
     *
     * @param id
     * @return
     */
    public TbPlatformAddr loadPlatformAddr(int id);

    List<Map<String,Object>> queryGlobelAddr(String parentId);

    List<Map<String,Object>> queryGlobelAddrByIds(String ids);

}