package Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.gprinter.command.GpUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.Custom_Entty;

/**
 * Created by admin on 2017/6/14.
 */
public class Bitmap_Utils {

    public static Bitmap appendTextToPicture(final String picPath, final String msg) {
//        具有指定宽度和高度可变的位图,它的初始密度可以调用getDensity()  
        final int TXT_SIZE = 24;
        Bitmap bmp = BitmapFactory.decodeFile(picPath);
        final int y_offset = 5;
        int heigth = bmp.getHeight() + y_offset + TXT_SIZE;
        final int max_width = bmp.getWidth();
        List<String> buf = new ArrayList<String> ();
        String lineStr = "";
        Paint p = new Paint();
        Typeface font = Typeface.create("宋体",Typeface.BOLD);
        p.setColor(Color.BLACK);
        p.setTypeface(font);
        p.setTextSize(TXT_SIZE);
        for(int i=0;i<msg.length();){
            if(Character.getType(msg.charAt(i)) == Character.OTHER_LETTER) {
                // 如果这个字符是一个汉字  
                if ((i + 1) < msg.length()) {
                    lineStr += msg.substring(i, i + 2);
                    }
                i=i+2;
                }else{
                lineStr+=msg.substring(i,i+1);
                i++;
                }
            float[] ws=new float[lineStr.length()];
            int wid = p.getTextWidths(lineStr, ws);
            if(wid>max_width){
                buf.add(lineStr);
                lineStr = "";
                heigth += (TXT_SIZE + y_offset);
                }
            if (i >= msg.length()) {
                heigth += (TXT_SIZE + y_offset);
                break;
                }
            }

        Bitmap canvasBmp = Bitmap.createBitmap(max_width,heigth+TXT_SIZE,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(canvasBmp);
        canvas.drawColor(Color.WHITE);
        float y = y_offset + TXT_SIZE;
        for(String str : buf) {
            canvas.drawText(str,0,y,p);
            y += (TXT_SIZE + y_offset);
            }
        canvas.drawBitmap(bmp, 0, y, p);
        return canvasBmp;
    }


    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Bitmap getBitmap(String str,int testsize){
        Bitmap bitmap = Bitmap.createBitmap(220, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#ffffff"));
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);// 若设置为center，则文本左半部分显示不全 paint.setColor(Color.RED);
        paint.setAntiAlias(true);// 消除锯齿
        paint.setTextSize(testsize);
        Typeface font = Typeface.create("宋体",Typeface.BOLD);
        paint.setTypeface(font);
        canvas.drawText(str, 10, 100, paint) ;
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }


    public static byte[] addBitmap(int x, int y, com.gprinter.command.LabelCommand.BITMAP_MODE mode, int nWidth, Bitmap b) {
        byte[] codecontent=null;
        byte[] s=null;
        if(b != null) {
            int width = (nWidth + 7) / 8 * 8;
            int height = b.getHeight() * width / b.getWidth();
            Log.d("BMP", "bmp.getWidth() " + b.getWidth());
            Bitmap grayBitmap = GpUtils.toGrayscale(b);
            Bitmap rszBitmap = GpUtils.resizeImage(grayBitmap, width, height);
            byte[] src = GpUtils.bitmapToBWPix(rszBitmap);
            height = src.length / width;
            width /= 8;
            String str = "BITMAP " + x + "," + y + "," + width + "," + height + "," + mode.getValue() + ",";
            s=addStrToCommand(str);
            codecontent = GpUtils.pixToLabelCmd(src);
        }
        return addBytes(s,codecontent);
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    private static byte[] addStrToCommand(String str) {
        byte[] bs = null;
        if(!str.equals("")) {
            try {
                bs = str.getBytes("GB2312");
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }

        }
        return bs;
    }

    public static Bitmap fromText( String text,float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);// 消除锯齿
        Typeface font = Typeface.create("宋体",Typeface.BOLD);
        paint.setTypeface(font);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int width = (int)paint.measureText(text);
        int height = fm.descent - fm.ascent;
        if (width==0){
            width=1;
            height=1;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#ffffff"));
        canvas.drawText(text, 0, fm.leading - fm.ascent, paint);
        canvas.save();
        return bitmap;
    }


    public static Bitmap getBitmap(Commodity adats, Custom_Entty custom_entty, String str, int testsize){
        Bitmap bitmap = Bitmap.createBitmap(400, 260, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#ffffff"));
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);// 若设置为center，则文本左半部分显示不全 paint.setColor(Color.RED);
        paint.setAntiAlias(true);// 消除锯齿
        paint.setTextSize(testsize);
        Typeface font = Typeface.create("宋体",Typeface.BOLD);
        paint.setTypeface(font);
//        canvas.drawText(str, 10, 260, paint) ;
        if (custom_entty.getShopY() != null) {
            if (!custom_entty.getShopY().equals("")) {
                canvas.drawText(SharedUtil.getString("name"), Integer.parseInt(custom_entty.getShopY())-10, Integer.parseInt(custom_entty.getShopX())+10, paint) ;
            }
        }
        if (custom_entty.getNameY() != null) {
            if (!custom_entty.getNameY().equals("")) {
                canvas.drawText(adats.getName(), Integer.parseInt(custom_entty.getNameY())-10, Integer.parseInt(custom_entty.getNameX())+10, paint) ;
            }
        }
        if (custom_entty.getPriceY() != null) {
            if (!custom_entty.getPriceY().equals("")) {
                canvas.drawText(adats.getPrice(), Integer.parseInt(custom_entty.getPriceY())-10, Integer.parseInt(custom_entty.getPriceX())+10, paint) ;
            }
        }
        if (custom_entty.getMemberpriceY() != null) {
            if (!custom_entty.getMemberpriceY().equals("")) {
                canvas.drawText(adats.getMember_price(), Integer.parseInt(custom_entty.getMemberpriceY())-10, Integer.parseInt(custom_entty.getMemberpriceX())+10, paint) ;
            }
        }

        if (custom_entty.getSpecificationsY() != null) {
            if (!custom_entty.getSpecificationsY().equals("")) {
                canvas.drawText(adats.getSpecification(), Integer.parseInt(custom_entty.getSpecificationsY())-10, Integer.parseInt(custom_entty.getSpecificationsX())+10, paint) ;
            }
        }
        if (custom_entty.getCompanyY() != null) {
            if (!custom_entty.getCompanyY().equals("")) {
                canvas.drawText(adats.getUnit(), Integer.parseInt(custom_entty.getCompanyY())-10, Integer.parseInt(custom_entty.getCompanyX())+10, paint) ;
            }
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        Bitmap bitmap1 = Yasoutup(bitmap);
        return bitmap1;
    }

     public static void FormatBMP(Bitmap bitmap)
    {
        if (bitmap != null) {
            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int[] pixels=new int[w*h];
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h);//取得BITMAP的所有像素点
            byte[] rgb = addBMP_RGB_888(pixels,w,h);
            byte[] header = addBMPImageHeader(62+rgb.length);
            byte[] infos = addBMPImageInfosHeader(w, h,rgb.length);
            byte[] colortable = addBMPImageColorTable();
            byte[] buffer = new byte[62 + rgb.length];

            System.arraycopy(header, 0, buffer, 0, header.length);
             System.arraycopy(infos, 0, buffer, 14, infos.length);
             System.arraycopy(colortable, 0, buffer, 54, colortable.length);
             System.arraycopy(rgb, 0, buffer, 62, rgb.length);
             try {
                 FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/Download/"+"文字图片.bmp");
                 fos.write(buffer);
                 } catch (FileNotFoundException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
                 }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                }
            }
        }


    /**
     * 旋转bitmap图片
     * @param bm 要旋转的图片
     * @param orientationDegree 旋转角度
     * @return
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }


    /**
     * 压缩图片
     * @param bit
     * @return
     */
    public static Bitmap Yasoutup(Bitmap bit){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] bytes = baos.toByteArray();
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bm;
    }

