package Entty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/4/22.
 */
public class Order_Entty implements Serializable {


    /**
     * status : 200
     * message : ok
     * data : [{"order_id":"170422110764315","obj_id":"25309","pay_status":"0","seller_id":"56","createtime":"1492830458","mark_text":null,"name":"蓝宝基尼,老干妈,1m1米,蓝宝基尼,老干妈,这么,mm,mmm","nums":"1","price":"185.00","goods_id":"6710","item_id":"22120","goods":[{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22120","goods_num":"1"},{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22121","goods_num":"1"},{"goods_name":"1m1米","goods_id":"6679","goods_price":"3.000","item_id":"22122","goods_num":"1"},{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22123","goods_num":"1"},{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22124","goods_num":"1"},{"goods_name":"这么","goods_id":"6720","goods_price":"25.000","item_id":"22125","goods_num":"4"},{"goods_name":"mm","goods_id":"6688","goods_price":"3.000","item_id":"22126","goods_num":"1"},{"goods_name":"mmm","goods_id":"6686","goods_price":"3.000","item_id":"22127","goods_num":"1"}]},{"order_id":"170422103366823","obj_id":"25291","pay_status":"0","seller_id":"56","createtime":"1492830393","mark_text":"123","name":"老干妈,小米6,小米5,小米4","nums":"2","price":"39570.00","goods_id":"5681","item_id":"22101","goods":[{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22101","goods_num":"2"},{"goods_name":"小米6","goods_id":"6638","goods_price":"34555.000","item_id":"22102","goods_num":"1"},{"goods_name":"小米5","goods_id":"6637","goods_price":"2444.000","item_id":"22105","goods_num":"1"},{"goods_name":"小米4","goods_id":"6636","goods_price":"2555.000","item_id":"22119","goods_num":"1"}]},{"order_id":"170422105293678","obj_id":"25296","pay_status":"0","seller_id":"56","createtime":"1492830182","mark_text":"123","name":"老干妈,小米6,小米5,小米4,1111","nums":"2","price":"39573.00","goods_id":"5681","item_id":"22107","goods":[{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22107","goods_num":"2"},{"goods_name":"小米6","goods_id":"6638","goods_price":"34555.000","item_id":"22108","goods_num":"1"},{"goods_name":"小米5","goods_id":"6637","goods_price":"2444.000","item_id":"22109","goods_num":"1"},{"goods_name":"小米4","goods_id":"6636","goods_price":"2555.000","item_id":"22110","goods_num":"1"},{"goods_name":"1111","goods_id":"6661","goods_price":"3.000","item_id":"22118","goods_num":"1"}]},{"order_id":"170422110050607","obj_id":"25303","pay_status":"0","seller_id":"56","createtime":"1492830011","mark_text":null,"name":"蓝宝基尼,老干妈,1m1米,蓝宝基尼","nums":"1","price":"71.00","goods_id":"6710","item_id":"22114","goods":[{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22114","goods_num":"1"},{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22115","goods_num":"1"},{"goods_name":"1m1米","goods_id":"6679","goods_price":"3.000","item_id":"22116","goods_num":"1"},{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22117","goods_num":"1"}]},{"order_id":"170421172957776","obj_id":"24896","pay_status":"0","seller_id":"56","createtime":"1492766977","mark_text":"2125","name":"小米4,手抓骨（1根）","nums":"1","price":"2563.00","goods_id":"6636","item_id":"21706","goods":[{"goods_name":"小米4","goods_id":"6636","goods_price":"2555.000","item_id":"21706","goods_num":"1"},{"goods_name":"手抓骨（1根）","goods_id":"5681","goods_price":"8.000","item_id":"21709","goods_num":"1"}]}]
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable{


        /**
         * order_id : 170422110764315
         * obj_id : 25309
         * pay_status : 0
         * seller_id : 56
         * createtime : 1492830458
         * mark_text : null
         * name : 蓝宝基尼,老干妈,1m1米,蓝宝基尼,老干妈,这么,mm,mmm
         * nums : 1
         * price : 185.00
         * goods_id : 6710
         * item_id : 22120
         * goods : [{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22120","goods_num":"1"},{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22121","goods_num":"1"},{"goods_name":"1m1米","goods_id":"6679","goods_price":"3.000","item_id":"22122","goods_num":"1"},{"goods_name":"蓝宝基尼","goods_id":"6710","goods_price":"30.000","item_id":"22123","goods_num":"1"},{"goods_name":"老干妈","goods_id":"5681","goods_price":"8.000","item_id":"22124","goods_num":"1"},{"goods_name":"这么","goods_id":"6720","goods_price":"25.000","item_id":"22125","goods_num":"4"},{"goods_name":"mm","goods_id":"6688","goods_price":"3.000","item_id":"22126","goods_num":"1"},{"goods_name":"mmm","goods_id":"6686","goods_price":"3.000","item_id":"22127","goods_num":"1"}]
         */

        public List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            private String order_id;
            private String obj_id;
            private String pay_status;
            private String seller_id;
            private String createtime;
            private String mark_text;
            private String name;
            private String nums;
            private String price;
            private String goods_id;
            private String item_id;



            /**
             * goods_name : 蓝宝基尼
             * goods_id : 6710
             * goods_price : 30.000
             * item_id : 22120
             * goods_num : 1
             */



            public List<GoodsBean> goods;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getObj_id() {
                return obj_id;
            }

            public void setObj_id(String obj_id) {
                this.obj_id = obj_id;
            }

            public String getPay_status() {
                return pay_status;
            }

            public void setPay_status(String pay_status) {
                this.pay_status = pay_status;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getMark_text() {
                return mark_text;
            }

            public void setMark_text(String mark_text) {
                this.mark_text = mark_text;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public static class GoodsBean implements Serializable{


                public String getCost() {
                    return cost;
                }

                public void setCost(String cost) {
                    this.cost = cost;
                }

                private String Store;
                private String cost;
                private String goods_name;
                private String goods_id;
                private String goods_price;
                private String item_id;
                private String goods_num;


                public String getStore() {
                    return Store;
                }

                public void setStore(String store) {
                    Store = store;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getGoods_price() {
                    return goods_price;
                }

                public void setGoods_price(String goods_price) {
                    this.goods_price = goods_price;
                }

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public String getGoods_num() {
                    return goods_num;
                }

                public void setGoods_num(String goods_num) {
                    this.goods_num = goods_num;
                }
            }
        }
    }
}
