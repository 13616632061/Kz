package Entty;

/**
 * Created by Administrator on 2017/8/12.
 * 餐桌
 */

public class Res_Table {
    String res_table_sort_id;
    String res_table_sort_name;
    String res_table_id;
    String res_table_name;
    String res_table_people_nums;//餐桌最多人数
    String res_table_notes;//餐桌备注
    String res_table_status;//餐桌状态
    String reserve_time;//预定的时间

    public Res_Table(String res_table_sort_id, String res_table_sort_name, String res_table_id, String res_table_name, String res_table_people_nums, String res_table_notes, String res_table_status,String reserve_time) {
        this.res_table_sort_id = res_table_sort_id;
        this.res_table_sort_name = res_table_sort_name;
        this.res_table_id = res_table_id;
        this.res_table_name = res_table_name;
        this.res_table_people_nums = res_table_people_nums;
        this.res_table_notes = res_table_notes;
        this.res_table_status = res_table_status;
        this.reserve_time = reserve_time;
    }


    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getRes_table_sort_id() {
        return res_table_sort_id;
    }

    public void setRes_table_sort_id(String res_table_sort_id) {
        this.res_table_sort_id = res_table_sort_id;
    }

    public String getRes_table_sort_name() {
        return res_table_sort_name;
    }

    public void setRes_table_sort_name(String res_table_sort_name) {
        this.res_table_sort_name = res_table_sort_name;
    }

    public String getRes_table_id() {
        return res_table_id;
    }

    public void setRes_table_id(String res_table_id) {
        this.res_table_id = res_table_id;
    }

    public String getRes_table_name() {
        return res_table_name;
    }

    public void setRes_table_name(String res_table_name) {
        this.res_table_name = res_table_name;
    }

    public String getRes_table_people_nums() {
        return res_table_people_nums;
    }

    public void setRes_table_people_nums(String res_table_people_nums) {
        this.res_table_people_nums = res_table_people_nums;
    }

    public String getRes_table_notes() {
        return res_table_notes;
    }

    public void setRes_table_notes(String res_table_notes) {
        this.res_table_notes = res_table_notes;
    }

    public String getRes_table_status() {
        return res_table_status;
    }

    public void setRes_table_status(String res_table_status) {
        this.res_table_status = res_table_status;
    }

}
