package com.yufan.common.service;


import com.yufan.common.bean.ReceiveJsonBean;

public interface IResultOut {

    public boolean checkParam(ReceiveJsonBean receiveJsonBean);

    public String getResult(ReceiveJsonBean receiveJsonBean);
}
