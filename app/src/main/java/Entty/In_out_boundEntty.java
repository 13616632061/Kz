package Entty;

import java.util.List;

/**
 * Created by admin on 2018/5/8.
 */

public class In_out_boundEntty {


    /**
     * status : 200
     * message : ok
     * data : {"list":[{"id":"10","seller_id":"115","oparator":"小明饭店","remark":"测试备注","order_id":"180511143715846","nums":"1","money":"3.000","createtime":"1526020626","type":"0"},{"id":"9","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511141010260","nums":"1","money":"3.000","createtime":"1526019002","type":"0"},{"id":"8","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511140418365","nums":"5","money":"12.000","createtime":"1526018655","type":"0"},{"id":"7","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511140277952","nums":"4","money":"9.000","createtime":"1526018575","type":"0"},{"id":"6","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511140188903","nums":"2","money":"4.500","createtime":"1526018497","type":"0"},{"id":"5","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511120951264","nums":"1","money":"3.000","createtime":"1526011781","type":"0"},{"id":"4","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511114559552","nums":"1","money":"3.000","createtime":"1526010306","type":"0"},{"id":"3","seller_id":"115","oparator":"小明饭店","remark":"备注显示","order_id":"180511112628723","nums":"2","money":"4.500","createtime":"1526009181","type":"0"}],"total":[{"num":"10"}]}
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
            /**
             * id : 10
             * seller_id : 115
             * oparator : 小明饭店
             * remark : 测试备注
             * order_id : 180511143715846
             * nums : 1
             * money : 3.000
             * createtime : 1526020626
             * type : 0
             */

            private List<ListBean> list;
            /**
             * num : 10
             */

            private List<TotalBean> total;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<TotalBean> getTotal() {
                return total;
            }

            public void setTotal(List<TotalBean> total) {
                this.total = total;
            }

            public static class ListBean {
                private String id;
                private String seller_id;
                private String oparator;
                private String remark;
                private String order_id;
                private String nums;
                private String money;
                private String createtime;
                private String type;

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

                public String getOparator() {
                    return oparator;
                }

                public void setOparator(String oparator) {
                    this.oparator = oparator;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
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
            }

            public static class TotalBean {
                private String num;

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }
            }
        }
    }
}
