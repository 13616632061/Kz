package Entty;

/**
 * Created by admin on 2018/12/11.
 */

public class Pay_success {

    /**
     * status : 200
     * message : 支付成功
     * data : {"third_order_id":"70061544492690","pay_way":"ZFBZF","sms_list_id":"1","amount":"1","sms_num":"99","body":"sms buy","add_time":"1544492690","status":"1"}
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
         * third_order_id : 70061544492690
         * pay_way : ZFBZF
         * sms_list_id : 1
         * amount : 1
         * sms_num : 99
         * body : sms buy
         * add_time : 1544492690
         * status : 1
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
            private String third_order_id;
            private String pay_way;
            private String sms_list_id;
            private String amount;
            private String sms_num;
            private String body;
            private String add_time;
            private String status;

            public String getThird_order_id() {
                return third_order_id;
            }

            public void setThird_order_id(String third_order_id) {
                this.third_order_id = third_order_id;
            }

            public String getPay_way() {
                return pay_way;
            }

            public void setPay_way(String pay_way) {
                this.pay_way = pay_way;
            }

            public String getSms_list_id() {
                return sms_list_id;
            }

            public void setSms_list_id(String sms_list_id) {
                this.sms_list_id = sms_list_id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getSms_num() {
                return sms_num;
            }

            public void setSms_num(String sms_num) {
                this.sms_num = sms_num;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
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
