package Utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.gprinter.command.EscCommand;
import com.gprinter.command.GpUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import Entty.Commodity;
import Entty.In_Out_Details;
import Entty.New_NumberEntty;
import Entty.Refund_entty;
import Entty.ShuliangEntty;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**蓝牙打印,排版打印格式
 * @author linjinfa@126.com
 * @date 2013-6-17 下午3:37:10 
 */
public class BluetoothPrintFormatUtil {

	/**
	 * 打印纸一行最大的字节
	 */
	public static final int LINE_BYTE_SIZE = 32;
	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = "$";

	public static StringBuffer sb = new StringBuffer();

	/**
	 * 选择加粗模式
	 */
	public static final byte[] BOLD = {0x1b, 0x45, 0x01};
	/**
	 * 取消加粗模式
	 */
	public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};
	/**

	 /**
	 * 排版居中标题
	 * @param title
	 * @return
	 */
	public static String printTitle(String title) {
		sb.delete(0, sb.length());
		for (int i = 0; i < (LINE_BYTE_SIZE - getBytesLength(title)) / 2; i++) {
			sb.append(" ");
		}
		sb.append(title);
		return sb.toString();
	}
	public static  int  getWordCount(String s) {
		int  length  =   0 ;
		for ( int  i  =   0 ; i  <  s.length(); i ++ )
		{
			int  ascii  =  Character.codePointAt(s, i);
			if (ascii  >=   0   &&  ascii  <= 255 )
				length ++ ;
			else
				length  +=   2 ;

		}
		return  length;
	}


	public static String getMiddle(String c, int length){
		String str="";
		for (int i=0;i<(length-(c.length()+4))/2;i++){
			str += "-";
		}
		str+="  "+c+"  ";
		for (int i=(length-c.length())/2;i<length-4;i++){
			str+="-";
		}

		return str;
	}


	public static String getPrintChar(String c, int length) {
		String ret = "";

		for(int i = 0; i < length; i++) {
			ret += c;
		}

		return ret;
	}
	//打印在右边
	public static String getPrintRight(String title, int l) {
		return getPrintRight(title, " ", l);
	}

	public static String getPrintRight(String title, String ch, int l) {
		String ret = "";

		int shopNameLength = getWordCount(title);
		l -= shopNameLength;
		if(l > 0) {
			for(int i = 0; i < l; i++) {
				ret += ch;
			}
		}
		ret += title;

		return ret;
	}

	//打印在左边
	public static String getPrintLeft(String title, int l) {
		String ret = title;

		int shopNameLength = getWordCount(title);
		l -= shopNameLength;
		if(l > 0) {
			for(int i = 0; i < l; i++) {
				ret += " ";
			}
		}

		return ret;
	}
	//打印在左边
	public static String getPrint(String title, int l) {
		String ret = title;

		int shopNameLength = getWordCount(title);
		l -= shopNameLength;
		if(l > 0) {
			for(int i = 0; i < l; i++) {
				ret += " ";
			}
		}

		return ret;
	}


	public static String PrintTransfer(String Cashier,String Mobile,String Duecash,String Spare_gold,String Total,String singular,
									   String Paid,
									   String Other,
									   String remark,
									   String startTime,
									   String endTime){
		String ret = "";
		//商家名称
		ret += "\r\n";
		Cashier = printTitle(Cashier);
		ret+=Cashier;
		ret += "\r\n";
		ret += getPrintLeft("移动收入：" + Mobile, LINE_BYTE_SIZE);
		ret += getPrintLeft("应有现金：" + Duecash, LINE_BYTE_SIZE);
		ret += getPrintLeft("备用金：" + Spare_gold, LINE_BYTE_SIZE);
		ret += getPrintLeft("总销售额：" + Total, LINE_BYTE_SIZE);
		ret += getPrintLeft("总单数：" + singular, LINE_BYTE_SIZE);
		ret += getPrintLeft("上缴金额：" + Paid, LINE_BYTE_SIZE);
		ret += getPrintLeft("其他收入：" + Other, LINE_BYTE_SIZE);
		ret += getPrintLeft("备注：" + remark, LINE_BYTE_SIZE);
		ret += getPrintLeft("开始时间：" + startTime, LINE_BYTE_SIZE);
		ret += getPrintLeft("结束时间：" + endTime, LINE_BYTE_SIZE);
		ret += "\r\n";
		ret += "\r\n";
		ret += "\r\n";
		ret += "\r\n";
		return ret;

	}


	public static String Printsale(String Cashier,String time,String staff,String pay,String Total,String singular,
									   String Paid){
		String ret = "";
		//商家名称
		ret += "\r\n";
		Cashier = printTitle(Cashier);
		ret+=Cashier;
		ret += "\r\n";
		ret += getPrintLeft("时间：" + time, LINE_BYTE_SIZE);
		ret += getPrintLeft("员工：" + staff, LINE_BYTE_SIZE);
		ret += getPrintLeft("支付方式：" + pay, LINE_BYTE_SIZE);
		ret += getPrintLeft("总销售额：" + Total, LINE_BYTE_SIZE);
		ret += getPrintLeft("现金支付：" + singular, LINE_BYTE_SIZE);
		ret += getPrintLeft("移动支付：" + Paid, LINE_BYTE_SIZE);
		ret += "\r\n";
		ret += "\r\n";
		return ret;

	}


	//得到收银小票的打印模板
	public static String getcashPrinterMsg(
			String shopName,
			String order_id,
			String orderDate,
			boolean hasPay,
			double payed,
			String mark_text
	){
		String ret = "";
		//商家名称
		ret += "\r\n";
		shopName = printTitle(shopName);
		ret += shopName;
		ret += "\r\n";
		ret += "\r\n";
		//订单号
		ret += printTitle("订单号：" + order_id);
		//下单时间
		ret += printTitle("下单时间：" + orderDate);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//名称
		ret +="现金交易";
		int l1 = ss_l - getWordCount(shopName);
		for(int j = 0; j < l1; j++) {
			ret += " ";
		}

		int nn = 1;
		double pp =  payed;
		String su = SysUtils.priceFormat(nn * pp, false);
		//数量
		ret += getPrintRight(String.valueOf(1), 16);

		//小计
		ret += getPrintRight(su, 8);
		ret += "\r\n";
		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss);
		ret += hasPay ? "已支付" : "未支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		ret += getPrintLeft("备注："+mark_text , LINE_BYTE_SIZE);
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		ret += printTitle("本地生活就选 易之星");

		ret += "\n\n";

		return ret;
	};

	//打印在中间
	public static String getPrintCenter(String title) {
		String ret = "";

		int shopNameLength = getWordCount(title);
		int l = LINE_BYTE_SIZE - shopNameLength;
		if(l > 0) {
			int l_left = (l-3)/ 2;
			int l_right = l - l_left;

			ret = getPrintChar(" ", l_left) + title + getPrintChar(" ", l_right);
		}

//        if(ret.length() > 0) {
//            ret += "\n";
//        }

		return ret;
	}

	//得到收银余额支付小票的打印模板
	public static String getPrinterYUer(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<ShuliangEntty> entty,
			boolean hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String Discount,
			String balance,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount,
			float Memberdiscount,
			String _Total) {
		String ret ="";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
		ret += printTitle(shopName);
//		ret += shopName;
//		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
//		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){
//名称
					//名称
					String name;
					name= bean.getName();
					ret += name;

					ret+="\r\n";

//					String price=bean.getPrice();
					String price="0";
//					price=bean.getPrice();
					if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
						if (bean.getIs_special_offer()!=null){
							if (bean.getIs_special_offer().equals("no")) {
								if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
									if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
										price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
									}
								} else {
									if (!bean.getPrice().equals("") ) {
										price=bean.getPrice();
									}
								}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();								}
							}
						}else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();							}
						}
					}else {
						if (!bean.getPrice().equals("")){
							price=bean.getPrice();
						}
					}

					ret+=price;
					int l1 = ss_l - getWordCount(price);

					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = entty.get(i).getNumber();

					double pp = Double.parseDouble(price);
					String su;
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
//					su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
//									su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp),Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}

					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);

				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;

					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					String price="0";
