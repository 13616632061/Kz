package Entty;

import java.util.List;

/**
 * Created by admin on 2018/12/8.
 */

public class Recharge_smsEntty {


    /**
     * status : 200
     * message : ok
     * data : {"list":[{"id":"1","price":"0.010","num":"99","sort_num":"0","remake":"测试套餐"},{"id":"2","price":"10.000","num":"100","sort_num":"1","remake":"套餐1"},{"id":"3","price":"20.000","num":"200","sort_num":"2","remake":"套餐2"}]}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        private int status;
        private String message;
        private DataBean data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
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
            /**
             * id : 1
             * price : 0.010
             * num : 99
             * sort_num : 0
             * remake : 测试套餐
             */

            private List<ListBean> list;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                private String id;
                private String price;
                private String num;
                private String sort_num;
                private String remake;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getSort_num() {
                    return sort_num;
                }

                public void setSort_num(String sort_num) {
                    this.sort_num = sort_num;
                }

                public String getRemake() {
                    return remake;
                }

                public void setRemake(String remake) {
                    this.remake = remake;
                }
            }
        }
    }
}
