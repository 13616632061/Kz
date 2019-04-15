package Entty;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/22.
 */
public class Staff_entty implements Serializable{


    /**
     * work_id : 4
     * role_id : null
     * seller_id : 56
     * rate : null
     * login_name : 刚刚好
     * phone : 15932966457
     * bn : 7986
     * disable : false
     * createtime : 1495448438
     * login_pass : s18cf6de3d0d1f0bee0136745d470c43
     */

    private String cost_disable;
    private String store_disable;
    private String profit_disable;
    private String work_id;
    private String role_id;
    private String seller_id;
    private String rate;
    private String login_name;
    private String phone;
    private String bn;
    private String disable;
    private String createtime;
    private String login_pass;

    public String getCost_disable() {
        return cost_disable;
    }

    public void setCost_disable(String cost_disable) {
        this.cost_disable = cost_disable;
    }

    public String getStore_disable() {
        return store_disable;
    }

    public void setStore_disable(String store_disable) {
        this.store_disable = store_disable;
    }

    public String getProfit_disable() {
        return profit_disable;
    }

    public void setProfit_disable(String profit_disable) {
        this.profit_disable = profit_disable;
    }

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public Object getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getLogin_pass() {
        return login_pass;
    }

    public void setLogin_pass(String login_pass) {
        this.login_pass = login_pass;
    }
}