//					price=bean.getPrice();
					if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
						if (bean.getIs_special_offer()!=null){
							if (bean.getIs_special_offer().equals("no")) {
								if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
									if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
										price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
									}
								} else {
									if (!bean.getPrice().equals("") ) {
										price=bean.getPrice();
									}
								}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();								}
							}
						}else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();							}
						}
					}else {
						if (!bean.getPrice().equals("")){
							price=bean.getPrice();
						}
					}

					double nn = entty.get(i).getNumber();

					double pp = Double.parseDouble(price);
					String su;
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
//					su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp),Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}

					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;

				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}

				String price="0";
//					price=bean.getPrice();
				if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
					if (bean.getIs_special_offer()!=null){
						if (bean.getIs_special_offer().equals("no")) {
							if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
								if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
									price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
								}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();
								}
							}
						} else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();								}
						}
					}else {
						if (!bean.getPrice().equals("") ) {
							price=bean.getPrice();							}
					}
				}else {
					if (!bean.getPrice().equals("")){
						price=bean.getPrice();
					}
				}

				double nn = entty.get(i).getNumber();

				double pp = Double.parseDouble(price);
				String su;
				if (SharedUtil.getString("sw_total")!=null){
					if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
//					su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
						if (bean.getIs_special_offer()!=null) {
							if (bean.getIs_special_offer().equals("no")) {
								su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp), Memberdiscount), false);
							} else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
				}else {
					su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
				}

				//数量
//				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
				ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		if (Double.parseDouble(_Total)>0){
			String sssw = "" + _Total;
			int sss_ = LINE_BYTE_SIZE - getWordCount(sssw);
			ret += "原价";
			ret += getPrintChar(" ", sss_ - 6);
			ret += sssw;
			ret += "\r\n";
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		ret += "余额支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注



//		if (Double.parseDouble(_Total)>0){
//			_Total=_Total.replaceAll("<br/>","\r\n");
//			ret += getPrintLeft("原价：" + _Total, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		balance = balance.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("会员余额：" + balance, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			String count="";
			if (_discount!=null&&!_discount.equals("")){
				count=Float.parseFloat(_discount)+"";
			}
			count = count.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(count)), LINE_BYTE_SIZE);
			ret += "\r\n";

		}

		Discount = Discount.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("会员折扣优惠：" + String.format("%.2f",Double.parseDouble(Discount)), LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);

		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}

		ret += "\r\n";
		ret += printTitle("欢迎再次光临");

		ret += "\r\n";

//		if (SharedUtil.getString("address")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
//		}
//
//		if (SharedUtil.getString("print_remarks")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
//		}


		ret += "\r\n";
		ret += "\n\n";
		return ret;
	}



	//得到收银余额支付小票的打印模板
	public static String getPrinterYUernew(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<New_NumberEntty> entty,
			boolean hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String Discount,
			String balance,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount,
			float Memberdiscount,
			String _Total) {
		String ret ="";

		ret += "\r\n";
		ret += printTitle(shopName);
		ret += "\r\n";
		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
		ret += getPrintLeft("单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);
			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){
					//名称
					String name;
					name= bean.getName();
					ret += name;
					ret+="\r\n";
					String price=bean.getPrice();
					ret+=price;
					int l1 = ss_l - getWordCount(price);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}
					double nn = entty.get(i).getNumber();
					double pp = Double.parseDouble(bean.getPrice());
					String su;
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;

					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = entty.get(i).getNumber();

					double pp = Double.parseDouble(bean.getPrice());
					String su;
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
//					su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}

					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;

				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}

				double nn = entty.get(i).getNumber();

				double pp = Double.parseDouble(bean.getPrice());
				String su;
				if (SharedUtil.getString("sw_total")!=null){
					if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
//					su = SysUtils.priceFormat(nn * pp * Memberdiscount, false);
						if (bean.getIs_special_offer()!=null) {
							if (bean.getIs_special_offer().equals("no")) {
								su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , Memberdiscount), false);
							} else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
				}else {
					su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
				}

				//数量
//				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
				ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		if (Double.parseDouble(_Total)>0){
			String sssw = "" + _Total;
			int sss_ = LINE_BYTE_SIZE - getWordCount(sssw);
			ret += "原价";
			ret += getPrintChar(" ", sss_ - 6);
			ret += sssw;
			ret += "\r\n";
			ret += getPrintChar("-", LINE_BYTE_SIZE);

//			_Total=_Total.replaceAll("<br/>","\r\n");
//			ret += getPrintLeft("原价：" + _Total, LINE_BYTE_SIZE);
//			ret += "\r\n";
		}

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		ret += "余额支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注

		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		balance = balance.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("会员余额：" + balance, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			String count="";
			if (_discount!=null&&!_discount.equals("")){
				count=Float.parseFloat(_discount)+"";
			}
			count = count.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(count)), LINE_BYTE_SIZE);
			ret += "\r\n";

		}

		Discount = Discount.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("会员折扣优惠：" + String.format("%.2f",Double.parseDouble(Discount)), LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);

		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}

		ret += "\r\n";
		ret += printTitle("欢迎再次光临");

		ret += "\r\n";

//		if (SharedUtil.getString("address")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
//		}
//
//		if (SharedUtil.getString("print_remarks")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
//		}


		ret += "\r\n";
		ret += "\n\n";
		return ret;
	}


	//得到收银余额支付小票的打印模板
	public static String getnewPrinterYUer(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<New_NumberEntty> entty,
			boolean hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile) {
		String ret ="";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
		ret += printTitle(shopName);
//		ret += shopName;
//		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
//		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			//名称
			String name;
			if(bean.getName().length()>8){
				name= bean.getName().substring(0,9);
			}else {
				name= bean.getName();
			}
			ret += name;
			int l1 = ss_l - getWordCount(name);
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}




			double nn = entty.get(i).getNumber();

			double pp = Double.parseDouble(bean.getPrice());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
