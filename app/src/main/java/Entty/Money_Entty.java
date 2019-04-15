package Entty;

/**
 * Created by admin on 2017/6/26.
 * 报货详情实体类
 */
public class Money_Entty {


    /**
     * report_id : 87
     * total_amount : 64.000
     * seller_name : 山东蓝翔
     * cashier : 山东蓝翔
     * addtime : 1498462579
     */

    private String report_id;
    private String total_amount;
    private String seller_name;
    private String addtime;
    private String cashier;
    private String nums;
    private String status;

    //审核
    private String is_verify;


    public String getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
