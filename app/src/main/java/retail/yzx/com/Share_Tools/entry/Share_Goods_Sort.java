package retail.yzx.com.Share_Tools.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/8.
 */

public class Share_Goods_Sort implements Parcelable {
    String type_id;//共享商品分类id
    String type_name;//共享商品分类名
    boolean share_goods_sort_click;//分类是否被选中
    ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;//共享商品信息

    public Share_Goods_Sort(String type_id, String type_name, boolean share_goods_sort_click, ArrayList<Share_GoodsInfo> mShare_GoodsInfoList) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.share_goods_sort_click = share_goods_sort_click;
        this.mShare_GoodsInfoList = mShare_GoodsInfoList;
    }

    protected Share_Goods_Sort(Parcel in) {
        type_id = in.readString();
        type_name = in.readString();
        share_goods_sort_click = in.readByte() != 0;
        mShare_GoodsInfoList = in.createTypedArrayList(Share_GoodsInfo.CREATOR);
    }

    public static final Creator<Share_Goods_Sort> CREATOR = new Creator<Share_Goods_Sort>() {
        @Override
        public Share_Goods_Sort createFromParcel(Parcel in) {
            return new Share_Goods_Sort(in);
        }

        @Override
        public Share_Goods_Sort[] newArray(int size) {
            return new Share_Goods_Sort[size];
        }
    };

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public boolean isShare_goods_sort_click() {
        return share_goods_sort_click;
    }

    public void setShare_goods_sort_click(boolean share_goods_sort_click) {
        this.share_goods_sort_click = share_goods_sort_click;
    }

    public ArrayList<Share_GoodsInfo> getmShare_GoodsInfoList() {
        return mShare_GoodsInfoList;
    }

    public void setmShare_GoodsInfoList(ArrayList<Share_GoodsInfo> mShare_GoodsInfoList) {
        this.mShare_GoodsInfoList = mShare_GoodsInfoList;
    }

    @Override
    public String toString() {
        return "Share_Goods_Sort{" +
                "type_id='" + type_id + '\'' +
                ", type_name='" + type_name + '\'' +
                ", share_goods_sort_click=" + share_goods_sort_click +
                ", mShare_GoodsInfoList=" + mShare_GoodsInfoList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type_id);
        dest.writeString(type_name);
        dest.writeByte((byte) (share_goods_sort_click ? 1 : 0));
        dest.writeTypedList(mShare_GoodsInfoList);
    }
}
