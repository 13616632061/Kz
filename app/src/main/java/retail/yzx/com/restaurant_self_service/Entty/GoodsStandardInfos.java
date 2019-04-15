package retail.yzx.com.restaurant_self_service.Entty;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/18.
 */

public class GoodsStandardInfos {
    String goods_standard;//商品规格型号
    ArrayList<GoodsStandard> mGoodsStandardList;//具体的规格

    public GoodsStandardInfos(String goods_standard, ArrayList<GoodsStandard> mGoodsStandardList) {
        this.goods_standard = goods_standard;
        this.mGoodsStandardList = mGoodsStandardList;
    }

    public String getGoods_standard() {
        return goods_standard;
    }

    public void setGoods_standard(String goods_standard) {
        this.goods_standard = goods_standard;
    }

    public ArrayList<GoodsStandard> getmGoodsStandardList() {
        return mGoodsStandardList;
    }

    public void setmGoodsStandardList(ArrayList<GoodsStandard> mGoodsStandardList) {
        this.mGoodsStandardList = mGoodsStandardList;
    }

    @Override
    public String toString() {
        return "GoodsStandardInfos{" +
                "goods_standard='" + goods_standard + '\'' +
                ", mGoodsStandardList=" + mGoodsStandardList +
                '}';
    }
}