    // BMP文件头
        private static byte[] addBMPImageHeader(int size) {
        byte[] buffer = new byte[14];
        buffer[0] = 0x42;
        buffer[1] = 0x4D;
        buffer[2] = (byte) (size >> 0);
        buffer[3] = (byte) (size >> 8);
         buffer[4] = (byte) (size >> 16);
         buffer[5] = (byte) (size >> 24);
         buffer[6] = 0x00;
         buffer[7] = 0x00;
         buffer[8] = 0x00;
         buffer[9] = 0x00;
//  buffer[10] = 0x36;
         buffer[10] = 0x3E;
         buffer[11] = 0x00;
         buffer[12] = 0x00;
         buffer[13] = 0x00;
         return buffer;
        }

    // BMP文件信息头
     private static byte[] addBMPImageInfosHeader(int w, int h, int size) {

         Log.i("_DETEST_", "size="+size);
         byte[] buffer = new byte[40];
         buffer[0] = 0x28;
         buffer[1] = 0x00;
         buffer[2] = 0x00;
         buffer[3] = 0x00;

         buffer[4] = (byte) (w >> 0);
         buffer[5] = (byte) (w >> 8);
         buffer[6] = (byte) (w >> 16);
          buffer[7] = (byte) (w >> 24);

         buffer[8] = (byte) (h >> 0);
         buffer[9] = (byte) (h >> 8);
         buffer[10] = (byte) (h >> 16);
         buffer[11] = (byte) (h >> 24);
         buffer[12] = 0x01;
         buffer[13] = 0x00;

         buffer[14] = 0x01;
         buffer[15] = 0x00;

         buffer[16] = 0x00;
         buffer[17] = 0x00;
         buffer[18] = 0x00;
         buffer[19] = 0x00;

         buffer[20] = (byte) (size >> 0);
         buffer[21] = (byte) (size >> 8);
         buffer[22] = (byte) (size >> 16);
         buffer[23] = (byte) (size >> 24);

//  buffer[24] = (byte) 0xE0;
//  buffer[25] = 0x01;
         buffer[24] = (byte) 0xC3;
         buffer[25] = 0x0E;
         buffer[26] = 0x00;
         buffer[27] = 0x00;
        //  buffer[28] = 0x02;
//  buffer[29] = 0x03;
         buffer[28] = (byte) 0xC3;
         buffer[29] = 0x0E;
          buffer[30] = 0x00;
         buffer[31] = 0x00;

         buffer[32] = 0x00;
         buffer[33] = 0x00;
          buffer[34] = 0x00;
         buffer[35] = 0x00;

         buffer[36] = 0x00;
          buffer[37] = 0x00;
        buffer[38] = 0x00;
        buffer[39] = 0x00;
         return buffer;
         }

