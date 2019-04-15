package retail.yzx.com.restaurant_nomal.Entry;

import java.util.List;

/**
 * Created by admin on 2018/12/18.
 */

public class Reserve_Entty {


    /**
     * status : 200
     * message : ok
     * data : {"page":1,"totalPage":1,"lists":[{"id":"126","seller_id":"7006","table_name":"1号桌","person_name":"t","type_id":"38","nums":"6","money":"70.000","mobile":"45497654332","reserve_time":"1545095239","schedule_status":"0","status":"2"},{"id":"139","seller_id":"7006","table_name":null,"person_name":"ty","type_id":null,"nums":"0","money":"0.000","mobile":"0","reserve_time":"0","schedule_status":"0","status":"3"},{"id":"140","seller_id":"7006","table_name":null,"person_name":"tf","type_id":null,"nums":"6","money":"50.000","mobile":"45497654332","reserve_time":"1545095239","schedule_status":"0","status":"3"},{"id":"141","seller_id":"7006","table_name":null,"person_name":"fhff","type_id":null,"nums":"6","money":"70.000","mobile":"564890085543","reserve_time":"1545096757","schedule_status":"0","status":"3"},{"id":"142","seller_id":"7006","table_name":null,"person_name":"oppp","type_id":null,"nums":"6","money":"80.000","mobile":"18720961328","reserve_time":"1545284294","schedule_status":"0","status":"2"}]}
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
         * page : 1
         * totalPage : 1
         * lists : [{"id":"126","seller_id":"7006","table_name":"1号桌","person_name":"t","type_id":"38","nums":"6","money":"70.000","mobile":"45497654332","reserve_time":"1545095239","schedule_status":"0","status":"2"},{"id":"139","seller_id":"7006","table_name":null,"person_name":"ty","type_id":null,"nums":"0","money":"0.000","mobile":"0","reserve_time":"0","schedule_status":"0","status":"3"},{"id":"140","seller_id":"7006","table_name":null,"person_name":"tf","type_id":null,"nums":"6","money":"50.000","mobile":"45497654332","reserve_time":"1545095239","schedule_status":"0","status":"3"},{"id":"141","seller_id":"7006","table_name":null,"person_name":"fhff","type_id":null,"nums":"6","money":"70.000","mobile":"564890085543","reserve_time":"1545096757","schedule_status":"0","status":"3"},{"id":"142","seller_id":"7006","table_name":null,"person_name":"oppp","type_id":null,"nums":"6","money":"80.000","mobile":"18720961328","reserve_time":"1545284294","schedule_status":"0","status":"2"}]
         */

        private DataBean data;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private int page;
            private int totalPage;
            private String count;
            /**
             * id : 126
             * seller_id : 7006
             * table_name : 1号桌
             * person_name : t
             * type_id : 38
             * nums : 6
             * money : 70.000
             * mobile : 45497654332
             * reserve_time : 1545095239
             * schedule_status : 0
             * status : 2
             */

            private List<ListsBean> lists;


            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public List<ListsBean> getLists() {
                return lists;
            }

            public void setLists(List<ListsBean> lists) {
                this.lists = lists;
            }

            public static class ListsBean {
                private String id;
                private String seller_id;
                private String table_name;
                private String person_name;
                private String type_id;
                private String nums;
                private String money;
                private String mobile;
                private String reserve_time;
                private String schedule_status;
                private String status;

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

                public String getTable_name() {
                    return table_name;
                }

                public void setTable_name(String table_name) {
                    this.table_name = table_name;
                }

                public String getPerson_name() {
                    return person_name;
                }

                public void setPerson_name(String person_name) {
                    this.person_name = person_name;
                }

                public String getType_id() {
                    return type_id;
                }

                public void setType_id(String type_id) {
                    this.type_id = type_id;
                }

                public String getNums() {
                    return nums;
                }

                public void setNums(String nums) {
                    this.nums = nums;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getReserve_time() {
                    return reserve_time;
                }

                public void setReserve_time(String reserve_time) {
                    this.reserve_time = reserve_time;
                }

                public String getSchedule_status() {
                    return schedule_status;
                }

                public void setSchedule_status(String schedule_status) {
                    this.schedule_status = schedule_status;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }
}
