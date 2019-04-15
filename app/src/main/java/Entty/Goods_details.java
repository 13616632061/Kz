package Entty;

/**
 * Created by admin on 2017/8/3.
 * 供应商商品详情实体类
 */
public class Goods_details {


    /**
     * bncode : 0
     * goods_name : 风旺山珍海味160g
     * nums : 40
     * price : 4.000
     * goods_status : 0
     * menu : 世界男模
     */

    private String bncode;
    private String goods_name;
    private String nums;
    private String price;
    private String goods_status;
    private String menu;
    private String id;
    //审核
    private String is_verify;
    private String goods_id;
    //验收数量
    private String before_nums;

    public String getBefore_nums() {
        return before_nums;
    }

    public void setBefore_nums(String before_nums) {
        this.before_nums = before_nums;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBncode() {
        return bncode;
    }

    public void setBncode(String bncode) {
        this.bncode = bncode;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }


}
