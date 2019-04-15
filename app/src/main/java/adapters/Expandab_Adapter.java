package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Group_Entty;
import Utils.DateUtils;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/14.
 */
public class Expandab_Adapter extends BaseExpandableListAdapter {
    public List<Group_Entty> datas;
    public Context context;
    public SetOnclick setOnclick;

    public Expandab_Adapter SetListenerOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Expandab_Adapter.this;
    }

    public Expandab_Adapter(Context context) {
        this.context = context;
        this.datas=new ArrayList<>();
    }

    public void setDatas(List<Group_Entty> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }


    //  获得父项的数量  
    @Override
    public int getGroupCount() {
        return datas.size();
    }
    //  获得某个父项的子项数目  
    @Override
    public int getChildrenCount(int i) {
        return datas.get(i).getChild().size();
    }
    //  获得某个父项  
    @Override
    public Object getGroup(int i) {
        return datas.get(i);
    }
    //  获得某个父项的某个子项  
    @Override
    public Object getChild(int i, int i1) {
        return datas.get(i).getChild().get(i1);
    }
    //  获得某个父项的id  
    @Override
    public long getGroupId(int i) {
        return i;
    }
    //  获得某个父项的某个子项的id  
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过  
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //  获得父项显示的view  
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder=null;
        if (view!=null){
            groupHolder= (GroupHolder) view.getTag();
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.money_details_layout,null);
            groupHolder=new GroupHolder();
            groupHolder.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            groupHolder.tv_ures_name= (TextView) view.findViewById(R.id.tv_ures_name);
            groupHolder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            groupHolder.tv_time= (TextView) view.findViewById(R.id.tv_time);
            groupHolder.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            groupHolder.tv_price= (TextView) view.findViewById(R.id.tv_price);
            groupHolder.ll= (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(groupHolder);
        }
        groupHolder.tv_xuhao.setText((i+1)+"");
        groupHolder.tv_name.setText(datas.get(i).getCashier());
        groupHolder.tv_ures_name.setText(datas.get(i).getSeller_name());
        groupHolder.tv_time.setText(DateUtils.getDateTimeFromMillisecond(1000*Long.parseLong(datas.get(i).getAddtime())));
        groupHolder.tv_price.setText(StringUtils.stringpointtwo(datas.get(i).getTotal_amount()));
        groupHolder.tv_nums.setText(datas.get(i).getChild().size()+"");
        if (b){
            groupHolder.ll.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }else {
            groupHolder.ll.setBackgroundColor(Color.parseColor("#D7D7D7"));
        }
        return view;
    }
    //  获得子项显示的view  
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view1, ViewGroup viewGroup) {
       final ChildHolder childHolder;
        if (view1!=null){
            childHolder= (ChildHolder) view1.getTag();
        }else {
            childHolder=new ChildHolder();
            view1=LayoutInflater.from(context).inflate(R.layout.money_layout,null);
            childHolder.tv_xuhao= (TextView) view1.findViewById(R.id.tv_xuhao);
            childHolder.tv_name= (TextView) view1.findViewById(R.id.tv_name);
            childHolder.tv_code= (TextView) view1.findViewById(R.id.tv_code);
            childHolder.tv_number= (TextView) view1.findViewById(R.id.tv_number);
            childHolder.tv_price= (TextView) view1.findViewById(R.id.tv_price);
            childHolder.cb_box= (CheckBox) view1.findViewById(R.id.cb_box);
            childHolder.ll_Child= (LinearLayout) view1.findViewById(R.id.ll_Child);
            childHolder.but_dispose= (Button) view1.findViewById(R.id.but_dispose);
            view1.setTag(childHolder);
        }

        childHolder.tv_xuhao.setText((i1+1)+"");
        childHolder.tv_name.setText(datas.get(i).getChild().get(i1).getGoods_name());
        childHolder.tv_code.setText(datas.get(i).getChild().get(i1).getBncode());
        childHolder.tv_number.setText(datas.get(i).getChild().get(i1).getNums()+"");
        childHolder.tv_price.setText(StringUtils.stringpointtwo(datas.get(i).getChild().get(i1).getPrice()));
        childHolder.cb_box.setVisibility(View.GONE);
        if (datas.get(i).getChild().get(i1).getGoods_status().equals("0")){
            childHolder.but_dispose.setText("未处理");
        }else {
            childHolder.but_dispose.setText("已处理");
        }
        childHolder.but_dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datas.get(i).getChild().get(i1).getGoods_status().equals("0")){
                    setOnclick.SetOnclickChild(i,i1);
                    childHolder.but_dispose.setText("未处理");
                }else {
                    childHolder.but_dispose.setText("已处理");
                }

            }
        });
        return view1;
    }
    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true  
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class GroupHolder{
        public TextView tv_xuhao,tv_ures_name,tv_name,tv_time,tv_nums,tv_price;

        LinearLayout ll;
    }
    class ChildHolder{
        public TextView tv_xuhao,tv_name,tv_code,tv_number,tv_price;
        public CheckBox cb_box;
        Button but_dispose;
        public LinearLayout ll_Child;
    }

    //选中的接口回掉
    public interface SetOnclick {
        void SetOnclickChild(int i, int i1);
    }

}
