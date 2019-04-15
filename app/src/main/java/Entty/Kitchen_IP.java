package Entty;

/**
 * Created by admin on 2019/3/30.
 */

public class Kitchen_IP {
    String tag_id;
    String IP;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "Kitchen_IP{" +
                "tag_id='" + tag_id + '\'' +
                ", IP='" + IP + '\'' +
                '}';
    }
}
