package Entty;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */
public class Group_Entty {


    /**
     * report_id : 97
     * total_amount : 374.000
     * seller_name : 山东蓝翔
     * cashier : 山东蓝翔
     * addtime : 1498550010
     * child : [{"goods_name":"J小老板芥末味紫菜","bncode":"8858702403597","price":"9.500","nums":"17"},{"goods_name":"J小老板原味","bncode":"8858702410816","price":"1.500","nums":"17"},{"goods_name":"X喜之郎脆脆冰哈密瓜味","bncode":"6926475203606","price":"2.000","nums":"17"},{"goods_name":"J台湾美馔甘庶汁","bncode":"4712826825151","price":"7.000","nums":"17"},{"goods_name":"X喜之郎脆脆冰酸奶味","bncode":"6926475203583","price":"1.000","nums":"17"},{"goods_name":"X喜之郎脆脆冰葡萄味","bncode":"6926475203569","price":"1.000","nums":"17"}]
     */





    private String report_id;
    private String total_amount;
    private String seller_name;
    private String cashier;
    private String addtime;
    /**
     * goods_name : J小老板芥末味紫菜
     * bncode : 8858702403597
     * price : 9.500
     * nums : 17
     */

    private List<ChildBean> child;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class ChildBean {
        private String goods_name;
        private String bncode;
        private String price;
        private String nums;
        private String goods_id;
        private String goods_status;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoods_status() {
            return goods_status;
        }

        public void setGoods_status(String goods_status) {
            this.goods_status = goods_status;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }


        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getBncode() {
            return bncode;
        }

        public void setBncode(String bncode) {
            this.bncode = bncode;
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
    }
}
