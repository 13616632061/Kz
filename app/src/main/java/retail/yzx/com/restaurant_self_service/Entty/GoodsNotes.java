package retail.yzx.com.restaurant_self_service.Entty;

/**
 * Created by Administrator on 2017/7/18.
 */

public class GoodsNotes {
    String price_standard;//不同规格商品的价格
    String spec_info_standard;//含有的规格信息
    String is_default;//是否默认的规格
    String product_id;//产品id

    public GoodsNotes(String price_standard, String spec_info_standard, String is_default, String product_id) {
        this.price_standard = price_standard;
        this.spec_info_standard = spec_info_standard;
        this.is_default = is_default;
        this.product_id = product_id;
    }

    public String getPrice_standard() {
        return price_standard;
    }

    public void setPrice_standard(String price_standard) {
        this.price_standard = price_standard;
    }

    public String getSpec_info_standard() {
        return spec_info_standard;
    }

    public void setSpec_info_standard(String spec_info_standard) {
        this.spec_info_standard = spec_info_standard;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "GoodsNotes{" +
                "price_standard='" + price_standard + '\'' +
                ", spec_info_standard='" + spec_info_standard + '\'' +
                ", is_default='" + is_default + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }
}
