package retail.yzx.com.restaurant_nomal.Entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Res_GoodsOrders implements Parcelable {
    String order_id;
    String order_time;
    String order_table_nums;
    String order_people_num;
    String order_notes;
    String order_total_nums;
    String order_total_money;
    String order_dopackage;
    String order_name;
    String table_id;
    boolean open_goodnote;
    ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;

    public Res_GoodsOrders(String order_id, String order_time, String order_table_nums, String order_people_num, String order_notes, String order_total_nums, String order_total_money, String order_dopackage, String order_name, String table_id, boolean open_goodnote, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo) {
        this.order_id = order_id;
        this.order_time = order_time;
        this.order_table_nums = order_table_nums;
        this.order_people_num = order_people_num;
        this.order_notes = order_notes;
        this.order_total_nums = order_total_nums;
        this.order_total_money = order_total_money;
        this.order_dopackage = order_dopackage;
        this.order_name = order_name;
        this.table_id = table_id;
        this.open_goodnote = open_goodnote;
        this.mSelf_Service_GoodsInfo = mSelf_Service_GoodsInfo;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_table_nums() {
        return order_table_nums;
    }

    public void setOrder_table_nums(String order_table_nums) {
        this.order_table_nums = order_table_nums;
    }

    public String getOrder_people_num() {
        return order_people_num;
    }

    public void setOrder_people_num(String order_people_num) {
        this.order_people_num = order_people_num;
    }

    public String getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(String order_notes) {
        this.order_notes = order_notes;
    }

    public String getOrder_total_nums() {
        return order_total_nums;
    }

    public void setOrder_total_nums(String order_total_nums) {
        this.order_total_nums = order_total_nums;
    }

    public String getOrder_total_money() {
        return order_total_money;
    }

    public void setOrder_total_money(String order_total_money) {
        this.order_total_money = order_total_money;
    }

    public String getOrder_dopackage() {
        return order_dopackage;
    }

    public void setOrder_dopackage(String order_dopackage) {
        this.order_dopackage = order_dopackage;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public boolean isOpen_goodnote() {
        return open_goodnote;
    }

    public void setOpen_goodnote(boolean open_goodnote) {
        this.open_goodnote = open_goodnote;
    }

    public ArrayList<Self_Service_GoodsInfo> getmSelf_Service_GoodsInfo() {
        return mSelf_Service_GoodsInfo;
    }

    public void setmSelf_Service_GoodsInfo(ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo) {
        this.mSelf_Service_GoodsInfo = mSelf_Service_GoodsInfo;
    }

    public static Creator<Res_GoodsOrders> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Res_GoodsOrders{" +
                "order_id='" + order_id + '\'' +
                ", order_time='" + order_time + '\'' +
                ", order_table_nums='" + order_table_nums + '\'' +
                ", order_people_num='" + order_people_num + '\'' +
                ", order_notes='" + order_notes + '\'' +
                ", order_total_nums='" + order_total_nums + '\'' +
                ", order_total_money='" + order_total_money + '\'' +
                ", order_dopackage='" + order_dopackage + '\'' +
                ", order_name='" + order_name + '\'' +
                ", table_id='" + table_id + '\'' +
                ", open_goodnote=" + open_goodnote +
                ", mSelf_Service_GoodsInfo=" + mSelf_Service_GoodsInfo +
                '}';
    }

    protected Res_GoodsOrders(Parcel in) {
        order_id = in.readString();
        order_time = in.readString();
        order_table_nums = in.readString();
        order_people_num = in.readString();
        order_notes = in.readString();
        order_total_nums = in.readString();
        order_total_money = in.readString();
        order_dopackage = in.readString();
        order_name = in.readString();
        table_id = in.readString();
        open_goodnote = in.readByte() != 0;
        mSelf_Service_GoodsInfo = in.createTypedArrayList(Self_Service_GoodsInfo.CREATOR);
    }

    public static final Creator<Res_GoodsOrders> CREATOR = new Creator<Res_GoodsOrders>() {
        @Override
        public Res_GoodsOrders createFromParcel(Parcel in) {
            return new Res_GoodsOrders(in);
        }

        @Override
        public Res_GoodsOrders[] newArray(int size) {
            return new Res_GoodsOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(order_time);
        dest.writeString(order_table_nums);
        dest.writeString(order_people_num);
        dest.writeString(order_notes);
        dest.writeString(order_total_nums);
        dest.writeString(order_total_money);
        dest.writeString(order_dopackage);
        dest.writeString(order_name);
        dest.writeString(table_id);
        dest.writeByte((byte) (open_goodnote ? 1 : 0));
        dest.writeTypedList(mSelf_Service_GoodsInfo);
    }
}