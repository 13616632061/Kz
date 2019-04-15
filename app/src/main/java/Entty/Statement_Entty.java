package Entty;

/**
 * Created by admin on 2017/5/3.
 */
public class Statement_Entty {

//    订单号
    public String order_id;
    //下单时间
    public String time;
    //收银员
    public String name;
//支付方式
    public String payment;
// 订单类型
    public String OrderType;
    //金额
    public String money;
    //利润
    public String lirun;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLirun() {
        return lirun;
    }

    public void setLirun(String lirun) {
        this.lirun = lirun;
    }


}
