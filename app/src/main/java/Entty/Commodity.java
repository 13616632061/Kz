package Entty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/4/7.
 * Comparator
 * Comparable
 */
public class Commodity implements Serializable {



    //供应商
    private String provider_id;
    private String provider_name;

    private String specification;
//    商品id
    private String goods_id;
    private String name;

    private String product_id;
    //拼音
    private String py;

    private String seller_id;
//    价格
    private String price;
//    进价
    private String cost;
    //条码
    private String bncode;
    //编码
    private String bn;
//    分类id
    private String tag_id;
//    单位
    private String unit;
    private int unit_id;
//    库存
    private String store;
//    库存上限
    private String good_limit;
//    库存下限
    private String good_stock;
//    生产日期
    private String PD;
    //    到期日期
    private String GD;
    //上下架
    private String marketable;
//    分类名
    private String tag_name;
//    是否在快捷栏
    public String altc;
//    快捷栏的信息
    public int position;
    public String good_remark;
//    煮物栏
    public String cook_position;

//    是否拆箱

    public boolean box_disable;

    //判断是否为开放状态
    public String auth;


    public String member_price;

    //是否是特价商品
    public String is_special_offer;


    public String discount;

    //不同等级的会员价的
    public String custom_member_price;

    //判断是选取哪个会员价的值
    public String type;

    //产地
    public String produce_addr;

    //判断是不是临时产品
    public String intro;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getProduce_addr() {
        return produce_addr;
    }

    public void setProduce_addr(String produce_addr) {
        this.produce_addr = produce_addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustom_member_price() {
        return custom_member_price;
    }

    public void setCustom_member_price(String custom_member_price) {
        this.custom_member_price = custom_member_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getIs_special_offer() {
        return is_special_offer;
    }

    public void setIs_special_offer(String is_special_offer) {
        this.is_special_offer = is_special_offer;
    }

    public String getMember_price() {
        return member_price;
    }

    public void setMember_price(String member_price) {
        this.member_price = member_price;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isBox_disable() {
        return box_disable;
    }

    public void setBox_disable(boolean box_disable) {
        this.box_disable = box_disable;
    }

    public String getCook_position() {
        return cook_position;
    }

    public void setCook_position(String cook_position) {
        this.cook_position = cook_position;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Labels> adats;

    public List<Labels> getAdats() {
        return adats;
    }

    public void setAdats(List<Labels> adats) {
        this.adats = adats;
    }




    public static class Labels implements Serializable{
       public String label_id;
        public String label_name;

        public String getLabel_id() {
            return label_id;
        }

        public void setLabel_id(String label_id) {
            this.label_id = label_id;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }

    }

    public String getGood_remark() {
        return good_remark;
    }

    public void setGood_remark(String good_remark) {
        this.good_remark = good_remark;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAltc() {
        return altc;
    }

    public void setAltc(String altc) {
        this.altc = altc;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
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

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBncode() {
        return bncode;
    }

    public void setBncode(String bncode) {
        this.bncode = bncode;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getGood_limit() {
        return good_limit;
    }

    public void setGood_limit(String good_limit) {
        this.good_limit = good_limit;
    }

    public String getGood_stock() {
        return good_stock;
    }

    public void setGood_stock(String good_stock) {
        this.good_stock = good_stock;
    }

    public String getPD() {
        return PD;
    }

    public void setPD(String PD) {
        this.PD = PD;
    }

    public String getGD() {
        return GD;
    }

    public void setGD(String GD) {
        this.GD = GD;
    }

    public String getMarketable() {
        return marketable;
    }

    public void setMarketable(String marketable) {
        this.marketable = marketable;
    }

}
