package shujudb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entty.Commodity;
import Entty.Function_entty;
import Entty.Message_Beans2;
import Entty.New_NumberEntty;
import Entty.ShuliangEntty;
import Utils.DateUtils;
import Utils.StringUtils;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by admin on 2018/7/7.
 */

public class Sqlite_Entity {
    SqliteHelper sqliteHelper;
    public Sqlite_Entity(Context context) {
        sqliteHelper=new SqliteHelper(context);
    }

    public void add(Commodity commodity,String goods_id){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("goods_id", commodity.getGoods_id());
        values.put("name", commodity.getName());
        values.put("py", commodity.getPy());
        values.put("price", commodity.getPrice());
        values.put("cost", commodity.getCost());
        values.put("store", commodity.getStore());
        values.put("bncode", commodity.getBncode());
        values.put("tag_id", commodity.getTag_id());
        values.put("unit", commodity.getUnit());
        values.put("good_limit", commodity.getGood_limit());
        values.put("good_stock", commodity.getGood_stock());
        values.put("PD", commodity.getPD());
        values.put("GD", commodity.getGD());
        values.put("marketable", commodity.getMarketable());
        values.put("tag_name", commodity.getTag_name());
        values.put("ALTC", commodity.getAltc());
        values.put("product_id", commodity.getProduct_id());
        values.put("bn", commodity.getBn());
        values.put("provider_id", commodity.getProvider_id());
        values.put("provider_name", commodity.getProvider_name());
        values.put("cook_position",commodity.getCook_position());
        values.put("member_price",commodity.getMember_price());
        sqLiteDatabases.insert("commodity",null,values);
        values.clear();
    }

    /**
     * 添加挂单的商品便利版本
     * @param commodity 挂单的商品
     * @param order_id 挂单的订单号
     * @param number 挂单的商品数量
     */
    public void addguadan(List<Commodity> commodity, String order_id, List<ShuliangEntty> number, String good_size, String goods_notes){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        String  names="";
        float total_amount = 0;
        for (int i=0;i<commodity.size();i++){
            values.put("order_id", order_id);
            if (commodity.size()-1==i){
                names=names+commodity.get(i).getName();
            }else {
                names=names+commodity.get(i).getName()+",";
            }
            total_amount += (Float.parseFloat(commodity.get(i).getPrice()) * number.get(i).getNumber());
            values.put("goods_id", commodity.get(i).getGoods_id());
            values.put("name", commodity.get(i).getName());
            values.put("py", commodity.get(i).getPy());
            values.put("price", commodity.get(i).getPrice());
            values.put("cost", commodity.get(i).getCost());
            values.put("store", commodity.get(i).getStore());
            values.put("bncode", commodity.get(i).getBncode());
            values.put("marketable", commodity.get(i).getMarketable());
            values.put("ALTC", commodity.get(i).getAltc());
            values.put("product_id", commodity.get(i).getProduct_id());
            values.put("bn", commodity.get(i).getBn());
            values.put("provider_id", commodity.get(i).getProvider_id());
            values.put("provider_name", commodity.get(i).getProvider_name());
            values.put("cook_position",commodity.get(i).getCook_position());
            values.put("member_price",commodity.get(i).getMember_price());
            values.put("is_special_offer",commodity.get(i).getIs_special_offer());
            values.put("number",number.get(i).getNumber());
            values.put("good_size",good_size);
            values.put("goods_notes",goods_notes);
            sqLiteDatabases.insert("hangupgoods",null,values);
        }
        String substring="";
        if (names.length()>12){
            substring = names.substring(0, 12);
        }else {
            substring=names;
        }
        long time=Long.parseLong(DateUtils.getTime());
        addguadanorder("false","",total_amount+"",time,order_id,substring+"...","","","");
        values.clear();
    }

