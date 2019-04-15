package retail.yzx.com.restaurant_self_service.Entty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/18.
 */

public class Self_Service_RestanrauntGoodsInfo implements Parcelable{
    String goods_id;//id
    String name;//名字
    String price;//显示的价格
    String image_default_id;//商品图片
    String nums;//商品数量
    String tag_id;//商品分类
    ArrayList<GoodsStandardInfos> mGoodsStandardInfosList;//商品规格信息
    private ArrayList<GoodsNotes> mGoodsNotesLists;//商品备注信息

    public Self_Service_RestanrauntGoodsInfo(String goods_id, String name, String price, String image_default_id, String nums, ArrayList<GoodsStandardInfos> mGoodsStandardInfosList, ArrayList<GoodsNotes> mGoodsNotesLists,String tag_id) {
        this.goods_id = goods_id;
        this.name = name;
        this.price = price;
        this.image_default_id = image_default_id;
        this.nums = nums;
        this.mGoodsStandardInfosList = mGoodsStandardInfosList;
        this.mGoodsNotesLists = mGoodsNotesLists;
        this.tag_id=tag_id;
    }

    protected Self_Service_RestanrauntGoodsInfo(Parcel in) {
        goods_id = in.readString();
        name = in.readString();
        price = in.readString();
        image_default_id = in.readString();
        nums = in.readString();
        tag_id=in.readString();
    }

    public static final Creator<Self_Service_RestanrauntGoodsInfo> CREATOR = new Creator<Self_Service_RestanrauntGoodsInfo>() {
        @Override
        public Self_Service_RestanrauntGoodsInfo createFromParcel(Parcel in) {
            return new Self_Service_RestanrauntGoodsInfo(in);
        }

        @Override
        public Self_Service_RestanrauntGoodsInfo[] newArray(int size) {
            return new Self_Service_RestanrauntGoodsInfo[size];
        }
    };


    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_default_id() {
        return image_default_id;
    }

    public void setImage_default_id(String image_default_id) {
        this.image_default_id = image_default_id;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public ArrayList<GoodsStandardInfos> getmGoodsStandardInfosList() {
        return mGoodsStandardInfosList;
    }

    public void setmGoodsStandardInfosList(ArrayList<GoodsStandardInfos> mGoodsStandardInfosList) {
        this.mGoodsStandardInfosList = mGoodsStandardInfosList;
    }

    public ArrayList<GoodsNotes> getmGoodsNotesLists() {
        return mGoodsNotesLists;
    }

    public void setmGoodsNotesLists(ArrayList<GoodsNotes> mGoodsNotesLists) {
        this.mGoodsNotesLists = mGoodsNotesLists;
    }

    @Override
    public String toString() {
        return "Self_Service_RestanrauntGoodsInfo{" +
                "goods_id='" + goods_id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", image_default_id='" + image_default_id + '\'' +
                ", nums='" + nums + '\'' +
                ", mGoodsStandardInfosList=" + mGoodsStandardInfosList +
                ", mGoodsNotesLists=" + mGoodsNotesLists +
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
        dest.writeString(price);
        dest.writeString(image_default_id);
        dest.writeString(nums);
        dest.writeString(tag_id);
    }
}
