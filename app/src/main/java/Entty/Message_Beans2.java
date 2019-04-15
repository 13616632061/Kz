package Entty;

/**
 * Created by YGD on 2018/1/13.
 */

public class Message_Beans2 {

        private String id;
        private String title;
        private String content;
        private String extend;
        private String type;
        private String pal;
        private String user_id;
        private String time;
        private String state;

         public String getState() {
         return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getExtend() {
            return extend;
        }

        public void setExtend(String extend) {
            this.extend = extend;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPal() {
            return pal;
        }

        public void setPal(String pal) {
            this.pal = pal;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


    @Override
    public String toString() {
        return "Message_Beans2{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", extend='" + extend + '\'' +
                ", type='" + type + '\'' +
                ", pal='" + pal + '\'' +
                ", user_id='" + user_id + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
