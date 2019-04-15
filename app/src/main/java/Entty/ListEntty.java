package Entty;

/**
 * Created by admin on 2017/3/25.
 * 保存数量和选中状态
 *
 */
public class ListEntty {
    public boolean isChecked;
    public int number;
    public String index;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
