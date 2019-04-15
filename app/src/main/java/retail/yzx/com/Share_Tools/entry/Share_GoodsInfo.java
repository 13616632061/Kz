package retail.yzx.com.Share_Tools.entry;

import android.os.Parcel;
import android.os.Parcelable;

import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;

/**
 * Created by Administrator on 2017/9/8.
 */

public class Share_GoodsInfo implements Parcelable{
    String id;//商品id
    String name;//商品名字
    String price;//商品价格
    String store;//商品库存
    String bn;//商品编码
    String image_default_url;//商品图片url
    String status;//商品使用状态
    String marketable;//共享商品是否开启
    String cost_money;//收费
    String free_time;//免费时间
    String cost_time;//收费时间

    public Share_GoodsInfo(String id, String name, String price, String store, String bn, String image_default_url, String status, String marketable, String cost_money, String free_time, String cost_time) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store = store;
        this.bn = bn;
        this.image_default_url = image_default_url;
        this.status = status;
        this.marketable = marketable;
        this.cost_money = cost_money;
        this.free_time = free_time;
        this.cost_time = cost_time;
    }

    protected Share_GoodsInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        store = in.readString();
        bn = in.readString();
        image_default_url = in.readString();
        status = in.readString();
        marketable = in.readString();
        cost_money = in.readString();
        free_time = in.readString();
        cost_time = in.readString();
    }

    public static final Creator<Share_GoodsInfo> CREATOR = new Creator<Share_GoodsInfo>() {
        @Override
        public Share_GoodsInfo createFromParcel(Parcel in) {
            return new Share_GoodsInfo(in);
        }

        @Override
        public Share_GoodsInfo[] newArray(int size) {
            return new Share_GoodsInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getImage_default_url() {
        return image_default_url;
    }

    public void setImage_default_url(String image_default_url) {
        this.image_default_url = image_default_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarketable() {
        return marketable;
    }

    public void setMarketable(String marketable) {
        this.marketable = marketable;
    }

    public String getCost_money() {
        return cost_money;
    }

    public void setCost_money(String cost_money) {
        this.cost_money = cost_money;
    }

    public String getFree_time() {
        return free_time;
    }

    public void setFree_time(String free_time) {
        this.free_time = free_time;
    }

    public String getCost_time() {
        return cost_time;
    }

    public void setCost_time(String cost_time) {
        this.cost_time = cost_time;
    }

    @Override
    public String toString() {
        return "Share_GoodsInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", store='" + store + '\'' +
                ", bn='" + bn + '\'' +
                ", image_default_url='" + image_default_url + '\'' +
                ", status='" + status + '\'' +
                ", marketable='" + marketable + '\'' +
                ", cost_money='" + cost_money + '\'' +
                ", free_time='" + free_time + '\'' +
                ", cost_time='" + cost_time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(store);
        dest.writeString(bn);
        dest.writeString(image_default_url);
        dest.writeString(status);
        dest.writeString(marketable);
        dest.writeString(cost_money);
        dest.writeString(free_time);
        dest.writeString(cost_time);
    }
}
