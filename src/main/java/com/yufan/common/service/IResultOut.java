package com.yufan.common.service;


import com.yufan.common.bean.ReceiveJsonBean;

public interface IResultOut {

    public String getResult(ReceiveJsonBean receiveJsonBean);

    public boolean checkParam(ReceiveJsonBean receiveJsonBean);
}
