package Entty;

import java.util.List;

/**
 * Created by admin on 2018/12/6.
 */

public class Template_Entty {


    /**
     * status : 200
     * message : ok
     * data : [{"id":"2","template_id":"234732","content":"{1}为您的登录验证码，请于{2}分钟内填写。如非本人操作，请忽略本短信。","sign":null,"sort_num":"1","remake":"普通短信"},{"id":"1","template_id":"235175","content":"{1}尊敬的会员用户，双十二即来，本店火爆促销！{2}，期待您的光临！回T退订","sign":null,"sort_num":"0","remake":"双十二活动"}]
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
         * id : 2
         * template_id : 234732
         * content : {1}为您的登录验证码，请于{2}分钟内填写。如非本人操作，请忽略本短信。
         * sign : null
         * sort_num : 1
         * remake : 普通短信
         */

        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private String id;
            private String template_id;
            private String content;
            private Object sign;
            private String sort_num;
            private String remake;

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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Object getSign() {
                return sign;
            }

            public void setSign(Object sign) {
                this.sign = sign;
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
