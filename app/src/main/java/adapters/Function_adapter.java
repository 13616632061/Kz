package adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Function_entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/31.
 */
public class Function_adapter extends BaseAdapter {

    public Context context;
    public List<Function_entty> adats;
    //判断是否是编辑状态
    public String type="0";
    public SetOnclickitme setOnclickitme;

    public Function_adapter setOnclickitme(SetOnclickitme setOnclickitme) {
        this.setOnclickitme = setOnclickitme;
        return Function_adapter.this;
    }

    public Function_adapter(Context context) {
        this.context=context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Function_entty> adats,String type){
        this.adats=adats;
        this.type=type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats==null?0:adats.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoed viewHoed;
        if (view != null) {
            viewHoed = (ViewHoed) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.function_itme, null);
            viewHoed = new ViewHoed();
            viewHoed.tv_function_name= (TextView) view.findViewById(R.id.tv_function_name);
            viewHoed.img_add= (ImageView) view.findViewById(R.id.img_add);
            viewHoed.img_show= (ImageView) view.findViewById(R.id.img_show);
            viewHoed.Rl_function= (RelativeLayout) view.findViewById(R.id.Rl_function);
            view.setTag(viewHoed);
        }

        if (type.equals("0")){
            viewHoed.img_add.setVisibility(View.GONE);
            viewHoed.img_show.setVisibility(View.GONE);
//            viewHoed.Rl_function.setBackgroundColor(Color.parseColor("#ffffff"));
        }else if (type.equals("1")){
//            viewHoed.Rl_function.setBackgroundColor(Color.parseColor("#fefefe"));
            viewHoed.img_add.setVisibility(View.VISIBLE);
            viewHoed.img_show.setVisibility(View.GONE);
            if (Boolean.parseBoolean(adats.get(i).getType())){
                viewHoed.img_add.setImageResource(R.drawable.reductionof);
            }else {
                viewHoed.img_add.setImageResource(R.drawable.add);
            }
        }else if (type.equals("2")){
            viewHoed.img_add.setVisibility(View.GONE);
            if (adats.get(i).getSecondary_sequence().equals("1")){
                viewHoed.img_show.setVisibility(View.VISIBLE);
            }else {
                viewHoed.img_show.setVisibility(View.GONE);
            }
        }
        Drawable top;
        switch (adats.get(i).getName()) {
            case "挂单":
                top = context.getResources().getDrawable(R.drawable.register);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "取单":
                top = context.getResources().getDrawable(R.drawable.withdrawal);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "赊账":
                top = context.getResources().getDrawable(R.drawable.credit);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "退货":
                top = context.getResources().getDrawable(R.drawable.return_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "兑奖":
                top = context.getResources().getDrawable(R.drawable.cashaprize);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "会员":
                top = context.getResources().getDrawable(R.drawable.vip_member);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "换钱":
                top = context.getResources().getDrawable(R.drawable.change);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "删行":
                top = context.getResources().getDrawable(R.drawable.delete_one);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "搜索":
                top = context.getResources().getDrawable(R.drawable.aabbcc);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "钱箱":
                top = context.getResources().getDrawable(R.drawable.cashbox);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "锁屏":
                top = context.getResources().getDrawable(R.drawable.lockscreen);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "登出":
                top = context.getResources().getDrawable(R.drawable.goout);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "同步":
                top = context.getResources().getDrawable(R.drawable.synchronization);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "会员价":
                top = context.getResources().getDrawable(R.drawable.vip_price);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "报表统计":
                top = context.getResources().getDrawable(R.drawable.report);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "商品管理":
                top = context.getResources().getDrawable(R.drawable.goods_manage);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "新增商品":
                top = context.getResources().getDrawable(R.drawable.new_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "出入库":
                top = context.getResources().getDrawable(R.drawable.out_in);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "盘点":
                top = context.getResources().getDrawable(R.drawable.lnventory);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "报损":
                top = context.getResources().getDrawable(R.drawable.loss);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "员工":
                top = context.getResources().getDrawable(R.drawable.staff);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "异常操作":
                top = context.getResources().getDrawable(R.drawable.back01);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "报货":
                top = context.getResources().getDrawable(R.drawable.up_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "会员管理":
                top = context.getResources().getDrawable(R.drawable.member_manage);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "设置":
                top = context.getResources().getDrawable(R.drawable.setup);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "标签":
                top = context.getResources().getDrawable(R.drawable.label_print);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "打印":
                top = context.getResources().getDrawable(R.drawable.print);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "消息":
                top = context.getResources().getDrawable(R.drawable.news);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "关于":
                top = context.getResources().getDrawable(R.drawable.about);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "备用金":
                top = context.getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "改价":
                top = context.getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "计算器":
                top = context.getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
            case "旧版":
                top = context.getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                viewHoed.tv_function_name.setCompoundDrawables(null, top, null, null);
                viewHoed.tv_function_name.setText(adats.get(i).getName());
                break;
        }
        viewHoed.Rl_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclickitme.Onclickitme(i,type);
            }
        });


        return view;
    }

    private class ViewHoed {
        TextView tv_function_name;
        ImageView img_add,img_show;
        RelativeLayout Rl_function;
    }


   public interface SetOnclickitme{
        void Onclickitme(int i,String type);
    }

}
