package Entty;

import java.util.List;

/**
 * Created by admin on 2019/3/7.
 * 临时订单
 */

public class Temporary_Entty {
    String id;
    String goods_name;
    String bncode;
    String createtime;
    String total;
    List<Goods_info> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getBncode() {
        return bncode;
    }

    public void setBncode(String bncode) {
        this.bncode = bncode;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Goods_info> getData() {
        return data;
    }

    public void setData(List<Goods_info> data) {
        this.data = data;
    }

    public static class Goods_info {
        String name;
        String goods_id;
        String num;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
