package Entty;

import java.util.List;

/**
 * Created by admin on 2018/5/10.
 */

public class In_Out_Details {


    /**
     * status : 200
     * message : ok
     * data : [{"id":"16264","seller_id":"7006","store_id":"1831","goods_id":"94279","nums":"1","cost":"0.010","createtime":"1539938630","type":"1","store":"-51","name":"蒙牛纯牛奶250ml","bncode":"6923644223458","provider_name":null},{"id":"16265","seller_id":"7006","store_id":"1831","goods_id":"110424","nums":"1","cost":"21.000","createtime":"1539938630","type":"1","store":"885","name":"香巴佬蜜辣鸡排","bncode":"1006828100345","provider_name":null}]
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
         * id : 16264
         * seller_id : 7006
         * store_id : 1831
         * goods_id : 94279
         * nums : 1
         * cost : 0.010
         * createtime : 1539938630
         * type : 1
         * store : -51
         * name : 蒙牛纯牛奶250ml
         * bncode : 6923644223458
         * provider_name : null
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
            private String id;
            private String seller_id;
            private String store_id;
            private String goods_id;
            private String nums;
            private String cost;
            private String createtime;
            private String type;
            private String store;
            private String name;
            private String bncode;
            private String provider_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBncode() {
                return bncode;
            }

            public void setBncode(String bncode) {
                this.bncode = bncode;
            }

            public String getProvider_name() {
                return provider_name;
            }

            public void setProvider_name(String provider_name) {
                this.provider_name = provider_name;
            }
        }
    }
}
