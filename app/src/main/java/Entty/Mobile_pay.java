package Entty;

import java.io.Serializable;

/**
 * Created by admin on 2017/4/20.
 * 移动支付实体类
 */
public class Mobile_pay implements Serializable {
    private String name;
    private String money;

    private String order_id;
    private String payed_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayed_time() {
        return payed_time;
    }

    public void setPayed_time(String payed_time) {
        this.payed_time = payed_time;
    }
}