//			ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
			ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

			//小计
			ret += getPrintRight(su, 8);

			if(!TextUtils.isEmpty(bean.getAltc())) {
				//属性
				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		ret += "余额支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);

		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}

		ret += "\r\n";
		ret += printTitle("欢迎再次光临");

		ret += "\r\n";

//		if (SharedUtil.getString("address")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
//		}
//
//		if (SharedUtil.getString("print_remarks")!=null){
//			ret += "\r\n";
//			ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
//		}


		ret += "\r\n";
		ret += "\n\n";
		return ret;
	}

	//得到收银小票的打印模板
	public static String getPrinterMsg(
									   String shopName,
									   String shopTel,
									   String orderSn,
									   String orderDate,
									   List<Commodity> goodsList,
									   List<ShuliangEntty> entty,
									   boolean hasPay,
									   double payed,
									   String orderRemark,
									   String yingshou,
									   String mobile) {
		String ret ="";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
		ret += printTitle(shopName);
//		ret += shopName;
//		ret += "\r\n";
//		shopName = getPrintCenter(shopName);
//		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			//名称
			String name;
			if(bean.getName().length()>8){
				name= bean.getName().substring(0,9);
			}else {
				name= bean.getName();
			}
			ret += name;

			int l1 = ss_l - getWordCount(name);
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}

			float nn = entty.get(i).getNumber();

			double pp = Double.parseDouble(bean.getPrice());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
//			ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
			ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

			//小计
			ret += getPrintRight(su, 8);

			if(!TextUtils.isEmpty(bean.getAltc())) {
				//属性
				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		ret += hasPay ? "移动支付" : "现金支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
		ret += "\r\n";
		ret += "\n\n";
		return ret;
	}


	//得到自助收银小票的打印模板
	public static String getSelfServicePrinterMsgg(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<ShuliangEntty> entty,
			int hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String zhaoling,
			String ispackage,
			String peoplenums,
			String tablenums,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount,
			float _Memberdiscount,
			String _Total) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		String dopackage="";
		if("true".equals(ispackage)){
			dopackage="( "+"打包"+" )";
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length())+dopackage);
		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="人数："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){

					//名称
					String name;
					name= bean.getName();
					ret += name;

					ret+="\r\n";

					String price="0";
//					price=bean.getPrice();
					if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
						if (bean.getIs_special_offer()!=null){
							if (bean.getIs_special_offer().equals("no")) {
									if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
										if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
											price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
										}
									} else {
										if (!bean.getPrice().equals("") ) {
											price=bean.getPrice();
										}
									}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();								}
							}
						}else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();							}
						}
					}else {
						if (!bean.getPrice().equals("")){
							price=bean.getPrice();
						}
					}
					ret+=price;
					int l1 = ss_l - getWordCount(price);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = entty.get(i).getNumber();

					double pp = Double.parseDouble(price);

					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);

				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;

					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}
					String price="0";
//					price=bean.getPrice();
					if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
						if (bean.getIs_special_offer()!=null){
							if (bean.getIs_special_offer().equals("no")) {
								if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
									if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
										price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
									}
								} else {
									if (!bean.getPrice().equals("") ) {
										price=bean.getPrice();
									}
								}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();								}
							}
						}else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();							}
						}
					}else {
						if (!bean.getPrice().equals("")){
							price=bean.getPrice();
						}
					}
					double nn = entty.get(i).getNumber();
					double pp = Double.parseDouble(price);
					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;
				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
				String price="0";
//					price=bean.getPrice();
				if (bean.getType()!=null&&!bean.getType().equals("")&&!bean.getType().equals("0")) {
					if (bean.getIs_special_offer()!=null){
						if (bean.getIs_special_offer().equals("no")) {
							if (bean.getCustom_member_price() != null && !bean.getCustom_member_price().equals("")) {
								if (!StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1].equals("")) {
									price=StringUtils.getStrings(bean.getCustom_member_price(), ",")[Integer.parseInt(bean.getType()) - 1];
								}
							} else {
								if (!bean.getPrice().equals("") ) {
									price=bean.getPrice();
								}
							}
						} else {
							if (!bean.getPrice().equals("") ) {
								price=bean.getPrice();								}
						}
					}else {
						if (!bean.getPrice().equals("") ) {
							price=bean.getPrice();							}
					}
				}else {
					if (!bean.getPrice().equals("")){
						price=bean.getPrice();
					}
				}

				double nn = entty.get(i).getNumber();
				double pp = Double.parseDouble(price);
				String su;
				if (discount){
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
				}else {
					su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
				}
				//数量
//				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
				ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
			}
//			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
//				ret += "\r\n";
//				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
//			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);


		if (Double.parseDouble(_Total)>0){
			String sssw = "" + _Total;
			int sss_ = LINE_BYTE_SIZE - getWordCount(sssw);
			ret += "原价";
			ret += getPrintChar(" ", sss_ - 6);
			ret += sssw;
			ret += "\r\n";
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		if(hasPay==1){
			ret +="移动支付";
		}else if(hasPay==2){
			ret +="现金支付";
		}else if(hasPay==3){
			ret +="未付款";
		}else if(hasPay==4){
			ret +="现金支付(定金)";
		}else if(hasPay==5){
			ret +="移动支付（定金）";
		}
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
////		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";



//		if (Double.parseDouble(_Total)>0){
//			_Total=_Total.replaceAll("<br/>","\r\n");
//			ret += getPrintLeft("原价：" + _Total, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}

		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			String count="";
			if (_discount!=null&&!_discount.equals("")){
				count=Float.parseFloat(_discount)+"";
			}
			count = count.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(count)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		zhaoling = zhaoling.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + zhaoling, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);
		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}
		ret += "\r\n";
		ret += printTitle("欢迎再次光临");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}

	//得到自助收银小票的打印模板
	public static String getSelfServicePrintermember(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<ShuliangEntty> entty,
			int hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String zhaoling,
			String ispackage,
			String peoplenums,
			String tablenums,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount,
			float _Memberdiscount,
			String _Total,
			String member_name,
			String member_phone,
			String member_balance) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		String dopackage="";
		if("true".equals(ispackage)){
			dopackage="( "+"打包"+" )";
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length())+dopackage);
		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="人数："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){

					//名称
					String name;
					name= bean.getName();
					ret += name;

					ret+="\r\n";

					String price=bean.getPrice();

					ret+=price;
					int l1 = ss_l - getWordCount(price);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = entty.get(i).getNumber();

					double pp = Double.parseDouble(bean.getPrice());

					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);

				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;

					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}
					double nn = entty.get(i).getNumber();
					double pp = Double.parseDouble(bean.getPrice());
					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;
				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
				double nn = entty.get(i).getNumber();
				double pp = Double.parseDouble(bean.getPrice());
				String su;
				if (discount){
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
				}else {
					su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
				}
				//数量
//				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
				ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
			}
