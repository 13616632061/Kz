package retail.yzx.com.supper_self_service.Entty;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/11.
 */

public class Self_Service_GoodsInfo implements Parcelable {

    private String goods_id;
    private String name;
    private String number;
    private String cost;
    private String price;
    private String notes;
    private String size;
    private String product_id;
    private String tag_id;


    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public Self_Service_GoodsInfo() {
    }

    public Self_Service_GoodsInfo(String goods_id, String name, String number, String cost, String price, String notes, String size, String product_id,String tag_id) {
        this.goods_id = goods_id;
        this.name = name;
        this.number = number;
        this.cost = cost;
        this.price = price;
        this.notes = notes;
        this.size = size;
        this.product_id = product_id;
        this.tag_id = tag_id;
    }

    protected Self_Service_GoodsInfo(Parcel in) {
        goods_id = in.readString();
        name = in.readString();
        number = in.readString();
        cost = in.readString();
        price = in.readString();
        notes = in.readString();
        size = in.readString();
        product_id = in.readString();
        tag_id=in.readString();
    }

    public static final Creator<Self_Service_GoodsInfo> CREATOR = new Creator<Self_Service_GoodsInfo>() {
        @Override
        public Self_Service_GoodsInfo createFromParcel(Parcel in) {
            return new Self_Service_GoodsInfo(in);
        }

        @Override
        public Self_Service_GoodsInfo[] newArray(int size) {
            return new Self_Service_GoodsInfo[size];
        }
    };

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "Self_Service_GoodsInfo{" +
                "goods_id='" + goods_id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", cost='" + cost + '\'' +
                ", price='" + price + '\'' +
                ", notes='" + notes + '\'' +
                ", size='" + size + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(cost);
        dest.writeString(price);
        dest.writeString(notes);
        dest.writeString(size);
        dest.writeString(product_id);
        dest.writeString(tag_id);
    }
}