    private static byte[] addBMPImageColorTable() {
         byte[] buffer = new byte[8];
         buffer[0] = (byte) 0xFF;
         buffer[1] = (byte) 0xFF;
         buffer[2] = (byte) 0xFF;
         buffer[3] = 0x00;

         buffer[4] = 0x00;
         buffer[5] = 0x00;
         buffer[6] = 0x00;
         buffer[7] = 0x00;
         return buffer;
         }
    private static byte[] addBMP_RGB_888(int[] b, int w, int h) {
         int len = w*h;
         int bufflen = 0;
        byte[] tmp = new byte[3];
        int index = 0,bitindex = 1;
         if (w*h % 8 != 0)//将8字节变成1个字节,不足补0
         {
             bufflen = w*h/ 8 + 1;
             }
          else
         {
             bufflen = w*h/ 8;
             }
         if (bufflen % 4 != 0)//BMP图像数据大小，必须是4的倍数，图像数据大小不是4的倍数时用0填充补足
          {
            bufflen = bufflen + bufflen%4;
             }

         byte[] buffer = new byte[bufflen];

         for (int i = len - 1; i >= w; i -= w) {
            // DIB文件格式最后一行为第一行，每行按从左到右顺序
             int end = i, start = i - w + 1;
             for (int j = start; j <= end; j++) {

                 tmp[0] = (byte) (b[j] >> 0);
                 tmp[1] = (byte) (b[j] >> 8);
                 tmp[2] = (byte) (b[j] >> 16);

                 String hex = "";
                 for (int g = 0; g < tmp.length; g++) {
                     String temp = Integer.toHexString(tmp[g] & 0xFF);
                     if (temp.length() == 1) {
                         temp = "0" + temp;
                         }
                     hex = hex + temp;
                     }

                 if (bitindex > 8)
                 {
                     index += 1;
                     bitindex = 1;
                     }

                 if (!hex.equals("ffffff")) {
                     buffer[index] = (byte) (buffer[index] | (0x01 << 8-bitindex));
                     }
                 bitindex++;
                 }
             }
        return buffer;
        }



}
