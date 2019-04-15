package Entty;

import java.util.List;

/**
 * Created by admin on 2018/4/21.
 */

public class Delivery_entty {


    /**
     * status : 200
     * message : ok
     * data : [{"goods_id":"46954","name":"充电宝","price":"6.000","cost":"0.000","bncode":"0","store":"3","good_limit":"8","good_stock":"4"},{"goods_id":"46956","name":"电脑","price":"6.000","cost":"0.000","bncode":"0","store":"1","good_limit":"4","good_stock":"2"},{"goods_id":"46959","name":"苹果","price":"0.200","cost":"0.100","bncode":"36900000000311","store":"1","good_limit":"0","good_stock":"2"}]
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        private String status;
        private String message;
        /**
         * goods_id : 46954
         * name : 充电宝
         * price : 6.000
         * cost : 0.000
         * bncode : 0
         * store : 3
         * good_limit : 8
         * good_stock : 4
         */

        private List<DataBean> data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private String goods_id;
            private String name;
            private String price;
            private String cost;
            private String bncode;
            private String store;
            private String good_limit;
            private String good_stock;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getBncode() {
                return bncode;
            }

            public void setBncode(String bncode) {
                this.bncode = bncode;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getGood_limit() {
                return good_limit;
            }

            public void setGood_limit(String good_limit) {
                this.good_limit = good_limit;
            }

            public String getGood_stock() {
                return good_stock;
            }

            public void setGood_stock(String good_stock) {
                this.good_stock = good_stock;
            }
        }
    }
}
