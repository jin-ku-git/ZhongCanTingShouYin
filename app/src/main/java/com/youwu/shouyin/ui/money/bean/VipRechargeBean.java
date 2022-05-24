package com.youwu.shouyin.ui.money.bean;

import java.io.Serializable;

/**
 * 充值金额表
 */
public class VipRechargeBean implements Serializable {


    private String id;
    private String recharge_price;//充值金额
    private String recharge_content;//赠送内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecharge_price() {
        return recharge_price;
    }

    public void setRecharge_price(String recharge_price) {
        this.recharge_price = recharge_price;
    }

    public String getRecharge_content() {
        return recharge_content;
    }

    public void setRecharge_content(String recharge_content) {
        this.recharge_content = recharge_content;
    }
}