    /**
     * 添加挂单的商品餐饮版本
     * @param commodity 挂单的商品
     * @param order_id 挂单的订单号
     */
    public void addguadancanyin(ArrayList<Self_Service_GoodsInfo> commodity, String order_id, String package1, String table_name, String table_id, long time,String customer_num){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        String  names="";
        float total_amount = 0;
        for (int i=0;i<commodity.size();i++){
            values.put("order_id", order_id);
            if (commodity.size()-1==i){
                names=names+commodity.get(i).getName();
            }else {
                names=names+commodity.get(i).getName()+",";
            }
            total_amount += (Float.parseFloat(commodity.get(i).getPrice()) * Float.parseFloat(commodity.get(i).getNumber()));
            values.put("goods_id", commodity.get(i).getGoods_id());
            values.put("name", commodity.get(i).getName());
            values.put("price", commodity.get(i).getPrice());
            if (!commodity.get(i).getCost().equals("")){
                values.put("cost", commodity.get(i).getCost());
            }else {
                values.put("cost", 0);
            }
            values.put("marketable", commodity.get(i).getNotes());
            values.put("product_id", commodity.get(i).getProduct_id());
            values.put("number",commodity.get(i).getNumber());
            values.put("good_size",commodity.get(i).getSize());
            values.put("goods_notes",commodity.get(i).getNotes());
            values.put("cook_position","");
            values.put("member_price","");
            values.put("is_special_offer","no");
            values.put("bncode",commodity.get(i).getGoods_id());
            values.put("store","");
            values.put("tag_id",commodity.get(i).getTag_id());
            sqLiteDatabases.insert("hangupgoods",null,values);
        }
        String substring="";
        if (names.length()>12){
            substring = names.substring(0, 12);
        }else {
            substring=names;
        }
        addguadanorder(package1,customer_num,total_amount+"", time,order_id,substring+"...","",table_name,table_id);
        values.clear();
    }

    /**
     * 添加挂单的订单
     * @param order_id 订单id
     * @param name 订单商品名字
     * @param remarks 订单备注
     * @param table_name 桌号名字
     * @param table_id 桌号id
     */
    public void addguadanorder(String package1,String customer_num,String total_amount,long createtime,String order_id,String name,String remarks,String table_name,String table_id){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("package", package1);
        values.put("customer_num", customer_num);
        values.put("total_amount", total_amount);
        values.put("createtime", createtime);
        values.put("order_id", order_id);
        values.put("name", name);
        values.put("remarks", remarks);
        values.put("table_name", table_name);
        values.put("table_id", table_id);
        sqLiteDatabases.insert("hangup",null,values);
        values.clear();
    }

