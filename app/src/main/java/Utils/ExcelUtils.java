package Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entty.Commodity;
import Entty.Member_entty;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import shujudb.Sqlite_Entity;

public class ExcelUtils {

	public static List<Map<String, String>> mapList;
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
		} catch (WriteException e) {

			e.printStackTrace();
		}
	}

	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("商品表", 0);
			sheet.addCell((WritableCell) new Label(0, 0, fileName,
					arial14format));
			for (int col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, 0, colName[col], arial10format));
			}
			workbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> void writeObjListToExcel(List<T> objList,
											   String fileName, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName),
						workbook);
				WritableSheet sheet = writebook.getSheet(0);
				for (int j = 0; j < objList.size(); j++) {
					ArrayList<String> list = (ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.addCell(new Label(i, j + 1, list.get(i),
								arial12format));
					}
				}
				writebook.write();
				Toast.makeText(c, "导出到手机存储中文件夹Family成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public static String replaceBlank(String src) {
		String dest = "";
		if (src != null) {
			Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
			Matcher matcher = pattern.matcher(src);
			dest = matcher.replaceAll("");
		}
		return dest;
	}

	static Sqlite_Entity sqlite_entity;

	public static List<Map<String, String>> read2DB(File f, Context con) {
		String namepy="";
		mapList=new ArrayList<>();
		mapList.clear();
		ArrayList<Commodity> billList = new ArrayList<>();
		Workbook course = null;
		try {
			course = Workbook.getWorkbook(f);
			Sheet sheet = course.getSheet(0);
			Cell cell = null;
			for (int i = 1; i < sheet.getRows(); i++) {
				Map<String, String> map1 = new HashMap<>();
				Commodity commodity = new Commodity();
//				cell = sheet.getCell(1, i);
//				commodity.setGoods_id(cell.getContents());
				cell = sheet.getCell(1, i);
				commodity.setName(cell.getContents());
				if (cell.getContents().contains("\\")){
					String name=replaceBlank(cell.getContents().replace("\\","/"));
					namepy=name;
					map1.put("name",name);
				}else {
					namepy=replaceBlank(cell.getContents());
					map1.put("name",replaceBlank(cell.getContents()));
				}

				cell = sheet.getCell(2, i);
				commodity.setPy(cell.getContents());

				String py = PinyinHelper.getShortPinyin(namepy);
				map1.put("py",py.toUpperCase());

				cell = sheet.getCell(3, i);
				commodity.setPrice(cell.getContents());
				map1.put("price",cell.getContents());
				cell = sheet.getCell(4, i);
				commodity.setCost(cell.getContents());
				map1.put("cost",cell.getContents());
				cell = sheet.getCell(5, i);
				commodity.setBncode(cell.getContents());
				if (cell.getContents().contains("'")){
					String bncode=cell.getContents().replace("'","");
					map1.put("bncode",bncode);
					Log.d("print","条形码是"+bncode);

				}else {
					map1.put("bncode",cell.getContents());
					Log.d("print","条形码是"+cell.getContents());
				}
				cell = sheet.getCell(6, i);
				commodity.setStore(cell.getContents());
				map1.put("store",cell.getContents());
				cell = sheet.getCell(7, i);
				commodity.setGood_limit(cell.getContents());
				map1.put("good_limit",cell.getContents());
				cell = sheet.getCell(8, i);
				commodity.setGood_stock(cell.getContents());
				if (cell.getContents().equals("")){
					map1.put("good_stock","0");
				}else {
					map1.put("good_stock",cell.getContents());
				}
				cell = sheet.getCell(9, i);
				commodity.setPD(cell.getContents());
				Log.e("print","读取的数据是"+cell.getContents());
				String s1="20"+cell.getContents()+" 00:00:00";
				long time1= DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s1);
				map1.put("PD",(time1/1000)+"");
				cell = sheet.getCell(10, i);
				commodity.setGD(cell.getContents());
				String s="20"+cell.getContents()+" 00:00:00";
				long time= DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s);
				map1.put("GD",(time/1000)+"");
				cell = sheet.getCell(11, i);
				commodity.setMarketable(cell.getContents());
				map1.put("marketable",cell.getContents());
				cell = sheet.getCell(12, i);
				map1.put("tag_name",cell.getContents());
				cell = sheet.getCell(13, i);
				map1.put("unit",cell.getContents());
				map1.put("seller_id", SharedUtil.getString("seller_id"));
				cell = sheet.getCell(14, i);
				if (cell.getContents().contains("\\")){
					String specification=cell.getContents().replace("\\","/");
					map1.put("specification",specification);
				}else {
					map1.put("specification",cell.getContents());
				}
				cell = sheet.getCell(15, i);
				Log.d("print","批量导入的商品"+cell.getContents());
				map1.put("custom_member_price",cell.getContents());

				cell = sheet.getCell(15, i);
				map1.put("produce_addr",cell.getContents());

				if (!namepy.equals("")&&namepy!=null){
					mapList.add(map1);
					billList.add(commodity);
				}
//				if (cell.getContents().contains("\\")){
//					String name=replaceBlank(cell.getContents().replace("\\","/"));
//					namepy=name;
//					if(name.equals("")||name==null){
//					}else {
//						mapList.add(map1);
//						billList.add(commodity);
//					}
//				}else {
//					if(replaceBlank(cell.getContents()).equals("")||replaceBlank(cell.getContents())==null){
//					}else {
//						mapList.add(map1);
//						billList.add(commodity);
//					}
//				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (course!=null){
				course.close();
			}
					}
		return mapList;
	}
	public static List<Map<String, String>> read2DB2(File f, Context con) {
		String namepy="";
		mapList=new ArrayList<>();
		ArrayList<Commodity> billList = new ArrayList<>();
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(f);
			Sheet sheet = course.getSheet(0);
			Cell cell = null;
			for (int i = 1; i < sheet.getRows(); i++) {
				Map<String, String> map1 = new HashMap<>();
				Commodity commodity = new Commodity();
//				cell = sheet.getCell(1, i);
//				commodity.setGoods_id(cell.getContents());
				cell = sheet.getCell(1, i);
				commodity.setName(cell.getContents());
				if (cell.getContents().contains("\\")){
					String name=cell.getContents().replace("\\","/");
					namepy=name;
					map1.put("name",name);
				}else {
					namepy=cell.getContents();
					map1.put("name",cell.getContents());
				}
				cell = sheet.getCell(2, i);
				commodity.setPy(cell.getContents());

				String py = PinyinHelper.getShortPinyin(namepy);
				map1.put("py",py.toUpperCase());

				cell = sheet.getCell(3, i);
				commodity.setPrice(cell.getContents());
				map1.put("price",cell.getContents());
				cell = sheet.getCell(4, i);
				commodity.setCost(cell.getContents());
				map1.put("cost",cell.getContents());
				cell = sheet.getCell(5, i);
				commodity.setBncode(cell.getContents());
				if (cell.getContents().contains("'")){
					String bncode=cell.getContents().replace("'","");
					map1.put("bncode",bncode);
					Log.d("print","条形码是"+bncode);

				}else {
					map1.put("bncode",cell.getContents());
					Log.d("print","条形码是"+cell.getContents());
				}
				cell = sheet.getCell(6, i);
				commodity.setStore(cell.getContents());
				map1.put("store",cell.getContents());
				cell = sheet.getCell(7, i);
				commodity.setGood_limit(cell.getContents());
				map1.put("good_limit",cell.getContents());
				cell = sheet.getCell(8, i);
				commodity.setGood_stock(cell.getContents());
				if (cell.getContents().equals("")){
					map1.put("good_stock","0");
				}else {
					map1.put("good_stock",cell.getContents());
				}
				cell = sheet.getCell(9, i);
				commodity.setPD(cell.getContents());
				Log.e("print","读取的数据是"+cell.getContents());
				String s1="20"+cell.getContents()+" 00:00:00";
				long time1= DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s1);
				map1.put("PD",(time1/1000)+"");
				cell = sheet.getCell(10, i);
				commodity.setGD(cell.getContents());
				String s="20"+cell.getContents()+" 00:00:00";
				long time= DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s);
				map1.put("GD",(time/1000)+"");
				cell = sheet.getCell(11, i);
				commodity.setMarketable(cell.getContents());
				map1.put("marketable",cell.getContents());
				cell = sheet.getCell(12, i);
				map1.put("tag_name",cell.getContents());
				cell = sheet.getCell(13, i);
				map1.put("unit",cell.getContents());
				map1.put("seller_id", SharedUtil.getString("seller_id"));
				cell = sheet.getCell(14, i);
				if (cell.getContents().contains("\\")){
					String specification=cell.getContents().replace("\\","/");
					map1.put("specification",specification);
				}else {
					map1.put("specification",cell.getContents());
				}


				if (cell.getContents().contains("\\")){
					String name=replaceBlank(cell.getContents().replace("\\","/"));
					namepy=name;
					if(name.equals("")||name==null){
					}else {
						mapList.add(map1);
						billList.add(commodity);
					}
				}else {
					if(replaceBlank(cell.getContents()).equals("")||replaceBlank(cell.getContents())==null){
					}else {
						mapList.add(map1);
						billList.add(commodity);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapList;
	}

	public static List<Map<String, String>> read2DB3(File f, Context con) {
		String namepy="";
		mapList=new ArrayList<>();
		ArrayList<Member_entty> billList = new ArrayList<>();
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(f);
			Sheet sheet = course.getSheet(0);
			Cell cell = null;
			for (int i = 1; i < sheet.getRows(); i++) {
				Map<String, String> map1 = new HashMap<>();
				Member_entty member_entty = new Member_entty();
//				cell = sheet.getCell(1, i);
//				commodity.setGoods_id(cell.getContents());
				cell = sheet.getCell(1, i);
				member_entty.setMember_name(cell.getContents());
				if (cell.getContents().contains("\\")){
					String name=cell.getContents().replace("\\","/");
					namepy=name;
					//会员名
					map1.put("member_name",name);
				}else {
					namepy=cell.getContents();
					map1.put("member_name",cell.getContents());
				}
				cell = sheet.getCell(2, i);
				member_entty.setDiscount_rate(cell.getContents());

//				String py = PinyinHelper.getShortPinyin(namepy);
				//折扣率
				map1.put("discount_rate",cell.getContents());
				cell = sheet.getCell(3, i);
				member_entty.setMobile(cell.getContents());
				//手机号
				map1.put("mobile",cell.getContents());
				cell = sheet.getCell(4, i);
				member_entty.setScore(cell.getContents());
				//积分
				map1.put("score",cell.getContents());
//				cell = sheet.getCell(5, i);
//				member_entty.setTime(cell.getContents());
//				map1.put("addtime",cell.getContents());

//				cell = sheet.getCell(9, i);
//				commodity.setPD(cell.getContents());
//				Log.e("print","读取的数据是"+cell.getContents());
//				String s1="20"+cell.getContents()+" 00:00:00";
//				long time1=DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s1);
//				map1.put("PD",(time1/1000)+"");
//				cell = sheet.getCell(10, i);
//				commodity.setGD(cell.getContents());
//				String s="20"+cell.getContents()+" 00:00:00";
//				long time=DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm:ss",s);
//				map1.put("GD",(time/1000)+"");
//				cell = sheet.getCell(11, i);
//				commodity.setMarketable(cell.getContents());
//				map1.put("marketable",cell.getContents());
//				cell = sheet.getCell(12, i);
//				map1.put("tag_name",cell.getContents());
//				cell = sheet.getCell(13, i);
//				map1.put("unit",cell.getContents());
//				map1.put("seller_id",SharedUtil.getString("seller_id"));
//				cell = sheet.getCell(14, i);
//				if (cell.getContents().contains("\\")){
//					String specification=cell.getContents().replace("\\","/");
//					map1.put("specification",specification);
//				}else {
//					map1.put("specification",cell.getContents());
//				}
				mapList.add(map1);
//				billList.add(commodity);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapList;
	}

	public static Object getValueByRef(Class cls, String fieldName) {
		Object value = null;
		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
				.substring(0, 1).toUpperCase());
		String getMethodName = "get" + fieldName;
		try {
			Method method = cls.getMethod(getMethodName);
			value = method.invoke(cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
