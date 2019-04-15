package Entty;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/12.
 * 餐桌分类
 */

public class Res_Table_Sort {
    String res_table_sort_id;
    String res_table_sort_name;
    boolean res_table_sort_click;
    boolean res_table_sort_edit;
    ArrayList<Res_Table> mRes_TableList;

    public Res_Table_Sort(String res_table_sort_id, String res_table_sort_name, boolean res_table_sort_click, boolean res_table_sort_edit, ArrayList<Res_Table> mRes_TableList) {
        this.res_table_sort_id = res_table_sort_id;
        this.res_table_sort_name = res_table_sort_name;
        this.res_table_sort_click = res_table_sort_click;
        this.res_table_sort_edit = res_table_sort_edit;
        this.mRes_TableList = mRes_TableList;
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

    public boolean isRes_table_sort_click() {
        return res_table_sort_click;
    }

    public void setRes_table_sort_click(boolean res_table_sort_click) {
        this.res_table_sort_click = res_table_sort_click;
    }

    public boolean isRes_table_sort_edit() {
        return res_table_sort_edit;
    }

    public void setRes_table_sort_edit(boolean res_table_sort_edit) {
        this.res_table_sort_edit = res_table_sort_edit;
    }

    public ArrayList<Res_Table> getmRes_TableList() {
        return mRes_TableList;
    }

    public void setmRes_TableList(ArrayList<Res_Table> mRes_TableList) {
        this.mRes_TableList = mRes_TableList;
    }


}