//			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
//				ret += "\r\n";
//				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
//			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);


		if (Double.parseDouble(_Total)>0){
			String sssw = "" + _Total;
			int sss_ = LINE_BYTE_SIZE - getWordCount(sssw);
			ret += "原价";
			ret += getPrintChar(" ", sss_ - 6);
			ret += sssw;
			ret += "\r\n";
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		if(hasPay==1){
			ret +="移动支付";
		}else if(hasPay==2){
			ret +="现金支付";
		}else if(hasPay==3){
			ret +="未付款";
		}else if(hasPay==4){
			ret +="现金支付(定金)";
		}else if(hasPay==5){
			ret +="移动支付（定金）";
		}
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
////		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";



//		if (Double.parseDouble(_Total)>0){
//			_Total=_Total.replaceAll("<br/>","\r\n");
//			ret += getPrintLeft("原价：" + _Total, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}

		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			String count="";
			if (_discount!=null&&!_discount.equals("")){
				count=Float.parseFloat(_discount)+"";
			}
			count = count.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(count)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		zhaoling = zhaoling.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + zhaoling, LINE_BYTE_SIZE);
		ret += "\r\n";


		if (SharedUtil.getfalseBoolean("sw_print_member_phone")){
			member_phone = member_phone.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("会员手机号：" + member_phone, LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		if (SharedUtil.getfalseBoolean("sw_print_member_name")){
			member_name = member_name.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("会员名：" + member_name, LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		member_balance = member_balance.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("余额：" + member_balance, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);
		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}
		ret += "\r\n";
		ret += printTitle("欢迎再次光临");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}

	//得到自助收银小票的打印模板
	public static String getSelfServicePrinterMsgnew(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<New_NumberEntty> entty,
			int hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String zhaoling,
			String ispackage,
			String peoplenums,
			String tablenums,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount,
			float _Memberdiscount,
			String _Total) {
		String ret = "";
		String dopackage="";
		if("true".equals(ispackage)){
			dopackage="( "+"打包"+" )";
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length())+dopackage);
		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="人数："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
		//订单号
		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);
			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){
					//名称
					String name;
					name= bean.getName();
					ret += name;
					ret+="\r\n";
					String price=bean.getPrice();
					ret+=price;
					int l1 = ss_l - getWordCount(price);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}
					double nn = entty.get(i).getNumber();
					double pp = Double.parseDouble(bean.getPrice());
					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;
					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}
					double nn = entty.get(i).getNumber();
					double pp = Double.parseDouble(bean.getPrice());
					String su;
					if (discount){
						if (SharedUtil.getString("sw_total")!=null){
							if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
								if (bean.getIs_special_offer()!=null) {
									if (bean.getIs_special_offer().equals("no")) {
										su = SysUtils.priceFormat(TlossUtils.mul(TlossUtils.mul(nn,pp) , _Memberdiscount), false);
									} else {
										su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
									}
								}else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
					//数量
//					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
					ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;
				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
				double nn = entty.get(i).getNumber();
				double pp = Double.parseDouble(bean.getPrice());
				String su;
				if (discount){
					if (SharedUtil.getString("sw_total")!=null){
						if (Boolean.parseBoolean(SharedUtil.getString("sw_total"))){
							if (bean.getIs_special_offer()!=null) {
								if (bean.getIs_special_offer().equals("no")) {
									su = SysUtils.priceFormat(nn * pp * _Memberdiscount, false);
								} else {
									su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
								}
							}else {
								su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
							}
						}else {
							su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
						}
					}else {
						su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
					}
				}else {
					su = SysUtils.priceFormat(TlossUtils.mul(nn,pp), false);
				}
				//数量
//				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);
				ret += getPrintRight(String.format("%.2f",entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
			}
//			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
//				ret += "\r\n";
//				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
//			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		if (Double.parseDouble(_Total)>0){
			String sssw = "" + _Total;
			int sss_ = LINE_BYTE_SIZE - getWordCount(sssw);
			ret += "原价";
			ret += getPrintChar(" ", sss_ - 6);
			ret += sssw;
			ret += "\r\n";
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		if(hasPay==1){
			ret +="移动支付";
		}else if(hasPay==2){
			ret +="现金支付";
		}else if(hasPay==3){
			ret +="未付款";
		}else if(hasPay==4){
			ret +="现金支付(定金)";
		}else if(hasPay==5){
			ret +="移动支付（定金）";
		}
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
////		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";



//		if (Double.parseDouble(_Total)>0){
//			_Total=_Total.replaceAll("<br/>","\r\n");
//			ret += getPrintLeft("原价：" + _Total, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			String count="";
			if (_discount!=null&&!_discount.equals("")){
				count=Float.parseFloat(_discount)+"";
			}
			count = count.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(count)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		zhaoling = zhaoling.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + zhaoling, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += "\r\n";
//		ret += printTitle("产品有质量问题需持此票退货");
//		ret += "\r\n";
//		ret += getPrintLeft("地址：" + "龙游县太平西路275号", LINE_BYTE_SIZE);
		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}
		ret += "\r\n";
		ret += printTitle("欢迎再次光临");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}

	//new得到自助收银小票的打印模板
	public static String getnewSelfServicePrinterMsgg(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Commodity> goodsList,
			List<New_NumberEntty> entty,
			int hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile,
			String zhaoling,
			String ispackage,
			String peoplenums,
			String tablenums,
			boolean reduce,
			String _reduce,
			boolean discount,
			String _discount) {
		String ret = "";

		String dopackage="";
		if("true".equals(ispackage)){
			dopackage="( "+"打包"+" )";
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		if (orderSn!=null&&!orderSn.equals("")){
		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length())+dopackage);
		}
		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="人数："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);

		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			//名称
			String name;
			if(bean.getName().length()>8){
				name= bean.getName().substring(0,9);
			}else {
				name= bean.getName();
			}
			ret += name;

			int l1 = ss_l - getWordCount(name);
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}

			double nn = entty.get(i).getNumber();

			double pp = Double.parseDouble(bean.getPrice());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
			ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

			//小计
			ret += getPrintRight(su, 8);
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		if(hasPay==1){
			ret +="移动支付";
		}else if(hasPay==2){
			ret +="现金支付";
		}else if(hasPay==3){
			ret +="未付款";
		}else if(hasPay==4){
			ret +="现金支付(定金)";
		}else if(hasPay==5){
			ret +="移动支付（定金）";
		}
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);


		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		if (reduce){
			_reduce = _reduce.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("立减优惠：" + String.format("%.2f",Double.parseDouble(_reduce)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}
		if (discount){
			_discount = _discount.replaceAll("<br/>","\r\n");
			ret += getPrintLeft("折扣优惠：" + String.format("%.2f",Double.parseDouble(_discount)), LINE_BYTE_SIZE);
			ret += "\r\n";
		}

		zhaoling = zhaoling.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + zhaoling, LINE_BYTE_SIZE);
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		if (SharedUtil.getString("address")!=null){
			if (!SharedUtil.getString("address").equals("")){
				ret += "\r\n";
				ret += getPrintLeft("地址：" + SharedUtil.getString("address"), LINE_BYTE_SIZE);
			}
		}

		if (SharedUtil.getString("print_remarks")!=null){
			if (!SharedUtil.getString("print_remarks").equals("")){
				ret += "\r\n";
				ret += getPrintLeft( SharedUtil.getString("print_remarks"), LINE_BYTE_SIZE);
			}
		}
		ret += "\r\n";
		ret += printTitle("欢迎再次光临");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}



	//打印出入库加价格
	public static String getSelfServicePrinterMsgg(
			String type,
			String shopName,
			String shopTel,
			String time,
			List<Commodity> goodsList,
			List<ShuliangEntty> entty,
			String orderRemark,
			String peoplenums,
			String tablenums) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		ret += "\r\n";
		type = getPrintCenter(type);
		ret+=type;
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
//		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="店名："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
//		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
//		ret += "\r\n";
		//电话
