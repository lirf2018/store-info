package com.yufan.itf.dao;

import com.yufan.pojo.ItfTbItemAdmin;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
public interface IItfAdminDao {

    ItfTbItemAdmin loadItfTbItemAdminByName(String name);

    ItfTbItemAdmin loadItfTbItemAdminById(int id);

    void updateItfTbItemAdminByName(ItfTbItemAdmin itfTbItemAdmin);

}
