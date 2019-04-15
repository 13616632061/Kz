package Entty;

import java.io.Serializable;

/**
 * Created by admin on 2017/4/14.
 */
public class Cash_entty implements Serializable{
//    单号
    public String order_id;
//    应收
    public String netreceipt;
//    实收
    public String amount;
//    找零
    public String change;
//    时间
    public String payed_time;
//    商家名字
    public String sellername;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getNetreceipt() {
        return netreceipt;
    }

    public void setNetreceipt(String netreceipt) {
        this.netreceipt = netreceipt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPayed_time() {
        return payed_time;
    }

    public void setPayed_time(String payed_time) {
        this.payed_time = payed_time;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }
}