//		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		ret += "\r\n";
		ret += getPrintLeft("时间：" + time,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    单价";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		double talot=0;
		for(int i = 0; i < goodsList.size(); i++) {
			Commodity bean = goodsList.get(i);

			//名称
			String name;
			if(bean.getName().length()>8){
				name= bean.getName().substring(0,9);
			}else {
				name= bean.getName();
			}
			ret += name;

			int l1 = ss_l - getWordCount(name);
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}

			double nn = entty.get(i).getNumber();

			double pp = Double.parseDouble(bean.getCost());
			String su = SysUtils.priceFormat(nn * pp, false);
			talot=TlossUtils.add(talot,Double.parseDouble(su));
			//数量
			ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

			//小计
			ret += getPrintRight(pp+"", 8);
//			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
//				ret += "\r\n";
//				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
//			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		ret += "\r\n";

		//备注
////		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";

//		String orderRemarkname=orderRemark.substring(0, orderRemark.indexOf(","));
		String orderRemarkphont=orderRemark.substring(orderRemark.indexOf(","));

		String spStr[] = orderRemark.split(",");


		ret += getPrintLeft("备注：" + spStr[0], LINE_BYTE_SIZE);
		if (!spStr[1].equals("")){
			ret += getPrintLeft("电话：" + orderRemarkphont, LINE_BYTE_SIZE);
		}
		ret += getPrintLeft("总件数：" + goodsList.size(), LINE_BYTE_SIZE);
		ret += getPrintLeft("总计：" + talot, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");

		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}


	//打印出入库加价格
	public static String getSelfServicePrinterMsgg(
			String shopName,
			String shopTel,
			String time,
			In_Out_Details in_out_details,
			String orderRemark,
			String peoplenums,
			String tablenums,
			String zongji) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		//商家名称
		ret += "\r\n";

		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="店名："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);

		ret += getPrintLeft("时间：" + time,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < in_out_details.getResponse().getData().size(); i++) {
			//名称
			String name;
			if(in_out_details.getResponse().getData().get(i).getName().length()>8){
				name= in_out_details.getResponse().getData().get(i).getName().substring(0,9);
			}else {
				name= in_out_details.getResponse().getData().get(i).getName();
			}
			ret += name;

			int l1 = ss_l - getWordCount(name);
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}

			double nn = Double.parseDouble(in_out_details.getResponse().getData().get(i).getNums());

			double pp = Double.parseDouble(in_out_details.getResponse().getData().get(i).getCost());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
			ret += getPrintRight(String.valueOf(in_out_details.getResponse().getData().get(i).getNums()), 4);

			//小计
			ret += getPrintRight(su, 8);
//			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
//				ret += "\r\n";
//				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
//			}

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		ret += "\r\n";

		//备注
