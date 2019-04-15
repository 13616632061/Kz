package Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 输入框工具类
 * Created by Administrator on 2016/12/22.
 */

public class SetEditTextInput {
    /**
     * 小数点后两位
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText) {
        //输入框的输入监听
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                StringBuilder sb = new StringBuilder();
                CharSequence a[]=s.toString().split("[+]");
                for(int i=0;i<a.length;i++){
                    if(a[i].length()<8) {
                        a[i] = a[i].subSequence(0, a[i].length());
                        if (a[i].toString().contains(".")) {
                            if (a[i].length() - 1 - a[i].toString().indexOf(".") > 2) {
                                a[i] = a[i].toString().subSequence(0,
                                        a[i].toString().indexOf(".") + 3);
                                sb.append(a[i] + "+");
                                if (sb.substring(sb.length() - 1).equals("+")) {
                                    sb.subSequence(0, sb.length() - 1);
                                    editText.setText(sb.subSequence(0, sb.length() - 1));
                                } else {
                                    editText.setText(sb);
                                }

                            }
                        }
                        if (a[i].toString().trim().substring(0).equals(".")) {
                            a[i] = "0" + a[i];
                            if (sb.substring(sb.length() - 1).equals("+")) {
                                sb.subSequence(0, sb.length() - 1);
                                editText.setText(sb.subSequence(0, sb.length() - 1));
                            } else {
                                editText.setText(sb);
                            }
                        }
                        if (a[i].toString().startsWith("0")
                                && a[i].toString().trim().length() > 1 && a[i].subSequence(1, 2).equals(0)) {
                            if (!a[i].toString().substring(1, 2).equals(".")) {
                                sb.append(a[i] + "+");
                                if (sb.substring(sb.length() - 1).equals("+")) {
                                    sb.subSequence(0, sb.length() - 1);
                                    editText.setText(sb.subSequence(0, sb.length() - 1));
                                } else {
                                    editText.setText(sb);
                                }
                            }
                        }
                        if(a[i].length()>1&&a[i].toString().startsWith("0")&&a[i].toString().substring(1, 2).equals("0")){
                            a[i] = "0";
                            sb.append(a[i] + "+");
                            if (sb.substring(sb.length() - 1).equals("+")) {
                                sb.subSequence(0, sb.length() - 1);
                                editText.setText(sb.subSequence(0, sb.length() - 1));
                            } else {
                                editText.setText(sb);
                            }
                        }
                        /**
                         * 判断只能输入一个小数点
                         */
                        if (a[i].toString().indexOf(".") >= 0) {
                            if (a[i].toString().indexOf(".", a[i].toString().indexOf(".") + 1) > 0) {
//                        tv_numOfChar.setText("已经输入\".\"不能重复输入");
                                a[i] = a[i].toString().substring(0, a[i].toString().length() - 1);
                                sb.append(a[i] + "+");
                                if (sb.substring(sb.length() - 1).equals("+")) {
                                    sb.subSequence(0, sb.length() - 1);
                                    editText.setText(sb.subSequence(0, sb.length() - 1));
                                } else {
                                    editText.setText(sb);
                                }
                                return;
                            }

                        }
                        sb.append(a[i] + "+");
                    }
                }
//                editText.setText(sb);
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().lastIndexOf(".") > 2) {
//                        s = s.toString().subSequence(0,
//                                s.toString().lastIndexOf(".") + 3);
//                        editText.setText(s);
//                        editText.setSelection(s.length());
//                    }
//                }
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    editText.setText(s);
//                    editText.setSelection(2);
//                }

//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        editText.setText(s.subSequence(0, 1));
//                        editText.setSelection(1);
//                        return;
//                    }
//                }
//                /**
//                 * 判断只能输入一个小数点
//                 */
//                if (editText.getText().toString().indexOf(".") >= 0) {
//                    if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
////                        tv_numOfChar.setText("已经输入\".\"不能重复输入");
//                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
//                        editText.setSelection(editText.getText().toString().length());
//                    }

//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }
    /**
     * 限制只能输入字母、数字和汉字
     * @return
     * @throws PatternSyntaxException
     */
    public static void setStringFilter(final EditText editText)throws PatternSyntaxException{
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String editable=editText.getText().toString();
                // 只允许字母、数字和汉字
                String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
                Pattern p   =   Pattern.compile(regEx);
                Matcher m   =   p.matcher(editable);
                String str=m.replaceAll("").trim();

                if(!editable.equals(str)){
                    editText.setText(str);
                    //设置新的光标所在位置
                    editText.setSelection(str.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }

    /**
     * 小数点后两位
     *
     * @param editText
     */
    public static void setEtPoint(final EditText editText) {
        //输入框的输入监听
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().lastIndexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().lastIndexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                /**
                 * 判断只能输入一个小数点
                 */
                if (editText.getText().toString().indexOf(".") >= 0) {
                    if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
//                        tv_numOfChar.setText("已经输入\".\"不能重复输入");
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    /**
     * 金额输入框中的内容限制（最大：小数点前五位，小数点后2位）
     * @param edt
     */
    public static void judgeNumber(final TextView edt){

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = edt.getEditableText().toString();
                int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
                if (posDot <= 0) {//不包含小数点
                    if (temp.length() <= 5) {
                        return;//小于五位数直接返回
                    } else {
                        edt.getEditableText().delete(5, 6);//大于五位数就删掉第六位（只会保留五位）
                        return;
                    }
                }
                if (temp.length() - posDot - 1 > 2)//如果包含小数点
                {
                    edt.getEditableText().delete(posDot + 3, posDot + 4);//删除小数点后的第三位
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
            * 金额输入框中的内容限制（最大：小数点前五位，小数点后2位）
            * @param edt
    */
    public static void judgeNumber(final EditText edt){

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = edt.getEditableText().toString();
                int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
                if (posDot <= 0) {//不包含小数点
                    if (temp.length() <= 5) {
                        return;//小于五位数直接返回
                    } else {
                        edt.getEditableText().delete(5, 6);//大于五位数就删掉第六位（只会保留五位）
                        return;
                    }
                }
                if (temp.length() - posDot - 1 > 2)//如果包含小数点
                {
                    edt.getEditableText().delete(posDot + 3, posDot + 4);//删除小数点后的第三位
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
