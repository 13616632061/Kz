package Entty;

import java.util.List;

/**
 * Created by admin on 2018/12/11.
 */

public class SendLogsms_Entty {

    /**
     * status : 200
     * message : ok
     * data : {"count":14,"page_count":2,"list":[{"id":"14","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544064046","remake":null},{"id":"13","template_id":"235175","seller_id":"7006","fail":"2","success":"1","tels":"123,3,18757653662","params":"s1,s2","add_time":"1544061532","remake":null},{"id":"12","template_id":"235175","seller_id":"7006","fail":"3","success":"0","tels":"123,3,18","params":"s1,s2","add_time":"1544060096","remake":null},{"id":"11","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544060060","remake":null},{"id":"10","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544060050","remake":null},{"id":"9","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059293","remake":null},{"id":"8","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059222","remake":null},{"id":"7","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059170","remake":null},{"id":"6","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059142","remake":null},{"id":"5","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544058655","remake":null}]}
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
        /**
         * count : 14
         * page_count : 2
         * list : [{"id":"14","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544064046","remake":null},{"id":"13","template_id":"235175","seller_id":"7006","fail":"2","success":"1","tels":"123,3,18757653662","params":"s1,s2","add_time":"1544061532","remake":null},{"id":"12","template_id":"235175","seller_id":"7006","fail":"3","success":"0","tels":"123,3,18","params":"s1,s2","add_time":"1544060096","remake":null},{"id":"11","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544060060","remake":null},{"id":"10","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544060050","remake":null},{"id":"9","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059293","remake":null},{"id":"8","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059222","remake":null},{"id":"7","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059170","remake":null},{"id":"6","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544059142","remake":null},{"id":"5","template_id":"235175","seller_id":"7006","fail":"2","success":"0","tels":"123,3","params":"s1,s2","add_time":"1544058655","remake":null}]
         */

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
            private int count;
            private int page_count;
            /**
             * id : 14
             * template_id : 235175
             * seller_id : 7006
             * fail : 2
             * success : 0
             * tels : 123,3
             * params : s1,s2
             * add_time : 1544064046
             * remake : null
             */

            private List<ListBean> list;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getPage_count() {
                return page_count;
            }

            public void setPage_count(int page_count) {
                this.page_count = page_count;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                private String id;
                private String template_id;
                private String seller_id;
                private String fail;
                private String success;
                private String tels;
                private String params;
                private String add_time;
                private Object remake;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTemplate_id() {
                    return template_id;
                }

                public void setTemplate_id(String template_id) {
                    this.template_id = template_id;
                }

                public String getSeller_id() {
                    return seller_id;
                }

                public void setSeller_id(String seller_id) {
                    this.seller_id = seller_id;
                }

                public String getFail() {
                    return fail;
                }

                public void setFail(String fail) {
                    this.fail = fail;
                }

                public String getSuccess() {
                    return success;
                }

                public void setSuccess(String success) {
                    this.success = success;
                }

                public String getTels() {
                    return tels;
                }

                public void setTels(String tels) {
                    this.tels = tels;
                }

                public String getParams() {
                    return params;
                }

                public void setParams(String params) {
                    this.params = params;
                }

                public String getAdd_time() {
                    return add_time;
                }

                public void setAdd_time(String add_time) {
                    this.add_time = add_time;
                }

                public Object getRemake() {
                    return remake;
                }

                public void setRemake(Object remake) {
                    this.remake = remake;
                }
            }
        }
    }
}
