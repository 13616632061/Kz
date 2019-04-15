package Utils;

import java.util.ArrayList;
import java.util.ListIterator;

import Entty.Commodity;
import Entty.Market_entty;

/**
 * Created by admin on 2017/5/25.
 */
public class SortUtils {

    //    库存得排序

    //    进价得排序


    //    销售价


    //商品总计排序
//    public static void sort4(List<Market_entty> data, boolean bl) {
//        int len = data.size();
//        for (int i = 0; i < len; i++) {
//            Market_entty temp = new Market_entty();
//            boolean isExchanged = false;
//            for (int j = len - 1; j > i; j--) {
//                if (bl) {
//                    if (Double.parseDouble(data.get(j - 1).getTotal()) > Double.parseDouble(data.get(j).getTotal())) {
//                        temp = data.get(j);
//                        data.set(j, data.get(j - 1));
//                        data.set(j - 1, temp);
//                    }
//                } else {
//                    if (Double.parseDouble(data.get(j - 1).getTotal()) < Double.parseDouble(data.get(j).getTotal())) {
//                        temp = data.get(j);
//                        data.set(j, null);
//                        data.set(j, data.get(j - 1));
//                        data.set(j - 1, null);
//                        data.set(j - 1, temp);
//                    }
//                }
//            }
//        }
//    }

    public static ArrayList<Commodity> upsales_num(ArrayList<Commodity> bookList, boolean bl) {
        Commodity[] array = bookList.toArray(new Commodity[bookList.size()]);
        Commodity temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Float.parseFloat(array[j - 1].getStore()) < Float.parseFloat(array[j].getStore())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Float.parseFloat(array[j - 1].getStore()) > Float.parseFloat(array[j].getStore())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Commodity> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Commodity> sort2(ArrayList<Commodity> bookList, boolean bl) {
        Commodity[] array = bookList.toArray(new Commodity[bookList.size()]);
        Commodity temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Float.parseFloat(array[j - 1].getPrice()) < Float.parseFloat(array[j].getPrice())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Float.parseFloat(array[j - 1].getPrice()) > Float.parseFloat(array[j].getPrice())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Commodity> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Commodity> sort3(ArrayList<Commodity> bookList, boolean bl) {
        Commodity[] array = bookList.toArray(new Commodity[bookList.size()]);
        Commodity temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Float.parseFloat(array[j - 1].getCost()) < Float.parseFloat(array[j].getCost())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Float.parseFloat(array[j - 1].getCost()) > Float.parseFloat(array[j].getCost())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Commodity> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }


    public static ArrayList<Market_entty> sort8(ArrayList<Market_entty> bookList, boolean bl) {
        Market_entty[] array = bookList.toArray(new Market_entty[bookList.size()]);
        Market_entty temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Double.parseDouble(array[j-1].getNums()) < Double.parseDouble(array[j].getNums())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Double.parseDouble(array[j-1].getNums()) > Double.parseDouble(array[j].getNums())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Market_entty> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Market_entty> sort4(ArrayList<Market_entty> bookList, boolean bl) {
        Market_entty[] array = bookList.toArray(new Market_entty[bookList.size()]);
        Market_entty temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Float.parseFloat(array[j - 1].getTotal()) < Float.parseFloat(array[j].getTotal())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Float.parseFloat(array[j - 1].getTotal()) > Float.parseFloat(array[j].getTotal())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Market_entty> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Market_entty> sort5(ArrayList<Market_entty> bookList, boolean bl) {
        Market_entty[] array = bookList.toArray(new Market_entty[bookList.size()]);
        Market_entty temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (Float.parseFloat(array[j - 1].getTotal()) < Float.parseFloat(array[j].getTotal())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (Float.parseFloat(array[j - 1].getTotal()) > Float.parseFloat(array[j].getTotal())) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Market_entty> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Market_entty> sort6(ArrayList<Market_entty> bookList, boolean bl) {
        Market_entty[] array = bookList.toArray(new Market_entty[bookList.size()]);
        Market_entty temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (bl){
                    if (TlossUtils.sub(Double.parseDouble(array[j - 1].getPrice()), Double.parseDouble(array[j - 1].getCost()))< TlossUtils.sub(Double.parseDouble(array[j - 1].getPrice()), Double.parseDouble(array[j - 1].getCost()))) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }else {
                    if (TlossUtils.sub(Double.parseDouble(array[j - 1].getPrice()), Double.parseDouble(array[j - 1].getCost()))> TlossUtils.sub(Double.parseDouble(array[j - 1].getPrice()), Double.parseDouble(array[j - 1].getCost()))) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
                }

            }
        }
        ListIterator<Market_entty> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    //快捷栏的排序
    public static ArrayList<Commodity> sort7(ArrayList<Commodity> bookList) {
        Commodity[] array = bookList.toArray(new Commodity[bookList.size()]);
        Commodity temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                    if (array[j - 1].getPosition() > array[j].getPosition()) {
                        temp = array[j];
                        array[j] = array[j - 1];
                        array[j - 1] = temp;
                    }
            }
        }
        ListIterator<Commodity> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }

    public static ArrayList<Commodity> sort8(ArrayList<Commodity> bookList) {
        Commodity[] array = bookList.toArray(new Commodity[bookList.size()]);
        Commodity temp = null;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (Integer.parseInt(array[j - 1].getCook_position()) > Integer.parseInt(array[j].getCook_position())) {
                    temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
        }
        ListIterator<Commodity> i = bookList.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
}

