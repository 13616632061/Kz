package retail.yzx.com.restaurant_self_service.Entty;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/19.
 */

public class GoodsSortInfos {
    private String sort_id;//商品分类id
    private  String sort_name;//分类名称
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;

    public GoodsSortInfos(String sort_id, String sort_name, ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList) {
        this.sort_id = sort_id;
        this.sort_name = sort_name;
        this.mSelf_Service_Restanraunt_GoodsInfoList = mSelf_Service_Restanraunt_GoodsInfoList;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public ArrayList<Self_Service_RestanrauntGoodsInfo> getmSelf_Service_Restanraunt_GoodsInfoList() {
        return mSelf_Service_Restanraunt_GoodsInfoList;
    }

    public void setmSelf_Service_Restanraunt_GoodsInfoList(ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList) {
        this.mSelf_Service_Restanraunt_GoodsInfoList = mSelf_Service_Restanraunt_GoodsInfoList;
    }

    @Override
    public String toString() {
        return "GoodsSortInfos{" +
                "sort_id='" + sort_id + '\'' +
                ", sort_name='" + sort_name + '\'' +
                ", mSelf_Service_Restanraunt_GoodsInfoList=" + mSelf_Service_Restanraunt_GoodsInfoList +
                '}';
    }
}
