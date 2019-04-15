package Entty;

/**
 * Created by admin on 2017/4/6.
 */
public class Fenlei_Entty {

    private String name;
    private boolean isVisibility;
    private boolean isedit;
    private int tag_id;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public boolean isedit() {
        return isedit;
    }

    public void setIsedit(boolean isedit) {
        this.isedit = isedit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisibility() {
        return isVisibility;
    }

    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }


}