////		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";

		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("备注：" + orderRemark, LINE_BYTE_SIZE);
		ret += getPrintLeft("总计：" + zongji, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");

		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}



	//打印的出入库
	public static String getInOutPrinterMsg(
			String shopName,
			String orderDate,
			String tel,
			String time,
			List<Commodity> entty,
			List<ShuliangEntty> adats,
			String type,
			String remark,
			String zongjia
	) {
		String ret = "";

		orderDate = getPrintCenter(orderDate);
		ret +=orderDate;
		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
//		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
//		ret += "\r\n";
//		ret += "\r\n";

		//电话


		ret += getPrintLeft("电话："+tel,LINE_BYTE_SIZE);
		ret += "\r\n";
		ret += getPrintLeft("时间：" + time,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		ret += "\r\n";
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
//		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
//		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
//		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss;
		if (type.equals("0")){
			ss = "数量";   //12
		}else {
			ss = "数量";   //12
		}

		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;

		//打印菜品
		for(int i = 0; i < entty.size(); i++) {
//			ret += getPrintChar("-", LINE_BYTE_SIZE);
			Commodity bean = entty.get(i);
			ret += getPrintLeft(bean.getBncode(),LINE_BYTE_SIZE);
			//名称
			ret += bean.getName();
			int l1 = ss_l - getWordCount(bean.getName());
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}
//			String su = SysUtils.priceFormat(nn * pp, false);
			//数量

			ret += getPrintRight(String.valueOf(adats.get(i).getNumber()), 4);

			//小计
//			ret += getPrintRight(su, 8);

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		ret+=getPrintLeft("备注:"+remark,LINE_BYTE_SIZE);
//		String sss = "" + payed;
//		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
//		ret += hasPay ? "移动支付" : "现金支付";
//		ret += getPrintChar(" ", sss_l - 6);
//		ret += sss;
		ret += "\r\n";

//		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
//		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		yingshou = yingshou.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		mobile = mobile.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
//		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}


	//出入库的详情打印
	public static String getInOutPrinterMsg(
			String shopName,
			String orderDate,
			String tel,
			In_Out_Details adats,
			String type,
			String remark
	) {
		String ret = "";

		orderDate = getPrintCenter(orderDate);
		ret +=orderDate;
		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
//		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
//		ret += "\r\n";
//		ret += "\r\n";

		//电话


		ret += getPrintLeft("电话："+tel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
//		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
//		ret += getPrintLeft("时间：" + orderDate,LINE_BYTE_SIZE);
//		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss;
		if (type.equals("0")){
			ss = "数量";   //12
		}else {
			ss = "数量";   //12
		}

		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;

		//打印菜品
		for(int i = 0; i < adats.getResponse().getData().size(); i++) {
//			ret += getPrintChar("-", LINE_BYTE_SIZE);

			ret += getPrintLeft( adats.getResponse().getData().get(i).getName(),LINE_BYTE_SIZE);
			//名称
			ret += adats.getResponse().getData().get(i).getName();
			int l1 = ss_l - getWordCount(adats.getResponse().getData().get(i).getName());
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}
//			String su = SysUtils.priceFormat(nn * pp, false);
			//数量

			ret += getPrintRight(String.valueOf(adats.getResponse().getData().get(i).getNums()), 4);

			//小计
//			ret += getPrintRight(su, 8);

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		ret+=getPrintLeft("备注:"+remark,LINE_BYTE_SIZE);

//		String sss = "" + payed;
//		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
//		ret += hasPay ? "移动支付" : "现金支付";
//		ret += getPrintChar(" ", sss_l - 6);
//		ret += sss;
		ret += "\r\n";
//		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
//		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		yingshou = yingshou.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		mobile = mobile.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
//		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}


	//得到自助收银小票的打印模板
	public static String getSelfServicePrinterMsg(
									   String shopName,
									   String shopTel,
									   String orderSn,
									   String orderDate,
									   ArrayList<Self_Service_GoodsInfo> goodsList,
									   ArrayList<Self_Service_GoodsInfo> entty,
									   int hasPay,
									   double payed,
									   String orderRemark,
									   String yingshou,
									   String mobile,
									   String ispackage,
									   String peoplenums,
									   String tablenums,
									   String num,
									   String notes) {
		String ret = "";

//		ret += getPrintChar("-", (LINE_BYTE_SIZE-num.length())/2);
		ret += getMiddle(num,LINE_BYTE_SIZE);

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		String dopackage="";
		if("true".equals(ispackage)){
			dopackage="( "+"打包"+" )";
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length()));
		ret += "\r\n";
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";
		String people_nums="";
		String table_nums="";
		if(!TextUtils.isEmpty(peoplenums)){
			people_nums="人数："+peoplenums;
		}if(!TextUtils.isEmpty(tablenums)){
			table_nums="桌号："+tablenums;
		}
		//人数，桌号
		ret += getPrintLeft(people_nums +"    " +table_nums,LINE_BYTE_SIZE);
		ret += "\r\n";
		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Self_Service_GoodsInfo bean = goodsList.get(i);

			if (SharedUtil.getString("print_price")!=null){
				if (Boolean.parseBoolean(SharedUtil.getString("print_price"))){

					//名称
					String name;
					name= bean.getName();
					ret += name;

					ret+="\r\n";

					String price=bean.getPrice();

					ret+=price;
					int l1 = ss_l - getWordCount(price);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = Double.parseDouble(entty.get(i).getNumber());

					double pp = Double.parseDouble(bean.getPrice());
					String su = SysUtils.priceFormat(nn * pp, false);
					//数量
					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
					if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
						ret += "\r\n";
						ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
						ret += "\r\n";
					}

				}else {
					//名称
					String name;
					if(bean.getName().length()>8){
						name= bean.getName().substring(0,9);
					}else {
						name= bean.getName();
					}
					ret += name;

					int l1 = ss_l - getWordCount(name);
					for(int j = 0; j < l1; j++) {
						ret += " ";
					}

					double nn = Double.parseDouble(entty.get(i).getNumber());

					double pp = Double.parseDouble(bean.getPrice());
					String su = SysUtils.priceFormat(nn * pp, false);
					//数量
					ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

					//小计
					ret += getPrintRight(su, 8);
					if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
						ret += "\r\n";
						ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
						ret += "\r\n";
					}
				}
			}else {
				//名称
				String name;
				if(bean.getName().length()>8){
					name= bean.getName().substring(0,9);
				}else {
					name= bean.getName();
				}
				ret += name;

				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
				double nn = Double.parseDouble(entty.get(i).getNumber());

				double pp = Double.parseDouble(bean.getPrice());
				String su = SysUtils.priceFormat(nn * pp, false);
				//数量
				ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

				//小计
				ret += getPrintRight(su, 8);
				if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
					ret += "\r\n";
					ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
					ret += "\r\n";
				}
			}


			/**
			//名称
			String name;
			if(bean.getName().length()>8){
				name= bean.getName().substring(0,9);
				ret += name;
				ret += "\r\n";
				ret +=bean.getName().substring(9,bean.getName().length());
				int l1 = ss_l - getWordCount(bean.getName().substring(9,bean.getName().length()));
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
			}else {
				name= bean.getName();
				ret += name;
				int l1 = ss_l - getWordCount(name);
				for(int j = 0; j < l1; j++) {
					ret += " ";
				}
			}
			double nn = Double.parseDouble(entty.get(i).getNumber());

			double pp = Double.parseDouble(bean.getPrice());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
			ret += getPrintRight(String.valueOf(entty.get(i).getNumber()), 4);

			//小计
			ret += getPrintRight(su, 8);
			if(!TextUtils.isEmpty(goodsList.get(i).getNotes())||!TextUtils.isEmpty(goodsList.get(i).getSize())){
				ret += "\r\n";
				ret += getPrintLeft("( "+goodsList.get(i).getSize()+goodsList.get(i).getNotes()+" )", LINE_BYTE_SIZE);
				ret += "\r\n";
			}**/

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		if(hasPay==1){
			ret +="移动支付";
		}else if(hasPay==2){
			ret +="现金支付";
		}else if(hasPay==3){
			ret +="未付款";
		}else if(hasPay==4){
			ret +="现金支付(定金)";
		}else if(hasPay==5){
			ret +="移动支付（定金）";
		}else if (hasPay==6){
			ret +="会员支付";
		}
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";

		notes = notes.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("备注：" + notes, LINE_BYTE_SIZE);
		ret += "\r\n";

//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");

		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}

	//得到自助收银小票的打印模板
	public static byte[] getSelfServicePrinterMsg(
			String shopName,
			String orderDate,
			ArrayList<Self_Service_GoodsInfo> goodsList,
			ArrayList<Self_Service_GoodsInfo> entty,
			String orderRemark,
			String ispackage,
			String tablenums,
			String num,
			String notes) {
		EscCommand esc=new EscCommand();
		esc.addInitializePrinter();
		esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
		String ret = "";

//		ret += getPrintChar("-", (LINE_BYTE_SIZE-num.length())/2);
		ret += getMiddle(num,LINE_BYTE_SIZE);

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";
		String dopackage="";
		if("true".equals(ispackage)){
			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
			esc.addText("打包\n"); // 打印文字
			esc.addPrintAndLineFeed();
		}
		//商家名称
		ret += "\r\n";
		ret += "\r\n";
//		ret += getPrintCenter(orderSn.substring(orderSn.length()-4,orderSn.length()));
		ret += "\r\n";
		ret += "\r\n";
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
		esc.addText(shopName+"\n"); // 打印文字
		if(!TextUtils.isEmpty(tablenums)){
			esc.addText("桌号："+tablenums+"\n"); // 打印文字
		}
		esc.addPrintAndLineFeed();
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
		esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
		if (StringUtils.isNumber1(orderDate)){
			orderDate=TimeZoneUtil.getTime1(Long.parseLong(orderDate)*1000);
		}
		esc.addText("下单时间：" + orderDate+"\n");
//		esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
//		esc.addText("名称");
//		esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);// 设置打印右对齐
//		esc.addText("数量\n");
		//打印菜品
		for(int i = 0; i < goodsList.size(); i++) {
			Self_Service_GoodsInfo bean = goodsList.get(i);
					//名称
					String name;
					name= bean.getName();
					esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
					esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
					String name1;
					if(bean.getName().length()>8){
							name1= bean.getName().substring(0,9);
						}else {
							name1= bean.getName();
						}
						esc.addText(name1+"  ");
// 					esc.addSetAbsolutePrintPosition((short)6);
//					esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);// 设置打印右对齐
					esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON);// 设置为倍高倍宽
					esc.addText(entty.get(i).getNumber()+"份");
					esc.addPrintAndLineFeed();
			if (!bean.getNotes().equals("")&&bean.getNotes()!=null){
				esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON);// 设置为倍高倍宽
				esc.addText("("+bean.getNotes()+")");
				esc.addPrintAndLineFeed();
			}
		}
