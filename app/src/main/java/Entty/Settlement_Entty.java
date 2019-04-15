package Entty;

/**
 * Created by admin on 2017/8/7.
 */
public class Settlement_Entty {


    /**
     * goods_num : 24
     * total_amount : 158.000
     * provider_name : 九个月
     * contact :
     * phone :
     * provider_id : 28
     * report_id : 161
     * report_order : 170804171362640
     * addtime : 1501838023
     * cashier_id : 0
     * order_status : 0
     * seller_name : 测试商家1
     */

    private String goods_num;
    private String total_amount;
    private String provider_name;
    private String contact;
    private String phone;
    private String provider_id;
    private String report_id;
    private String report_order;
    private String addtime;
    private String cashier_id;
    private String order_status;
    private String seller_name;
    private boolean reconciliation;

    public boolean isReconciliation() {
        return reconciliation;
    }

    public void setReconciliation(boolean reconciliation) {
        this.reconciliation = reconciliation;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_order() {
        return report_order;
    }

    public void setReport_order(String report_order) {
        this.report_order = report_order;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(String cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
}
