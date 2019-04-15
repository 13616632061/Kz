package Entty;

/**
 * Created by admin on 2017/8/21.
 */
public class Integral_Entty {


    /**
     * nums : 0
     * score : 6666
     * name : D
     * price : 1.000
     * bncode : 489338393
     */

    private String nums;
    private String score;
    private String name;
    private String price;
    private String bncode;
    private String swap_goods_id;
    private boolean elect;
    private String goods_id;


    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public boolean isElect() {
        return elect;
    }

    public void setElect(boolean elect) {
        this.elect = elect;
    }

    public String getSwap_goods_id() {
        return swap_goods_id;
    }

    public void setSwap_goods_id(String swap_goods_id) {
        this.swap_goods_id = swap_goods_id;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getBncode() {
        return bncode;
    }

    public void setBncode(String bncode) {
        this.bncode = bncode;
    }


}