//		esc.addPrintAndLineFeed();
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON);// 设置为倍高倍宽
		esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
		//备注
		esc.addText("订单备注:"+notes+"\n\n\n\n\n");
		esc.addPrintAndFeedLines((byte) 3);
		esc.addCutPaper();
		esc.addSound((byte)3,(byte) 100);
		Vector<Byte> datas = esc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);

		return bytes;
	}


	//得到自助收银小票的打印模板
	public static String getSelfServicePrinterMsg(
			String shopName,
			String shopTel,
			String orderSn,
			String orderDate,
			List<Refund_entty> entty,
			boolean hasPay,
			double payed,
			String orderRemark,
			String yingshou,
			String mobile) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
		ret += "\r\n";
		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话：" + shopTel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("下单时间：" + orderDate,LINE_BYTE_SIZE);
		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量    小计";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//打印菜品
		for(int i = 0; i < entty.size(); i++) {
			Refund_entty bean = entty.get(i);

			//名称
			ret += bean.getName();
			int l1 = ss_l - getWordCount(bean.getName());
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}

			double nn = Double.parseDouble(entty.get(i).getNums());

			double pp = Double.parseDouble(bean.getPrice());
			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
			ret += getPrintRight(String.valueOf(entty.get(i).getNums()), 4);

			//小计
			ret += getPrintRight(su, 8);

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		String sss = "" + payed;
		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
		ret += hasPay ? "移动支付" : "现金支付";
		ret += getPrintChar(" ", sss_l - 6);
		ret += sss;
		ret += "\r\n";
		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
		ret += "\r\n";
		yingshou = yingshou.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
		ret += "\r\n";
		mobile = mobile.replaceAll("<br/>","\r\n");
		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");

		ret += "\n\n";
		return ret;
	}


	//得到自助收银小票的打印模板
	public static String getMoneyPrinterMsg(
			String shopName,
			String orderDate,
			String tel,
			List<Commodity> entty,
			List<Integer> adats
				) {
		String ret = "";

		//抬头
//		ret += "----------";
//		ret += index;
//		int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
//		ret += getPrintChar(" ", a);
//		ret += shippingStr;
//		ret += "----------";
//		ret += "\r\n";

		//商家名称
//		ret += "\r\n";
		shopName = getPrintCenter(shopName);
		ret += shopName;
//		if(!TextUtils.isEmpty(deskNo)) {
//			ret += "\r\n";
//			ret += getPrintCenter("桌号：" + deskNo);
//		}
//		ret += "\r\n";
//		ret += "\r\n";

		//电话
		ret += getPrintLeft("电话："+tel,LINE_BYTE_SIZE);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
		//订单号
//		ret += getPrintLeft("订单号：" + orderSn,LINE_BYTE_SIZE);
		//下单时间
		ret += getPrintLeft("报货时间：" + orderDate,LINE_BYTE_SIZE);
//		ret += "\r\n";
		//分隔符
		ret += getPrintChar("-", LINE_BYTE_SIZE);
		//商品列表
		String ss = "数量";   //12
		int ss_l = LINE_BYTE_SIZE - getWordCount(ss);
		ret += "名称";
		ret += getPrintChar(" ", ss_l - 4);
		ret += ss;

		//打印菜品
		for(int i = 0; i < entty.size(); i++) {
//			ret += getPrintChar("-", LINE_BYTE_SIZE);
			Commodity bean = entty.get(i);
			ret += getPrintLeft(bean.getBncode(),LINE_BYTE_SIZE);
			//名称
			ret += bean.getName();
			int l1 = ss_l - getWordCount(bean.getName());
			for(int j = 0; j < l1; j++) {
				ret += " ";
			}
			int nn = adats.get(i);

			double pp = Double.parseDouble(bean.getPrice());
//			String su = SysUtils.priceFormat(nn * pp, false);
			//数量
			ret += getPrintRight(String.valueOf(adats.get(i)), 4);

			//小计
//			ret += getPrintRight(su, 8);

//			if(!TextUtils.isEmpty(bean.getAltc())) {
//				//属性
//				ret += getPrintLeft(bean.getAltc(), LINE_BYTE_SIZE);
//			}
			ret += getPrintChar("-", LINE_BYTE_SIZE);
		}

		ret+=getPrintLeft("商品数量:"+entty.size(),LINE_BYTE_SIZE);

//		String sss = "" + payed;
//		int sss_l = LINE_BYTE_SIZE - getWordCount(sss)-2;
//		ret += hasPay ? "移动支付" : "现金支付";
//		ret += getPrintChar(" ", sss_l - 6);
//		ret += sss;
		ret += "\r\n";
//		ret += getPrintChar("-", LINE_BYTE_SIZE);

		//备注
//		orderRemark = orderRemark.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("实收：" + orderRemark, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		yingshou = yingshou.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("应收：" + yingshou, LINE_BYTE_SIZE);
//		ret += "\r\n";
//		mobile = mobile.replaceAll("<br/>","\r\n");
//		ret += getPrintLeft("找零：" + mobile, LINE_BYTE_SIZE);
		ret += "\r\n";
//		if (hasAddress) {
//			ret += getPrintLeft(consignee + "  " + mobile, LINE_BYTE_SIZE);
//			ret += "\r\n";
//			ret += getPrintLeft(address, LINE_BYTE_SIZE);
//			ret += "\r\n";
//		}
//		ret += getPrintChar("-", LINE_BYTE_SIZE);
//		ret += printTitle("易星生活,有你真好！");
		ret += "\n\n";
		ret += "\n\n";
		return ret;
	}



	/**
	 * 排版居中内容(以':'对齐)
	 * 
	 * 例：姓名：李白
	 * 	       病区：5A病区
	 * 	  住院号：11111
	 * 
	 * @param
	 * @return
	 */
	public static String printMiddleMsg(LinkedHashMap<String, String> middleMsgMap) {
		sb.delete(0, sb.length());
		String separated = ":";
		int leftLength = (LINE_BYTE_SIZE - getBytesLength(separated)) / 2;
		for (Entry<String, String> middleEntry : middleMsgMap.entrySet()) {
			for (int i = 0; i < (leftLength - getBytesLength(middleEntry.getKey())); i++) {
				sb.append(" ");
			}
			sb.append(middleEntry.getKey() + "：" + middleEntry.getValue());
		}
		return sb.toString();
	}

	/**
	 * 排版左右对称内容(以':'对齐)
	 * 
	 * 例：姓名：李白	住院号：111111
	 * 	       病区：5A病区	     科室：五官科
	 * 	       体重：130kg
	 * 
	 * 
	 * @param leftMsgMap	左边部分要打印内容	左边内容大小可大于右边内容大小  反之右边过大时会忽略内容
	 * @param rightMsgMap	右边部分要打印内容
	 * @return
	 */
	public static String printSymmetryMSG(LinkedHashMap<String, String> leftMsgMap, LinkedHashMap<String, String> rightMsgMap) {
		sb.delete(0, sb.length());
		int leftPrefixLength = getMaxLength(leftMsgMap.keySet().toArray());
		int rightValueLength = getMaxLength(rightMsgMap.values().toArray());
		Object rightMsgKeys[] = rightMsgMap.keySet().toArray();
		int position = 0;
		for (Entry<String, String> leftEntry : leftMsgMap.entrySet()) {
			String leftMsgPrefix = leftEntry.getKey();
			String leftMsgValue = leftEntry.getValue();
			for (int leftI = 0; leftI < (leftPrefixLength - getBytesLength(leftMsgPrefix)); leftI++) {
				sb.append(" ");
			}
			String leftMsg = leftMsgPrefix + "：" + leftMsgValue;
			sb.append(leftMsg);

			if (position > rightMsgKeys.length - 1)
				continue;
			int leftLength = leftPrefixLength + getBytesLength("：" + leftMsgValue);
			String rightMsgPrefix = rightMsgKeys[position] + "：";
			int rightLength = getBytesLength(rightMsgPrefix) + rightValueLength;
			String rightMsgValue = rightMsgMap.get(rightMsgKeys[position]);

			for (int middle = 0; middle < (LINE_BYTE_SIZE - leftLength - rightLength); middle++) {
				sb.append(" ");
			}
			sb.append(rightMsgPrefix + rightMsgValue);
			position++;
		}
		return sb.toString();
	}

	/**
	 * 打印订餐单 (左中右对称)
	 * 
	 * 例:   菜名                           数量            单价
	 *     早餐：
	 *       豆沙包                         1      3.0
	 *       蛋                                   1      1.5
	 *     午餐：
	 *       包子                              3      11.0
	 *     晚餐：
	 *     	 土豆                                2      4.1
	 * @param menuMsgMap 	Key为餐次 Value为 菜谱字符串数组    格式为：豆沙包$数量$单价
	 * @return
	 */
	public static String printMenuMSG(LinkedHashMap<String, LinkedList<String>> menuMsgMap) {
		sb.delete(0, sb.length());

		String menus[] = null;
		List<String> menuNames = new ArrayList<String>();
		List<String> menuPrices = new ArrayList<String>();
		for (List<String> strList : menuMsgMap.values()) {
			for (String str : strList) {
				if (str.contains(SEPARATOR)) {
					menus = str.split("[" + SEPARATOR + "]");
					if (menus != null && menus.length != 0) {
						menuNames.add(menus[0]);
						menuPrices.add(menus[2]);
					}
				}
			}
		}

		String menuNameTxt = "名字";
		String numTxt = "数量";
		String priceTxt = "单价\n";

		int leftPrefixLength = getMaxLength(menuNames.toArray());
		int rightPrefixLength = getMaxLength(menuPrices.toArray());
		if (rightPrefixLength < getBytesLength(priceTxt))
			rightPrefixLength = getBytesLength(priceTxt);

		int leftMiddleNameLength = (leftPrefixLength - getBytesLength(menuNameTxt)) / 2;
		for (int i = 0; i < leftMiddleNameLength; i++) {
			sb.append(" ");
		}
		sb.append(menuNameTxt);
		int middleNumTxtLength = (LINE_BYTE_SIZE - leftPrefixLength - rightPrefixLength - getBytesLength(numTxt)) / 2;
		for (int i = 0; i < middleNumTxtLength + leftMiddleNameLength; i++) {
			sb.append(" ");
		}
		sb.append(numTxt);
		int middlePriceTxtLength = (rightPrefixLength - getBytesLength(priceTxt)) / 2;
		for (int i = 0; i < middleNumTxtLength + middlePriceTxtLength; i++) {
			sb.append(" ");
		}
		sb.append(priceTxt);

		for (Entry<String, LinkedList<String>> entry : menuMsgMap.entrySet()) {
			if (!"".equals(entry.getKey()))
				sb.append(entry.getKey() + "\n");
			for (String menu : entry.getValue()) {
				if (menu.contains(SEPARATOR)) {
					menus = menu.split("[" + SEPARATOR + "]");
					if (menus != null && menus.length != 0) {
						sb.append(menus[0]);
						for (int i = 0; i < (leftPrefixLength - getBytesLength(menus[0]) + middleNumTxtLength + getBytesLength(numTxt) / 2 - 1); i++) {
							sb.append(" ");
						}
						sb.append(menus[1]);
						for (int i = 0; i < (middleNumTxtLength + getBytesLength(numTxt) / 2 + 1 - getBytesLength(menus[1]) + middlePriceTxtLength); i++) {
							sb.append(" ");
						}
						sb.append(menus[2] + "\n");
					}
				} else { // 不包含分隔符 直接打印
					for (int i = 0; i < LINE_BYTE_SIZE / getBytesLength(menu) - getBytesLength("\n"); i++) {
						sb.append(menu);
					}
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获取最大长度
	 * @param msgs
	 * @return
	 */
	public static int getMaxLength(Object[] msgs) {
		int max = 0;
		int tmp;
		for (Object oo : msgs) {
			tmp = getBytesLength(oo.toString());
			if (tmp > max) {
				max = tmp;
			}
		}
		return max;
	}
	public static final int LEFT_TEXT_MAX_LENGTH = 5;
	/**
	 * 获取数据长度
	 * @param msg
	 * @return
	 */
	@SuppressLint("NewApi")
	public static int getBytesLength(String msg) {
		return msg.getBytes(Charset.forName("GB2312")).length;
	}

	public static String printThreeData(String leftText, String middleText, String rightText) {
		StringBuilder sb = new StringBuilder();
		// 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
		if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
			leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
		}
		int leftTextLength = getBytesLength(leftText);
		int middleTextLength = getBytesLength(middleText);
		int rightTextLength = getBytesLength(rightText);

		sb.append(leftText);
		// 计算左侧文字和中间文字的空格长度
		int marginBetweenLeftAndMiddle = LINE_BYTE_SIZE - leftTextLength - middleTextLength / 2;

		for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
			sb.append(" ");
		}
		sb.append(middleText);

		// 计算右侧文字和中间文字的空格长度
		int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - middleTextLength / 2 - rightTextLength;

		for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
			sb.append(" ");
		}

		// 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
		sb.delete(sb.length() - 1, sb.length()).append(rightText);
		return sb.toString();
	}
}
