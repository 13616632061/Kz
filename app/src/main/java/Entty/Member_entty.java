package Entty;

import java.io.Serializable;

/**
 * Created by admin on 2017/7/27.
 */
public class Member_entty implements Serializable {


    /**
     * member_name : yyy
     * mobile : 77
     * discount_rate : 6666
     * score : 666
     */

    private String member_name;
    private String mobile;
    private String discount_rate;
    private String score;
    private String time;
    private String member_id;
    private String surplus;
    private String pwd;
    private String birthday;
    private String remark;
    private String is_require_pass;
    private String member_lv_custom_id;
    private String member_lv_custom_key;
    private String member_lv_custom_name;


    public String getMember_lv_custom_id() {
        return member_lv_custom_id;
    }

    public void setMember_lv_custom_id(String member_lv_custom_id) {
        this.member_lv_custom_id = member_lv_custom_id;
    }

    public String getMember_lv_custom_key() {
        return member_lv_custom_key;
    }

    public void setMember_lv_custom_key(String member_lv_custom_key) {
        this.member_lv_custom_key = member_lv_custom_key;
    }

    public String getMember_lv_custom_name() {
        return member_lv_custom_name;
    }

    public void setMember_lv_custom_name(String member_lv_custom_name) {
        this.member_lv_custom_name = member_lv_custom_name;
    }

    public String getIs_require_pass() {
        return is_require_pass;
    }

    public void setIs_require_pass(String is_require_pass) {
        this.is_require_pass = is_require_pass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
