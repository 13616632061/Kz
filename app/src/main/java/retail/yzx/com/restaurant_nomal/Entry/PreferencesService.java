package retail.yzx.com.restaurant_nomal.Entry;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/30.
 */

public class PreferencesService {

    private Context context;

    public PreferencesService(Context context) {
        this.context = context;
    }

    public void setGoodsIsClick(boolean icClick){
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("Goods", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("icClick",icClick);
        editor.commit();
    }
    /**
     * 获取商品是否可以被点击的状态
     * @return
     */
    public Map<String,String> getGoodsIsClick(){
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("Goods", Context.MODE_PRIVATE);
        params.put("icClick", String.valueOf(preferences.getBoolean("icClick",true)));
        return params;
    }
}
