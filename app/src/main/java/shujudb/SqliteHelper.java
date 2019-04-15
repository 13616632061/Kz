package shujudb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 2017/3/20.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private static SqliteHelper sInstance;
    public SqliteHelper(Context context) {
        super(context, "yonghu.db", null, 28);
    }
    public static SqliteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SqliteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

//    第一次连接数据库时执行
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL("create table if not exists commodity(_id integer primary key,goods_id,name,py,price,cost,bncode,tag_id,unit,store,good_limit,good_stock,PD,GD,marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer,custom_member_price)");
        //创建一个备份的数据库
//        sqLiteDatabase.execSQL("create table if not exists backups(_id integer primary key,goods_id,name,py,price,cost,bncode,tag_id,unit,store,good_limit,good_stock,PD,GD,marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price)");
        sqLiteDatabase.execSQL("create table if not exists quick(_id integer primary key,goods_id,name,py,price,cost,bncode,tag_id,unit,store,good_limit,good_stock,PD,GD,marketable,tag_name,ALTC,product_id,bn)");
        sqLiteDatabase.execSQL("create table if not exists exls(_id integer primary key,goods_id,name,py,price,cost,bncode,store,good_limit,good_stock,PD,GD,marketable)");
        sqLiteDatabase.execSQL("create table if not exists  ProOut(_id integer primary key,cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)");
        sqLiteDatabase.execSQL("create table if not exists goodsSell(_id integer primary key,goods_id,name,number,py,price,cost,amount,product_id,bncode,store,good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn)");

        //消息需要的表结构
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS messages(id_ INTEGER PRIMARY KEY AUTOINCREMENT,content VARCHAR(500),time VARCHAR(100),state VARCHAR(100))");

        //功能列表的表结构
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS function(id_ INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(500),main_sequence VARCHAR(100),secondary_sequence VARCHAR(100),type VARCHAR(100),id VARCHAR(100))");

        //挂单的商品表结构
        sqLiteDatabase.execSQL("create table if not exists hangupgoods(_id integer primary key,order_id,goods_id,name,py,price,cost,bncode,store,marketable,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer,number,sellingprice,good_size,goods_notes,tag_id)");
        //挂单的订单数据
        sqLiteDatabase.execSQL("create table if not exists  hangup(_id integer primary key,order_id,name,remarks,table_name,table_id,createtime,package,customer_num,total_amount)");


        //餐饮所需的表结构
        //分类表
//        sqLiteDatabase.equals("create table if not exists res_goods_sort(sort_id varchar(20) PRIMARY KEY,sort_name varchar(20))");
//        sqLiteDatabase.equals("create table if not exists res_goods_info(goods_id varchar(20) PRIMARY KEY,goods_name varchar(20))");

    }
//     版本更新
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            if (i<i1){
//            String sql1 = "ALTER TABLE commodity ADD COLUMN provider_id VARCHAR";
//            String sql2 = "ALTER TABLE commodity ADD COLUMN provider_name VARCHAR";
//            sqLiteDatabase.execSQL(sql1);
//            sqLiteDatabase.execSQL(sql2);
//            sqLiteDatabase.execSQL("create table if not exists commodity(_id integer primary key,goods_id,name,py,price,cost,bncode,tag_id,unit,store,good_limit,good_stock,PD,GD,marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position)");

//            String sql3 = "ALTER TABLE commodity ADD COLUMN cook_position VARCHAR";
//            sqLiteDatabase.execSQL(sql3);
            sqLiteDatabase.execSQL("create table if not exists  ProOut(_id integer primary key,cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change)");
            sqLiteDatabase.execSQL("create table if not exists goodsSell(_id integer primary key,goods_id,name,number,py,price,cost,amount,product_id,bncode,store,good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id)");
            sqLiteDatabase.execSQL("create table if not exists commodity(_id integer primary key,goods_id,name,py,price,cost,bncode,tag_id,unit,store,good_limit,good_stock,PD,GD,marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer,custom_member_price)");
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS messages(id_ INTEGER PRIMARY KEY AUTOINCREMENT,content VARCHAR(500),time VARCHAR(100),state VARCHAR(100))");

                //挂单的商品表结构
                sqLiteDatabase.execSQL("create table if not exists hangupgoods(_id integer primary key,order_id,goods_id,name,py,price,cost,bncode,store,marketable,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer,number,sellingprice,good_size,goods_notes,tag_id)");
                //挂单的订单数据
                sqLiteDatabase.execSQL("create table if not exists  hangup(_id integer primary key,order_id,name,remarks,table_name,table_id,createtime,package,customer_num,total_amount)");

                //功能列表的表结构
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS function(id_ INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(500),main_sequence VARCHAR(100),secondary_sequence VARCHAR(100),type VARCHAR(100),id VARCHAR(100))");

            if (!checkColumnExist1(sqLiteDatabase,"commodity","is_special_offer")){
                upgradeToVersion1001(sqLiteDatabase,"commodity");
                }
            if (!checkColumnExist1(sqLiteDatabase,"commodity","custom_member_price")){
                upgradeToVersion1002(sqLiteDatabase,"commodity");
                }
                if (!checkColumnExist1(sqLiteDatabase,"hangupgoods","tag_id")){
                    upgradeToVersion1003(sqLiteDatabase,"hangupgoods");
                }
            }
    }

    private void upgradeToVersion1001(SQLiteDatabase db,String tabname){
        // favorite表新增1个字段
        String sql1 = "ALTER TABLE "+tabname+" ADD COLUMN is_special_offer VARCHAR";
        db.execSQL(sql1);
    }
    private void upgradeToVersion1002(SQLiteDatabase db,String tabname){
        // favorite表新增1个字段
        String sql1 = "ALTER TABLE "+tabname+" ADD COLUMN custom_member_price VARCHAR";
        db.execSQL(sql1);
    }
    private void upgradeToVersion1003(SQLiteDatabase db,String tabname){
        // favorite表新增1个字段
        String sql1 = "ALTER TABLE "+tabname+" ADD COLUMN tag_id VARCHAR";
        db.execSQL(sql1);
    }

    public boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName) {
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

}
