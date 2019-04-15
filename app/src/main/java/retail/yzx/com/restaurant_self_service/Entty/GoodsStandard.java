package retail.yzx.com.restaurant_self_service.Entty;

/**
 * Created by Administrator on 2017/7/20.
 */

public class GoodsStandard {
    String spec_value;//规格
    boolean isSelect;

    public GoodsStandard(String spec_value, boolean isSelect) {
        this.spec_value = spec_value;
        this.isSelect = isSelect;
    }

    public String getSpec_value() {
        return spec_value;
    }

    public void setSpec_value(String spec_value) {
        this.spec_value = spec_value;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "GoodsStandard{" +
                "spec_value='" + spec_value + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