    /**
     * 查询订单挂单的数据
     * @return
     */
    public String queryguadanorder(){
        SQLiteDatabase database=sqliteHelper.getReadableDatabase();
        Cursor cursor=database.query("hangup",null,null,null,null,null,null,null);
        List<Map<String,String>> mapList=new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String,String> map=new HashMap<>();
            map.put("order_id",cursor.getString(cursor.getColumnIndex("order_id")));
            map.put("name",cursor.getString(cursor.getColumnIndex("name")));
            map.put("remarks",cursor.getString(cursor.getColumnIndex("remarks")));
            map.put("table_name",cursor.getString(cursor.getColumnIndex("table_name")));
            map.put("table_id",cursor.getString(cursor.getColumnIndex("table_id")));
            map.put("createtime",cursor.getString(cursor.getColumnIndex("createtime")));
            map.put("package",cursor.getString(cursor.getColumnIndex("package")));
            map.put("customer_num",cursor.getString(cursor.getColumnIndex("customer_num")));
            map.put("total_amount",cursor.getString(cursor.getColumnIndex("total_amount")));
            mapList.add(map);
        }
        Gson gson=new Gson();
        return gson.toJson(mapList);
    }

    /**
     * 查询挂单的商品
     * @param order_id 挂单的订单id
     * @return
     */
    public String queryguadangoods(String order_id){
        List<Map<String,String>> mapList=new ArrayList<>();
        String str="";
        SQLiteDatabase sqLiteDatabases = sqliteHelper.getReadableDatabase();//查询是Readable，其余是Writable
        Cursor cursor = sqLiteDatabases.query("hangupgoods", null, "order_id = ?", new String[]{order_id}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Map<String,String> map=new HashMap<>();
                map.put("goods_id",cursor.getString(cursor.getColumnIndex("goods_id")));
                map.put("name",cursor.getString(cursor.getColumnIndex("name")));
                map.put("py",cursor.getString(cursor.getColumnIndex("py")));
                map.put("price",cursor.getString(cursor.getColumnIndex("price")));
                map.put("cost",cursor.getString(cursor.getColumnIndex("cost")));
                map.put("store",cursor.getString(cursor.getColumnIndex("store")));
                map.put("bncode",cursor.getString(cursor.getColumnIndex("bncode")));
                if (cursor.getString(cursor.getColumnIndex("marketable"))!=null){
                    map.put("marketable",cursor.getString(cursor.getColumnIndex("marketable")));
                }else {
                    map.put("marketable","");
                }
                map.put("ALTC",cursor.getString(cursor.getColumnIndex("ALTC")));
                if (cursor.getString(cursor.getColumnIndex("product_id"))!=null){
                    map.put("product_id",cursor.getString(cursor.getColumnIndex("product_id")));
                }else {
                    map.put("product_id","");
                }
                map.put("bn",cursor.getString(cursor.getColumnIndex("bn")));
                map.put("provider_id",cursor.getString(cursor.getColumnIndex("provider_id")));
                map.put("provider_name",cursor.getString(cursor.getColumnIndex("provider_name")));
                map.put("cook_position",cursor.getString(cursor.getColumnIndex("cook_position")));
                map.put("member_price",cursor.getString(cursor.getColumnIndex("member_price")));
                map.put("is_special_offer",cursor.getString(cursor.getColumnIndex("is_special_offer")));
                map.put("number",cursor.getString(cursor.getColumnIndex("number")));
                map.put("goods_size",cursor.getString(cursor.getColumnIndex("good_size")));
                map.put("tag_id",cursor.getString(cursor.getColumnIndex("tag_id")));
                mapList.add(map);
            }
            Gson gson=new Gson();
            str=gson.toJson(mapList);
        }

        return str;
    }

    /**
     * 查询挂单的订单
     * @param table_id 挂单的桌号id
     * @return
     */
    public String queryguadanorder(String table_id){
        List<Map<String,String>> mapList=new ArrayList<>();
        String str="";
        SQLiteDatabase sqLiteDatabases = sqliteHelper.getReadableDatabase();//查询是Readable，其余是Writable
        Cursor cursor = sqLiteDatabases.query("hangup", null, "table_id = ?", new String[]{table_id}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Map<String,String> map=new HashMap<>();
                map.put("order_id",cursor.getString(cursor.getColumnIndex("order_id")));
                map.put("name",cursor.getString(cursor.getColumnIndex("name")));
                map.put("remarks",cursor.getString(cursor.getColumnIndex("remarks")));
                map.put("table_name",cursor.getString(cursor.getColumnIndex("table_name")));
                map.put("table_id",cursor.getString(cursor.getColumnIndex("table_id")));
                map.put("createtime",cursor.getString(cursor.getColumnIndex("createtime")));
                map.put("package",cursor.getString(cursor.getColumnIndex("package")));
                map.put("customer_num",cursor.getString(cursor.getColumnIndex("customer_num")));
                map.put("total_amount",cursor.getString(cursor.getColumnIndex("total_amount")));
                mapList.add(map);
            }
            Gson gson=new Gson();
            str=gson.toJson(mapList);
        }

        return str;
    }

    /**
     * 删除挂单商品
     * @return
     */
    public int deleteguadan(String order_id){
        SQLiteDatabase database=sqliteHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.execSQL("DELETE FROM hangupgoods WHERE order_id = ?",new String[]{order_id});
            database.execSQL("DELETE FROM hangup WHERE order_id = ?",new String[]{order_id});
            database.setTransactionSuccessful();
        }catch (Exception e){
            return 0;
        }finally {
            database.endTransaction();
        }
        return 1;
    }

    /**
     * 修改挂单字段
     * @param field 挂单的字段名字
     * @param context 字段内容
     * @param tablename 表名
     * @param order_id 限定条件
     */
    public void modifyguadan(String field,String context,String tablename,String order_id){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(field, context);
        sqLiteDatabases.update(tablename, values, "order_id = ?", new String[]{order_id});
        values.clear();
    }


    public void deleteexls(){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        sqLiteDatabase.execSQL(("delete from  exls"));
    }

    public void update(Commodity commodity,String goods_id){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name", commodity.getName());
        values.put("price", commodity.getPrice());
        values.put("cost", commodity.getCost());
        values.put("store", commodity.getStore());
        values.put("bncode", commodity.getBncode());
        values.put("provider_id", commodity.getProvider_id());
        values.put("cook_position",commodity.getCook_position());
        values.put("member_price",commodity.getMember_price());
        sqLiteDatabases.update("commodity", values, "goods_id=?", new String[]{goods_id});
        values.clear();
    }

    public List<Commodity> query(String name,String goods_id) {
        List<Commodity> commodityList = null;
        SQLiteDatabase sqLiteDatabases = sqliteHelper.getReadableDatabase();//查询是Readable，其余是Writable
        Cursor cursor = sqLiteDatabases.query("commodity", null, name + "=?", new String[]{goods_id}, null, null, null);
        if (cursor != null) {
            commodityList = new ArrayList<>();
            while (cursor.moveToNext()) {
                Commodity commodity = new Commodity();
                commodity.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                commodity.setName(cursor.getString(cursor.getColumnIndex("name")));
                commodity.setPy(cursor.getString(cursor.getColumnIndex("py")));
                commodity.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                commodity.setCost(cursor.getString(cursor.getColumnIndex("cost")));
                commodity.setStore(cursor.getString(cursor.getColumnIndex("store")));
                commodity.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
                commodity.setTag_id(cursor.getString(cursor.getColumnIndex("tag_id")));
                commodity.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
                commodity.setGood_limit(cursor.getString(cursor.getColumnIndex("good_limit")));
                commodity.setGood_stock(cursor.getString(cursor.getColumnIndex("good_stock")));
                commodity.setPD(cursor.getString(cursor.getColumnIndex("PD")));
                commodity.setGD(cursor.getString(cursor.getColumnIndex("GD")));
                commodity.setMarketable(cursor.getString(cursor.getColumnIndex("marketable")));
                commodity.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
                commodity.setAltc(cursor.getString(cursor.getColumnIndex("ALTC")));
                commodity.setProduct_id(cursor.getString(cursor.getColumnIndex("product_id")));
                commodity.setBn(cursor.getString(cursor.getColumnIndex("bn")));
                commodity.setProvider_id(cursor.getString(cursor.getColumnIndex("provider_id")));
                commodity.setProvider_name(cursor.getString(cursor.getColumnIndex("provider_name")));
                commodity.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
                commodity.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                commodityList.add(commodity);
            }
        }else {
            return null;
        }
        cursor.close();
        return commodityList;
    }


    public boolean insertMsg(String time,String content,String state){
        SQLiteDatabase db=sqliteHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("time",time);
        values.put("content",content);
        values.put("state",state);
        long insert=db.insert("messages",null,values);
        if(insert>0){
            return true;
        }
        return false;
    }

    /**
     * 备份数据库
     * @param table
     * @return
     */
    public long findMaxId(String table) {
        // TODO Auto-generated method stub
        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
//Cursor cursor = database.query(table, null, null, null, null, null, " _id DESC");
        Cursor cursor= database.rawQuery("select count(2) from "+table,null);
        // cursor.getCount();
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;

    }

    /**
     * 查询本地的商品是否已经上传
     * @return
     */
    public String QueryOrder(){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        int number = 0, number2 = 0;
        Cursor c = null, c2 = null;
        if (c == null && c2 == null) {
            sqLiteDatabase = sqliteHelper.getReadableDatabase();
            c = sqLiteDatabase.rawQuery("select * from ProOut", null);
            c2 = sqLiteDatabase.rawQuery("select * from goodsSell", null);
        }
        number = c.getCount();
        number2 = c2.getCount();
        if (number != 0 && number2 != 0) {
            List<Map<String, Object>> List = new ArrayList<>();
            while (c.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                String cash_id = c.getString(c.getColumnIndex("cash_id"));
                map.put("cash_id", cash_id);
                c.getString(c.getColumnIndex("cash_time"));
                map.put("cash_time", c.getString(c.getColumnIndex("cash_time")));
                c.getString(c.getColumnIndex("seller_id"));
                map.put("seller_id", c.getString(c.getColumnIndex("seller_id")));
                c.getString(c.getColumnIndex("amount_receivable"));
                map.put("amount_receivable", c.getString(c.getColumnIndex("amount_receivable")));
                c.getString(c.getColumnIndex("receive_amount"));
                map.put("receive_amount", c.getString(c.getColumnIndex("receive_amount")));
                c.getString(c.getColumnIndex("add_change"));
                map.put("add_change", c.getString(c.getColumnIndex("add_change")));
                c.getString(c.getColumnIndex("payment"));
                map.put("payment", c.getString(c.getColumnIndex("payment")));
                Cursor cursor = sqLiteDatabase.rawQuery("select * from goodsSell where cash_id=?", new String[]{cash_id});
                List<Map<String, String>> listmap = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Map<String, String> map2 = new HashMap<>();
                    cursor.getString(cursor.getColumnIndex("goods_id"));
                    map2.put("goods_id", cursor.getString(cursor.getColumnIndex("goods_id")));
                    cursor.getString(cursor.getColumnIndex("name"));
                    map2.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    cursor.getString(cursor.getColumnIndex("number"));
                    map2.put("nums", cursor.getString(cursor.getColumnIndex("number")));
                    cursor.getString(cursor.getColumnIndex("py"));
                    map2.put("py", cursor.getString(cursor.getColumnIndex("py")));
                    cursor.getString(cursor.getColumnIndex("price"));
                    map2.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    cursor.getString(cursor.getColumnIndex("cost"));
                    map2.put("cost", cursor.getString(cursor.getColumnIndex("cost")));
                    cursor.getString(cursor.getColumnIndex("amount"));
                    map2.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
                    cursor.getString(cursor.getColumnIndex("product_id"));
                    map2.put("product_id", cursor.getString(cursor.getColumnIndex("product_id")));
                    cursor.getString(cursor.getColumnIndex("bncode"));
                    map2.put("bncode", cursor.getString(cursor.getColumnIndex("bncode")));
                    cursor.getString(cursor.getColumnIndex("store"));
                    map2.put("store", cursor.getString(cursor.getColumnIndex("store")));
                    cursor.getString(cursor.getColumnIndex("good_limit"));
                    cursor.getString(cursor.getColumnIndex("good_stock"));
                    cursor.getString(cursor.getColumnIndex("PD"));
                    cursor.getString(cursor.getColumnIndex("GD"));
                    cursor.getString(cursor.getColumnIndex("marketable"));
                    cursor.getString(cursor.getColumnIndex("tag_name"));
                    cursor.getString(cursor.getColumnIndex("tag_id"));
                    cursor.getString(cursor.getColumnIndex("unit"));
                    cursor.getString(cursor.getColumnIndex("unit_id"));
                    cursor.getString(cursor.getColumnIndex("cash_id"));
                    map2.put("bn", cursor.getString(cursor.getColumnIndex("bn")));
                    map2.put("cash_id", cursor.getString(cursor.getColumnIndex("cash_id")));
                    listmap.add(map2);
                }
                map.put("goods_info", listmap);
                List.add(map);
            }
            Gson gson = new Gson();
            String str = gson.toJson(List);
            Log.d("print", "QueryOrder: "+str);
            return str;
        }else {
            return "";
        }
    }

    /**
     * 上传成功删除本地数据
     */
    public void deleteorder(){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        sqLiteDatabase.execSQL(("delete from  ProOut"));
        sqLiteDatabase.execSQL(("delete from  goodsSell"));
    }

    /**
     * 检查数据库是否有该字段
     * @param tableName
     * @param columnName
     * @return
     */
    public boolean checkColumnExist1( String tableName, String columnName) {
        SQLiteDatabase db=sqliteHelper.getReadableDatabase();
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
             cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"            , null );
             result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
            Log.e("print","checkColumnExists1..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result ;
    }

    /**
     * 商品写入数据库
     * @param datas
     */
    public void insertcommodity(List<Commodity> datas){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        sqLiteDatabase.execSQL(("delete from  commodity"));
        String sql="insert into commodity (goods_id,name," +
                "py,price" +
                ",cost,bncode," +
                "tag_id,unit," +
                "store,good_limit," +
                "good_stock,PD,GD," +
                "marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement stat= sqLiteDatabase.compileStatement(sql);

        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < datas.size(); i++) {
            stat.bindString(1,datas.get(i).getGoods_id());
            stat.bindString(2,datas.get(i).getName());
            stat.bindString(3,datas.get(i).getPy());
            stat.bindString(4,datas.get(i).getPrice());
            stat.bindString(5,datas.get(i).getCost());
            stat.bindString(6, datas.get(i).getBncode().replaceAll(" ",""));
            stat.bindString(7,datas.get(i).getTag_id());
            stat.bindString(8,datas.get(i).getUnit());
            stat.bindString(9,datas.get(i).getStore());
            stat.bindString(10,datas.get(i).getGood_limit());
            stat.bindString(11,datas.get(i).getGood_stock());
            stat.bindString(12,datas.get(i).getPD());
            stat.bindString(13,datas.get(i).getGD());
            stat.bindString(14,datas.get(i).getMarketable());
            stat.bindString(15,datas.get(i).getTag_name());
            stat.bindString(16,datas.get(i).getAltc());
            stat.bindString(17,datas.get(i).getProduct_id()+"");
            stat.bindString(18,datas.get(i).getBn());
            stat.bindString(19,datas.get(i).getProvider_id());
            stat.bindString(20,datas.get(i).getProvider_name());
            stat.bindString(21,datas.get(i).getCook_position());
            stat.bindString(22,datas.get(i).getMember_price());
            stat.bindString(23,datas.get(i).getIs_special_offer());
            stat.executeInsert();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 订单写入本地的方法
     * @param cash_id 订单id
     * @param cash_time 订单时间
     * @param seller_id 操作人
     * @param amount_receivable 实收
     * @param receive_amount 应收
     * @param add_change 找零
     * @param payment 支付方式
     * @param commodities 商品
     * @param numberEntties 记录商品数量
     * @param iswholesale 是否取会员价
     */
    public void insertOrder(String cash_id, String cash_time, String seller_id,
                            String amount_receivable, String receive_amount,
                            String add_change, String payment,
                            List<Commodity> commodities, List<New_NumberEntty> numberEntties,boolean iswholesale){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        //商品写入数据库方法
        //order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash"
        sqLiteDatabase.execSQL("insert into ProOut (cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)values(?,?,?,?,?,?,?)", new Object[]{cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment});

        if (commodities!=null) {
            for (int i = 0; i < commodities.size(); i++) {
                String price=commodities.get(i).getPrice();
                if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("no")) {
                            if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")) {
                                if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") ) {
                                    price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                }
                            } else {
                                if (!commodities.get(i).getMember_price().equals("") ) {
                                    price=commodities.get(i).getMember_price();
                                }
                            }
                        }
                    } else {
                        if (!commodities.get(i).getPrice().equals("") ) {
                            price=commodities.get(i).getPrice();
                        }
                    }
                }else {
                    if (!commodities.get(i).getPrice().equals("") ) {
                        price=commodities.get(i).getPrice();
                    }
                }

                sqLiteDatabase.execSQL("insert into goodsSell (goods_id,name,number,py,price,cost,amount,product_id,bncode,store," +
                        "good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                        commodities.get(i).getGoods_id(), commodities.get(i).getName(),
                        numberEntties.get(i).getNumber(), commodities.get(i).getPy(), price,
                        commodities.get(i).getCost(), (Float.parseFloat(price) * numberEntties.get(i).getNumber()),
                        commodities.get(i).getProduct_id(), commodities.get(i).getBncode(), commodities.get(i).getStore(),
                        commodities.get(i).getGood_limit(), commodities.get(i).getGood_stock(),
                        commodities.get(i).getPD(), commodities.get(i).getGD(), commodities.get(i).getMarketable(),
                        commodities.get(i).getTag_name(), commodities.get(i).getTag_id(), commodities.get(i).getUnit(),
                        commodities.get(i).getUnit_id(), cash_id, commodities.get(i).getBn()});
                Log.d("print","商品写入的方式");
            }
        }
    }


    /**
     * 订单写入本地的方法
     * @param cash_id 订单id
     * @param cash_time 订单时间
     * @param seller_id 操作人
     * @param amount_receivable 实收
     * @param receive_amount 应收
     * @param add_change 找零
     * @param payment 支付方式
     * @param commodities 商品
     * @param numberEntties 记录商品数量
     * @param iswholesale 是否取会员价
     */
    public void insertOrder1(String cash_id, String cash_time, String seller_id,
                            String amount_receivable, String receive_amount,
                            String add_change, String payment,
                            List<Commodity> commodities, List<ShuliangEntty> numberEntties,boolean iswholesale){
        SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
        //商品写入数据库方法
        //order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash"
        sqLiteDatabase.execSQL("insert into ProOut (cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)values(?,?,?,?,?,?,?)", new Object[]{cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment});

        if (commodities!=null) {
            for (int i = 0; i < commodities.size(); i++) {
                String price=commodities.get(i).getPrice();
                if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("no")) {
                            if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")) {
                                if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") ) {
                                    price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                }
                            } else {
                                if (!commodities.get(i).getMember_price().equals("") ) {
                                    price=commodities.get(i).getMember_price();
                                }
                            }
                        }
                    } else {
                        if (!commodities.get(i).getPrice().equals("") ) {
                            price=commodities.get(i).getPrice();
                        }
                    }
                }else {
                    if (!commodities.get(i).getPrice().equals("") ) {
                        price=commodities.get(i).getPrice();
                    }
                }

                sqLiteDatabase.execSQL("insert into goodsSell (goods_id,name,number,py,price,cost,amount,product_id,bncode,store," +
                        "good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                        commodities.get(i).getGoods_id(), commodities.get(i).getName(),
                        numberEntties.get(i).getNumber(), commodities.get(i).getPy(), price,
                        commodities.get(i).getCost(), (Float.parseFloat(price) * numberEntties.get(i).getNumber()),
                        commodities.get(i).getProduct_id(), commodities.get(i).getBncode(), commodities.get(i).getStore(),
                        commodities.get(i).getGood_limit(), commodities.get(i).getGood_stock(),
                        commodities.get(i).getPD(), commodities.get(i).getGD(), commodities.get(i).getMarketable(),
                        commodities.get(i).getTag_name(), commodities.get(i).getTag_id(), commodities.get(i).getUnit(),
                        commodities.get(i).getUnit_id(), cash_id, commodities.get(i).getBn()});
                Log.d("print","商品写入的方式");
            }
        }
    }



    public void insertStock(List<Commodity> commodities,List<New_NumberEntty> numberEntties){
        if (sqliteHelper!=null){
            SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
            if (commodities!=null){
                for (int r = 0; r < commodities.size(); r++) {
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    while (cursor.moveToNext()) {
                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                        String newnums = (Float.parseFloat(nums) - numberEntties.get(r).getNumber()) + "";
                        ContentValues values = new ContentValues();
                        values.put("store", newnums);
                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    }
                }
            }
        }
    }

    public void insertStock1(List<Commodity> commodities,List<ShuliangEntty> numberEntties){
        if (sqliteHelper!=null){
            SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
            if (commodities!=null){
                for (int r = 0; r < commodities.size(); r++) {
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    while (cursor.moveToNext()) {
                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                        String newnums = (Float.parseFloat(nums) - numberEntties.get(r).getNumber()) + "";
                        ContentValues values = new ContentValues();
                        values.put("store", newnums);
                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    }
                }
            }
        }
    }


    public void insertfunction(List<String> datas){
        if (sqliteHelper!=null) {
            SQLiteDatabase sqLiteDatabase = sqliteHelper.getReadableDatabase();
            Cursor c = sqLiteDatabase.rawQuery("select * from function", null);
            if (c.getCount()==0){
                    String sql="insert into function (name,main_sequence," +
                            "secondary_sequence,type" +
                            ",id)values(?,?,?,?,?)";
                    SQLiteStatement stat= sqLiteDatabase.compileStatement(sql);
                    sqLiteDatabase.beginTransaction();
                    for (int i = 0; i < datas.size(); i++) {
                        Log.d("print","点击成功");
                        stat.bindString(1,datas.get(i));
                        stat.bindString(2,"0");
                        stat.bindString(3,"0");
                        stat.bindString(4,false+"");
                        stat.bindString(5,"");
                        stat.executeInsert();
                    }
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
            }

        }
    }


    public List<Function_entty> queryfunction(){
        List<Function_entty> function_entties=new ArrayList<>();
        Function_entty function_entty;
        SQLiteDatabase database=sqliteHelper.getReadableDatabase();
        Cursor cursor=database.query("function",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            function_entty=new Function_entty();
            function_entty.setName(cursor.getString(cursor.getColumnIndex("name")));
            function_entty.setId(cursor.getString(cursor.getColumnIndex("id")));
            function_entty.setMain_sequence(cursor.getString(cursor.getColumnIndex("main_sequence")));
            function_entty.setSecondary_sequence(cursor.getString(cursor.getColumnIndex("secondary_sequence")));
            function_entty.setType(cursor.getString(cursor.getColumnIndex("type")));
            function_entties.add(function_entty);
        }
        return function_entties;
    }


    public List<Function_entty> queryfunctionnmain(){
        List<Function_entty> function_entties=new ArrayList<>();
        Function_entty function_entty;
        SQLiteDatabase database=sqliteHelper.getReadableDatabase();
        Cursor cursor=database.query("function",null,"type = ?",new String[]{"true"},null,null,"main_sequence asc",null);
        while (cursor.moveToNext()){
            function_entty=new Function_entty();
            function_entty.setName(cursor.getString(cursor.getColumnIndex("name")));
            function_entty.setId(cursor.getString(cursor.getColumnIndex("id")));
            function_entty.setMain_sequence(cursor.getString(cursor.getColumnIndex("main_sequence")));
            function_entty.setSecondary_sequence(cursor.getString(cursor.getColumnIndex("secondary_sequence")));
            function_entty.setType(cursor.getString(cursor.getColumnIndex("type")));
            function_entties.add(function_entty);
        }
        return function_entties;
    }


    public List<Function_entty> queryuser(){
        List<Function_entty> function_entties=new ArrayList<>();
        Function_entty function_entty;
        SQLiteDatabase database=sqliteHelper.getReadableDatabase();
        Cursor cursor=database.query("function",null,"secondary_sequence = ?",new String[]{"0"},null,null,"main_sequence asc",null);
        while (cursor.moveToNext()){
            function_entty=new Function_entty();
            function_entty.setName(cursor.getString(cursor.getColumnIndex("name")));
            function_entty.setId(cursor.getString(cursor.getColumnIndex("id")));
            function_entty.setMain_sequence(cursor.getString(cursor.getColumnIndex("main_sequence")));
            function_entty.setSecondary_sequence(cursor.getString(cursor.getColumnIndex("secondary_sequence")));
            function_entty.setType(cursor.getString(cursor.getColumnIndex("type")));
            function_entties.add(function_entty);
        }
        return function_entties;
    }

    public void insertTYpe(String name,String type,String i){
        if (sqliteHelper!=null){
            SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
            if (name!=null){
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from function where name=?", new String[]{name});
                    while (cursor.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put("type", type);
                        values.put("main_sequence", i);
                        sqLiteDatabase.update("function", values, "name=?", new String[]{name});
                    }
            }
        }
    }

    public void insertuser(String name,String secondary_sequence,String i){
        if (sqliteHelper!=null){
            SQLiteDatabase sqLiteDatabase=sqliteHelper.getReadableDatabase();
            if (name!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("select * from function where name=?", new String[]{name});
                while (cursor.moveToNext()) {
                    ContentValues values = new ContentValues();
                    values.put("secondary_sequence", secondary_sequence);
                    sqLiteDatabase.update("function", values, "name=?", new String[]{name});
                }
            }
        }
    }




    public List<Message_Beans2> queryAllMsgs1(){
        List<Message_Beans2> msgBeanList=new ArrayList<>();
        Message_Beans2 msgBean;
        SQLiteDatabase database=sqliteHelper.getReadableDatabase();
        Cursor cursor=database.query("messages",new String[]{"content","state","time"},null,null,null,null,null,null);
        while (cursor.moveToNext()){
            msgBean=new Message_Beans2();
            msgBean.setState(cursor.getString(cursor.getColumnIndex("state")));
//            msgBean.setId(cursor.getString(cursor.getColumnIndex("id")));
            msgBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            msgBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
            msgBeanList.add(msgBean);
        }
        return msgBeanList;
    }


    public void replace(){
        SQLiteDatabase sqLiteDatabases=sqliteHelper.getWritableDatabase();
        sqLiteDatabases.beginTransaction();//开启事务
    }


    public int deleteMsgs(String time){
        SQLiteDatabase database=sqliteHelper.getWritableDatabase();
        database.beginTransaction();
//        String timeStr=String.valueOf(time);
        //int result = database.delete("messages", "where time = ?", new String[]{timeStr});
        try {
            database.execSQL("DELETE FROM messages WHERE time = ?",new String[]{time});
            database.setTransactionSuccessful();
        }catch (Exception e){
            return 0;
        }finally {
            database.endTransaction();
        }
        return 1;
    }



    //功能按钮写入本地方法


    public boolean insertFunction(String name,String main_sequence,String secondary_sequence,String type){
        SQLiteDatabase db=sqliteHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("main_sequence",main_sequence);
        values.put("secondary_sequence",secondary_sequence);
        values.put("type",type);

        long insert=db.insert("messages",null,values);
        if(insert>0){
            return true;
        }
        return false;
    }


    /**
     * 商品名，拼音码，条码 搜索本地的商品
     * @param str
     * @return
     */
    public List<Commodity> LocalSeek(String str) {
        if (sqliteHelper != null) {
            SQLiteDatabase sqLiteDatabase = sqliteHelper.getReadableDatabase();
            Cursor cursor = null;
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(str);
            if (m.matches()) {
                cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode like " + "'%" + str + "%'", null);
            }
            p = Pattern.compile("[a-zA-Z]*");
            m = p.matcher(str);
            if (m.matches()) {
                cursor = sqLiteDatabase.rawQuery("select * from commodity where py like " + "'%" + str + "%'", null);
            }
            p = Pattern.compile("[\u4e00-\u9fa5]*");
            m = p.matcher(str);
            if (m.matches()) {
                cursor = sqLiteDatabase.rawQuery("select * from commodity where name like " + "'%" + str + "%'", null);
            }
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                } else {
                    List<Commodity> list=new ArrayList<>();
                    while (cursor.moveToNext()) {
                        Commodity fuzzy = new Commodity();
                        fuzzy.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                        fuzzy.setName(cursor.getString(cursor.getColumnIndex("name")));
                        fuzzy.setPy(cursor.getString(cursor.getColumnIndex("py")));
                        fuzzy.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                        fuzzy.setCost(cursor.getString(cursor.getColumnIndex("cost")));
                        fuzzy.setStore(cursor.getString(cursor.getColumnIndex("store")));
                        fuzzy.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
                        fuzzy.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
                        fuzzy.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                        fuzzy.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
                        list.add(fuzzy);
                    }
                    return list;
                }
            }else {
                return null;
            }
        }else {
            return null;
        }
    }


}
