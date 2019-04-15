package Entty;

import java.io.Serializable;

/**
 * Created by admin on 2017/4/27.
 * 退款的实体类
 */
public class Refund_entty implements Serializable {

//    退款商家名字
    private String usersname;
//    退款商品名
    private String name;

    private String price;

    private String nums;
    private String order_id;
    private String time;

    public String getUsersname() {
        return usersname;
    }

    public void setUsersname(String usersname) {
        this.usersname = usersname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

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


}
