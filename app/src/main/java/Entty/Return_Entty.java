package Entty;

import java.util.List;

/**
 * Created by admin on 2017/6/9.
 */
public class Return_Entty {

    /**
     * order_id : 170428153076549
     * seller_id : 56
     * worker_name : 0
     * addtime : 1497001501
     * ship_id : 1706091745013952
     * dlytype : reship
     * dly_id : 34324323
     * items : [{"sum":"60.0","price":"60.0","item_id":"24614","nums":"1","goods_id":"6751","name":"test999"}]
     */

    private String order_id;
    private String seller_id;
    private String worker_name;
    private String addtime;
    private String ship_id;
    private String dlytype;
    private String dly_id;
    /**
     * sum : 60.0
     * price : 60.0
     * item_id : 24614
     * nums : 1
     * goods_id : 6751
     * name : test999
     */

    private List<ItemsBean> items;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getShip_id() {
        return ship_id;
    }

    public void setShip_id(String ship_id) {
        this.ship_id = ship_id;
    }

    public String getDlytype() {
        return dlytype;
    }

    public void setDlytype(String dlytype) {
        this.dlytype = dlytype;
    }

    public String getDly_id() {
        return dly_id;
    }

    public void setDly_id(String dly_id) {
        this.dly_id = dly_id;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        private String sum;
        private String price;
        private String item_id;
        private String nums;
        private String goods_id;
        private String name;

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
