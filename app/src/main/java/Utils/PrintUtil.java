package Utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gprinter.aidl.GpService;
import com.zj.btsdk.BluetoothService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Entty.Refund_entty;
import comon.error.Common;
import retail.yzx.com.debug;
import retail.yzx.com.kz.Printer_activity;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;

//import android.text.format.Time;
//import com.usbsdk.ASBStatus;
//import com.usbsdk.CallbackInfo;
//import com.usbsdk.CallbackInterface;
//import com.usbsdk.Device;
//import com.usbsdk.DeviceParameters;

/**
 * Created by admin on 2017/4/27.
 */
public class PrintUtil {


//    private static Device m_Device = new Device();

    public PrintUtil(Context context) {
        this.context = context;
//        m_Device.registerCallback(this);

    }

//    private static DeviceParameters m_DeviceParameters = new DeviceParameters();

    //GpCom objects
//    Device m_Device;
//    DeviceParameters m_DeviceParameters;
//    ASBStatus m_ASBData;
    //String FILE_NAME = "/mnt/internal_sd/quck.bin";
    String FILE_NAME = "quck.bin";
    //Status Thread
    Thread m_statusThread;
    boolean m_bStatusThreadStop;
    public OutputStream mOutputStream =new OutputStream();
    //progress dialog

    //static int test=0;
    //progress dialog
    ProgressDialog m_progressDialog;

    private final int ENABLE_BUTTON = 2;
    private final int SHOW_VERSION = 3;
    private final int UPDATE_FW = 4;
    private final int SHOW_PROGRESS = 5;
    private final int DISABLE_BUTTON = 6;
    private final int HIDE_PROGRESS=7;
    private final int REFRESH_PROGRESS=8;
    private final int SHOW_FONT_UPTAE_INFO=9;
    private final int SHOW_PRINTER_INFO_WHEN_INIT=10;
    private final byte  HDX_ST_NO_PAPER1 = (byte)(1<<0);     // 1 缺纸
    //private final byte  HDX_ST_BUF_FULL  = (byte)(1<<1);     // 1 缓冲满
    //private final byte  HDX_ST_CUT_ERR   = (byte)(1<<2);     // 1 打印机切刀错误
    private final byte  HDX_ST_HOT       = (byte)(1<<4);     // 1 打印机太热
    private final byte  HDX_ST_WORK      = (byte)(1<<5);     // 1 打印机在工作状态

    private boolean stop = false;
    public static int BinFileNum = 0;
    public static boolean ver_start_falg = false;
    boolean Status_Start_Falg = false;
    byte [] Status_Buffer=new byte[300];
    int Status_Buffer_Index = 0;
    public static int update_ver_event = 0;
    public static boolean update_ver_event_err = false;
    public static StringBuilder strVer=new StringBuilder("922");
    public static StringBuilder oldVer=new StringBuilder("922");
    public static File BinFile;
    // EditText mReception;
    public static final String TAG = "SampleCodeActivity";
    public static   String Error_State = "";
//    Time time = new Time();
    int TimeSecond;
    public CheckBox myCheckBox;
    public ProgressDialog myDialog = null;
    private int iProgress   = 0;
    String Printer_Info =new String();

    public static boolean flow_start_falg = false;
    byte [] flow_buffer=new byte[300];

    public TextView TextViewSerialRx;
    public static Context context;
    private  static int get_ver_count = 0;
    //    MyHandler handler;
    EditText Emission;
    Button ButtonCodeDemo;
    Button ButtonImageDemo;
    Button ButtonGetVersion;
    Button ButtonUpdateVersion;
    Button ButtonGetStatus;
    Button ButtonUpdateFontLib;
    static String debug_str="";
    String debug_strX="";
    ExecutorService pool = Executors.newSingleThreadExecutor();
    //WakeLock lock;
    int printer_status = 0;
    private ProgressDialog m_pDialog;

    private Bitmap mBitmap ;
    private Canvas mCanvas;
    private int lcd_width;
    private int lcd_height;
//    private class MyHandler extends Handler {
//        public void handleMessage(Message msg) {
//            if (stop == true)
//                return;
//            switch (msg.what) {
//                case DISABLE_BUTTON:
//                    //Close_Button();
//                    debug.d(TAG,"DISABLE_BUTTON");
//                    break;
//                case ENABLE_BUTTON:
//                    //ButtonCodeDemo.setEnabled(true);
//                    //ButtonImageDemo.setEnabled(true);
//                    ButtonGetVersion.setEnabled(true);
//                    //ButtonCharacterDemo.setEnabled(true);
//                    if(get_ver_count>1)
//                    {
//                        ButtonUpdateVersion.setEnabled(true);
//                        //ButtonUpdateFontLib.setEnabled(true);
//                    }
//
//                    debug.d(TAG,"ENABLE_BUTTON");
//                    break;
//                case SHOW_FONT_UPTAE_INFO:
//                    TextView tv3 = new TextView(MainActivity.this);
//                    tv3.setText((String)msg.obj);
//                    tv3.setGravity(Gravity.CENTER);
//                    tv3.setTextSize(25);
//                    tv3.findFocus();
//                    new AlertDialog.Builder(MainActivity.this)
//
//                            .setView(tv3)
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1,0, null));
//                                }
//                            }).show();
//                    break;
//                case SHOW_VERSION:
//                    TextView tv2 = new TextView(MainActivity.this);
//                    tv2.setText(getString(R.string.currentFWV)
//                            + MainActivity.strVer.toString());
//                    tv2.setGravity(Gravity.CENTER);
//                    tv2.setTextSize(25);
//                    tv2.findFocus();
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle(getString(R.string.getV))
//                            .setIcon(R.drawable.icon)
//                            .setView(tv2)
//                            .setCancelable(false)
//                            .setPositiveButton("OK", null).show();
//                    break;
//                case UPDATE_FW:
//                    m_pDialog.hide();
//                    TextView tv4 = new TextView(MainActivity.this);
//                    // if(!SampleCodeActivity.oldVer.toString().isEmpty())
//                {
//                    tv4.setText(getString(R.string.previousFWV)
//                            + MainActivity.oldVer.toString() + "\n"
//                            + getString(R.string.currentFWV)
//                            + MainActivity.strVer.toString());
//
//                }
//                // else
//                {
//                    // tv3.setText("update firmware version failed ");
//                }
//                tv4.setGravity(Gravity.CENTER);
//                tv4.setTextSize(22);
//                tv4.findFocus();
//
//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle(getString(R.string.updateFWFinish))
//                        .setIcon(R.drawable.icon).setView(tv4)
//                        .setCancelable(true)
//                        .setPositiveButton("OK", null).show();
//                break;
//                case SHOW_PROGRESS:
//                    m_pDialog = new ProgressDialog(MainActivity.this);
//                    m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    m_pDialog.setMessage((String)msg.obj);
//                    m_pDialog.setIndeterminate(false);
//                    m_pDialog.setCancelable(false);
//                    m_pDialog.show();
//                    break;
//                case  HIDE_PROGRESS:
//                    m_pDialog.hide();
//                    break;
//                case   REFRESH_PROGRESS :
//                    m_pDialog.setProgress(iProgress);
//                    break;
//                case     SHOW_PRINTER_INFO_WHEN_INIT:
//                    TextViewSerialRx.setText(Printer_Info+strVer.toString());
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    public Common.ERROR_CODE requestPermission(Context context) {
        UsbManager um = ((UsbManager) context.getSystemService(Context.USB_SERVICE));

        UsbDevice usbdev = getUsbDevice(um);

        if (usbdev != null) {

            // get requestPermission
            if (!um.hasPermission(usbdev)) {
                postRequestPermission(context, um, usbdev);

                return Common.ERROR_CODE.ERROR_OR_NO_ACCESS_PERMISSION;
            }

            return Common.ERROR_CODE.SUCCESS;
        }

        return Common.ERROR_CODE.NO_USB_DEVICE_FOUND;
    }
    static String ACTION_USB_PERMISSION = "com.usbsdk.USBPort.USB_PERMISSION";
    public static void postRequestPermission(Context context, UsbManager um,
                                              UsbDevice ud) {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                debug.d(TAG, intent.getAction());
                context.unregisterReceiver(this);
            }
        };

        IntentFilter ifilter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(receiver, ifilter);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        um.requestPermission(ud, pi);
    }

    static UsbDevice getUsbDevice(UsbManager um) {
        HashMap<String, UsbDevice> lst = um.getDeviceList();

        Iterator<UsbDevice> deviceIterator = lst.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice dev = (UsbDevice) deviceIterator.next();

            debug.d(TAG, "usb device : " + String.format("%1$04X:%2$04X", dev.getVendorId(), dev.getProductId()));


            if (dev.getVendorId() == 0x0485 ) {

                return dev;
            }
            if (dev.getVendorId() == 0xB000 ) {

                return dev;
            }

        }

        return null;
    }

    public void createAndRunStatusThread(final Activity act)
    {
        m_bStatusThreadStop=false;
        m_statusThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(m_bStatusThreadStop==false)
                {
                    try
                    {
                        //anything touching the GUI has to run on the Ui thread
                        act.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                //set indicator background color
//                                TextView tvOpen =(TextView)findViewById(R.id.textViewOpen);
//                                TextView tvClosed =(TextView)findViewById(R.id.textViewClosed);
//                                TextView tvdebug =(TextView)findViewById(R.id.textViewDebug);
//                                TextView tvdebugX =(TextView)findViewById(R.id.textViewDebugX);

//                                if(m_Device.isDeviceOpen())
//                                {
////                                    tvOpen.setBackgroundColor(0xFF00E000);		//green
////                                    tvClosed.setBackgroundColor(0xFF707070);	//gray
////                                    tvdebug.setBackgroundColor(0xF000E020);
//
//                                }
//                                else
//                                {
////                                    tvOpen.setBackgroundColor(0xFF707070);		//gray
////                                    tvClosed.setBackgroundColor(0xFFE00000);	//red
////                                    tvdebug.setBackgroundColor(0xFF707070);
//                                }
//                                tvdebugX.setBackgroundColor(0xFF707070);
//                                tvdebug.setText(debug_str);
//                                tvdebugX.setText(debug_strX);
                                //sleep(20000);

                            }
                        });

                        Thread.sleep(200);
                    }
                    catch(InterruptedException e)
                    {
                        m_bStatusThreadStop = true;
//                        messageBox(act, "Exception in status thread: " + e.toString() + " - " + e.getMessage(), "createAndRunStatusThread Error");
                    }
                }
            }
        });
        m_statusThread.start();
    }

    //打开打印机的监听
    public static void openButtonClicked()
    {
        Common.ERROR_CODE err = Common.ERROR_CODE.SUCCESS;
        debug_str="";
//        EditText editTextIPAddress = (EditText)findViewById(R.id.editTextIPAddress);
//        Spinner spinnerPortType = (Spinner)findViewById(R.id.spinnerPortType);
//        String selectedItem = (String)spinnerPortType.getSelectedItem();
        String selectedItem="USB";
        if(selectedItem.equals("Ethernet"))
        {
            //fill in some parameters
//            m_DeviceParameters.PortType = Common.PORT_TYPE.USB;
//            m_DeviceParameters.PortName = "";
//            m_DeviceParameters.ApplicationContext = context;
        }
        else if(selectedItem.equals("USB"))
        {
            Log.d("print","iiiiiiiii");
            //fill in some parameters
//            m_DeviceParameters.PortType = Common.PORT_TYPE.USB;
//            m_DeviceParameters.PortName = "";
//            m_DeviceParameters.ApplicationContext = context;
        }
        else
        {
            err = Common.ERROR_CODE.INVALID_PORT_TYPE;
        }
        byte [] dx={29 ,103 ,102 };
        print(dx);
        //	byte da[]={29 ,103, 102};
        //print(da);
        if(err== Common.ERROR_CODE.SUCCESS)
        {
            //set the parameters to the device
//            err = m_Device.setDeviceParameters(m_DeviceParameters);
            if(err!= Common.ERROR_CODE.SUCCESS)
            {
                String errorString = Common.getErrorText(err);
//                messageBox(context, errorString, "SampleCode: setDeviceParameters Error");
            }

            if(err== Common.ERROR_CODE.SUCCESS)
            {
                //open the device
                Log.d("print","iiiiiiiii");
//                err = m_Device.openDevice();
                if(err!= Common.ERROR_CODE.SUCCESS)
                {
                    String errorString = Common.getErrorText(err);
                    debug.d("SampleCode", "Error from openDevice: " + errorString);
//                    messageBox(context, errorString, "SampleCode 0: openDevice Error");
                }
            }

            if(err== Common.ERROR_CODE.SUCCESS)
            {
                //activate ASB sending
//                err = m_Device.activateASB(true, true, true, true, true, true);
                if(err!= Common.ERROR_CODE.SUCCESS)
                {
                    String errorString = Common.getErrorText(err);
                    debug.d("SampleCode", "Error from activateASB: " + errorString);
//                    messageBox(context, errorString, "SampleCode 1: openDevice Error");
                }
            }
        }
    }


    //
    //关闭打印机
    //
    public void closeButtonClicked(View view)
    {
//        Common.ERROR_CODE err = m_Device.closeDevice();
        Log.d("print","打印关闭");
//        if(err!= Common.ERROR_CODE.SUCCESS)
//        {
//            String errorString = Common.getErrorText(err);
////            messageBox(context, errorString, "closeDevice Error");
//        }

        //reset the ASB indicators
//        TextView textViewASBOnline =(TextView)findViewById(R.id.textViewASBOnline);
//        TextView textViewASBCover =(TextView)findViewById(R.id.textViewASBCover);
//        TextView textViewASBPaper =(TextView)findViewById(R.id.textViewASBPaper);
//        TextView textViewASBSlip =(TextView)findViewById(R.id.textViewASBSlip);
//        textViewASBOnline.setBackgroundColor(0xFF707070);	//gray
//        textViewASBCover.setBackgroundColor(0xFF707070);	//gray
//        textViewASBPaper.setBackgroundColor(0xFF707070);	//gray
//        textViewASBSlip.setBackgroundColor(0xFF707070);		//gray

    }


    //
    //打印文件的方法
    //
    public void printTextFileButtonClicked(View view)
    {

        debug.e(" quck", "  printTextFileButtonClicked" );
        get_status();

        new WriteThread().start();
    }


    //
    //读文件
    //
    String readTextFile(String filename) throws IOException
    {
        final StringBuilder sb = new StringBuilder();
        final FileInputStream fs = new FileInputStream(new File(filename));
        final BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String s;
        try {
            while((s = br.readLine()) != null)
            {
                sb.append(s);
                sb.append("\n");
                sb.append("");
            }
        } finally {
            if(br != null)
                br.close();
            if(fs != null)
                fs.close();
        }
        return sb.toString();
    }

    //
    //打印文件
    //
    void printTextFile(String filename)
    {
        Common.ERROR_CODE err = Common.ERROR_CODE.SUCCESS;

        try
        {
            String text = readTextFile(filename);
            byte[] bs = text.getBytes("GB2312");

//            if(m_Device.isDeviceOpen()==true)
//            {
//                Vector<Byte> data = new Vector<Byte>(bs.length);
//                for(int i=0; i<bs.length; i++) {
//                    data.add(bs[i]);
//                }
//
//                err = m_Device.sendData(data);
//                if(err!= Common.ERROR_CODE.SUCCESS)
//                {
//                    String errorString = Common.getErrorText(err);
////                    messageBox(context, errorString, "cutPaper Error");
//                }
//                else {
//                    err = m_Device.cutPaper();
//                    if(err!= Common.ERROR_CODE.SUCCESS)
//                    {
//                        String errorString = Common.getErrorText(err);
////                        messageBox(context, errorString, "cutPaper Error");
//                    }
//                }
//            }
//            else
//            {
////                messageBox(context, "Device is not open", "cutPaper Error");
//            }
        }
        catch(Exception e)
        {
//            messageBox(context, "Exception:" + e.getMessage(), "cutPaper Error");
        }
    }


    //
    //还不知道有什么用
    //
    //

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if(requestCode == FileBrowser.ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
//            Bundle bundle = null;
//            if (data != null && (bundle = data.getExtras()) != null) {
//                if (!bundle.containsKey("file")) {
//                    return;
//                }
//
//                String fpath = bundle.getString("file");
//
//                File f = new File(fpath);
//
//                if (!f.isDirectory()) {
//                    printTextFile(f.getAbsolutePath());
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    public Common.ERROR_CODE CallbackMethod(CallbackInfo cbInfo)
//    {
//        Common.ERROR_CODE retval = Common.ERROR_CODE.SUCCESS;
//
//        try
//        {
//            debug.e(" on CallbackMethod(GpComCallbackInfo cbInfo)"+cbInfo.receiveCount );
//
//            //do nothing, ignore any general incoming data
//
//            debug.d(TAG,"Receiving data: "+ Integer.toString(cbInfo.receiveCount)+ " bytes");
//            StringBuilder str = new StringBuilder();
//            StringBuilder strBuild = new StringBuilder();
//            for (int i = 0; i < cbInfo.receiveCount; i++)
//            {
//                //USBPort.this.m_receiveBuffer.add(Byte.valueOf(USBPort.this.m_receiveData[i]));
//                str.append(String.format(" %x", cbInfo.m_receiveData[i]));
//                strBuild.append(String.format("%c", (char) cbInfo.m_receiveData[i]));
//            }
//
//            debug.d(TAG, "onC= " + strBuild.toString());
//            debug.d(TAG, "onx= " + str.toString());
//
//            debug_str=debug_str+strBuild.toString();
//            debug_strX=debug_strX+str.toString();
//            if(debug_str.length()> (lcd_width*8/10))
//            {
//                debug_str=debug_str+"\n";
//
//            }
//            if(debug_strX.length()> (lcd_width*8/10))
//            {
//                debug_strX=debug_strX+"\n";
//
//            }
//    		/*
//	    	switch(cbInfo.ReceivedDataType)
//	    	{
//	    		case GENERAL:
//	    			debug.e(" on CallbackMethod(GpComCallbackInfo cbInfo)" );
//	    			//do nothing, ignore any general incoming data
//
//					debug.d(TAG,"Receiving data: "+ Integer.toString(cbInfo.receiveCount)+ " bytes");
//					StringBuilder str = new StringBuilder();
//					StringBuilder strBuild = new StringBuilder();
//					for (int i = 0; i < cbInfo.receiveCount; i++)
//					{
//						//USBPort.this.m_receiveBuffer.add(Byte.valueOf(USBPort.this.m_receiveData[i]));
//						str.append(String.format(" %x", cbInfo.m_receiveData[i]));
//						strBuild.append(String.format("%c", (char) cbInfo.m_receiveData[i]));
//					}
//					debug.d(TAG, "onReceivedC= " + strBuild.toString());
//					debug.d(TAG, "onReceivedx= " + str.toString());
//
//
//	    			break;
//	    		case ASB:	//new ASB data came in
//	    			debug.d("Sample", "new ASB data came in");
//	    			//receiveAndShowASBData();
//	    			break;
//	    	}	*/
//        }
//        catch(Exception e)
//        {
////            messageBox(context, "callback method threw exception: " + e.toString() + " - " + e.getMessage(), "Callback Error");
//        }
//
//        return retval;
//    }


    private void receiveAndShowASBData()
    {
        //retrieve current ASB data and show it
//        m_ASBData = m_Device.getASB();
//        showASBStatus();
    }

//    private void showASBStatus()
//    {
//        try
//        {
//            context.runOnUiThread(
//                    new Runnable()
//                    {
//                        public void run()
//                        {
//                            //get the status indicators from the GUI
////                            TextView textViewASBOnline =(TextView)findViewById(R.id.textViewASBOnline);
////                            TextView textViewASBCover =(TextView)findViewById(R.id.textViewASBCover);
////                            TextView textViewASBPaper =(TextView)findViewById(R.id.textViewASBPaper);
////                            TextView textViewASBSlip =(TextView)findViewById(R.id.textViewASBSlip);
//
//                            if(m_ASBData!=null)
//                            {
//                                //light up indicators
//                                if(m_ASBData.Online)
//                                {
////                                    textViewASBOnline.setBackgroundColor(0xFF00E000);	//green
//                                }
//                                else
//                                {
////                                    textViewASBOnline.setBackgroundColor(0xFFE00000);	//red
//                                }
//                                if(m_ASBData.CoverOpen)
//                                {
////                                    textViewASBCover.setBackgroundColor(0xFFE00000);	//red
//                                }
//                                else
//                                {
////                                    textViewASBCover.setBackgroundColor(0xFF00E000);	//green
//                                }
//                                if(m_ASBData.PaperOut==true)
//                                {
////                                    textViewASBPaper.setBackgroundColor(0xFFE00000);	//red
//                                }
//                                else
//                                {
//                                    if(m_ASBData.PaperNearEnd==true)
//                                    {
////                                        textViewASBPaper.setBackgroundColor(0xFFE0E000);	//yellow
//                                    }
//                                    else
//                                    {
////                                        textViewASBPaper.setBackgroundColor(0xFF00E000);	//green
//                                    }
//                                }
//                                if(m_ASBData.SlipSelectedAsActiveSheet)
//                                {
////                                    textViewASBSlip.setBackgroundColor(0xFF00E000);	//green
//                                }
//                                else
//                                {
////                                    textViewASBSlip.setBackgroundColor(0xFF707070);	//gray
//                                }
//                            }
//                        } //public void run()
//                    }); //this.runOnUiThread(
//        }
//        catch(Exception e)
//        {
//            messageBox(context, "receiveAndShowASBData threw exception: " + e.toString() + " - " + e.getMessage(), "ReceiveAndShowASBData Error");
//        }
//    }

//    public void messageBox(final Context context, final String message, final String title)
//    {
//        this.runOnUiThread(
//                new Runnable()
//                {
//                    public void run()
//                    {
//                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                        //alertDialog.setTitle(title);
//                        alertDialog.setMessage(message);
//                        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                alertDialog.cancel();
//                            }
//                        });
//                        alertDialog.show();
//                    }
//                }
//        );
//    }

    //
    //写入的线程1
    //
    private class WriteThread extends Thread {
        int  action_code;

        public WriteThread( )
        {

        }

        public void run()
        {
            byte[] bytes={};


            try {
                Log.d("print","打印字");

                xxdfsdf ();
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Log.d("print","打印字");
            String str="Shsjhdhh";

            bytes=str.getBytes();
            Byte[] byteList = toBytes(bytes);
            Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
//            m_Device.sendData(vector);


            new BmpThread().start();

        }

    }

    //写入的线程2
    //
    private class WriteThread2 extends Thread {
        int  action_code;

        public WriteThread2( )
        {

        }

        public void run()
        {


            new BmpThread().start();

        }

    }


    void get_status2()
    {


    	/*

    	 try {
			Resources r = getResources();

			 InputStream is = r.openRawResource(R.drawable.long_pic3);

				byte[] b = null;
			int count;

				count = is.available();
				b = new byte[count];

			is.read(b);
			SendLongDataToUart(b,count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    }

    	*/

    }

    //	执行写入线程2
    void get_status()
    {

        new WriteThread2().start();
    	/*
    	  byte[] bytes={};
   		String str ="              she had to carry this piece of notes to find the post college personnel Yangtze river non-success patched up fake snow everywhere. Shu-qin liu said, at that took the note to her, say we leave in Beijing, she don't accept. Then I said to her with tears in their eyes, director of jiang, then you can give me to buy my daughter's insurance, Jiang Xueyun said school has school rules and regulations, I said is not. I was crying, she said that you gave me, its no use crying, school has the school rules and regulations, there are many such person I have ever met.she had to carry this piece of notes to find the post college personnel Yangtze river non-success patched up fake snow everywhere. Shu-qin liu said, at that took the note to her, say we leave in Beijing, she don't accept. Then I said to her with tears in their eyes, director of jiang, then you can give me to buy my daughter's insurance, Jiang Xueyun said school has school rules and regulations, I said is not. I was crying, she said that you gave me, its no use crying, school has the school rules and regulations, there are many such person I have ever met.she had to carry this piece of notes to find the post college personnel Yangtze river non-success patched up fake snow everywhere. Shu-qin liu said, at that took the note to her, say we leave in Beijing, she don't accept. Then I said to her with tears in their eyes, director of jiang, then you can give me to buy my daughter's insurance, Jiang Xueyun said school has school rules and regulations, I said is not. I was crying, she said that you gave me, its no use crying, school has the school rules and regulations, there are many such person I have ever met.she had to carry this piece of notes to find the post college personnel Yangtze river non-success patched up fake snow everywhere. Shu-qin liu said, at that took the note to her, say we leave in Beijing, she don't accept. Then I said to her with tears in their eyes, director of jiang, then you can give me to buy my daughter's insurance, Jiang Xueyun said school has school rules and regulations, I said is not. I was crying, she said that you gave me, its no use crying, school has the school rules and regulations, there are many such person I have ever met.                 "  ;


		bytes=str.getBytes();
		Byte[] byteList = toBytes(bytes);
    Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
    m_Device.sendData(vector);

    	 try {
			Resources r = getResources();

			InputStream is = r.openRawResource(R.raw.huidu);
				byte[] b = null;
			int count;

				count = is.available();
				b = new byte[count];

			is.read(b);
			//print(b);
			SendLongDataToUart(b,count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    }
*/


    }

    void  xxdfsdf () throws UnsupportedEncodingException
    {


        Log.d("print","打印字头部");
        byte[] ddx={0x0a,0x0a,0x0a,0x0a,0x0a,0x1D, 0x21, 0x00};

        sendCommand("------打印头部----".getBytes("GBK"),ddx,0x0a,0x0a,0x0a,0x1b, 0x61, 1 ,0x1D, 0x21, 0x01);



//        sendCommand(0x0a,0x0a,0x0a,0x0a,0x0a,0x1D, 0x21, 0x00); // cancel double width
    }



    class OutputStream  {


        public OutputStream( ) {

        }

        void write(int[] bytes)
        {

            //	bytes = "倍高命令倍高命令倍高命令".getBytes("cp936");
            Byte[] byteList = toBytes(bytes);
            Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
            //   Vector<Byte> vector = new Vector<Byte>();

//            m_Device.sendData(vector);
        }
        void write(byte[] bytes)
        {

            //	bytes = "倍高命令倍高命令倍高命令".getBytes("cp936");
            Byte[] byteList = toBytes(bytes);
            Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
            //   Vector<Byte> vector = new Vector<Byte>();

//            m_Device.sendData(vector);
        }
        void write(int data)
        {
            int[] bytes =new int[1];
            bytes[0]=data;
            //	bytes = "倍高命令倍高命令倍高命令".getBytes("cp936");
            Byte[] byteList = toBytes(bytes);
            Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
            //   Vector<Byte> vector = new Vector<Byte>();

//            m_Device.sendData(vector);
        }
    }

    //
    //byte转换成字节数组
    //
    static Byte[]   toBytes(byte[] bytes)
    {
        Byte[] byteList =new Byte[bytes.length];
        int i;
        for(i=0;i<bytes.length;i++)
        {

            byteList[i]= bytes[i];
        }
        return    byteList;
    }


    //
    //int 数组转换成字节书
    //
    Byte[]   toBytes(int[] bytes)
    {
        Byte[] byteList =new Byte[bytes.length];
        int i;
        for(i=0;i<bytes.length;i++)
        {

            byteList[i]= (byte)bytes[i];
        }
        return    byteList;
    }

    //
    //线程停止几秒
    //
    private void sleep(int ms) {
        // debug.d(TAG,"start sleep "+ms);
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // debug.d(TAG,"end sleep "+ms);
    }




    //
    //读取的线程
    //
    private class SendThread extends Thread {
        InputStream is;
        public SendThread(InputStream is ) {
            this.is=is;
        }
        public void run() {
            try {
                int count = is.available();
                Log.d("print","SendFileToUart "+count );
                byte[] b = new byte[count];
                is.read(b);
                print(b);
                debug.e("quck2", "all data have send!!  "   );
                sleep(3000);

            } catch (Exception e) {
                e.printStackTrace();
            }finally{


                //HdxUtil.SetPrinterPower(0);
            }

        }
    }

    //
    //发送文件
    //
    public void SendFileToUart(InputStream is )
    {
        new SendThread(is).start();

        //handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1, 0,null));
    }


    //
    //发送命令
    //
    void sendCommand(int... bs) {

        Vector<Byte> data = new Vector<Byte>(bs.length);
        for(int i=0; i<bs.length; i++) {

            data.add((byte)bs[i]);
        }

//        m_Device.sendData(data);

    }


    //
    //发送byte数组的命令
    //
    static void sendCommand(byte[] dx, byte[] dy, int... bs) {


        Vector<Byte> data = new Vector<Byte>(bs.length);

        for(int i=0; i<bs.length; i++) {

            data.add((byte)bs[i]);
        }


        for(int i=0; i<dx.length; i++) {

            data.add((byte)dx[i]);
        }


        for(int i=0; i<dy.length; i++) {

            data.add((byte)dy[i]);
        }
//        m_Device.sendData(data);

    }




    //
    //打印的字节
    //

    static void  print(byte[] bs)
    {
        Vector<Byte> data = new Vector<Byte>(bs.length);
        for(int i=0; i<bs.length; i++) {

            data.add(bs[i]);
        }

//        m_Device.sendData(data);
    }


    //
    //发送长数据加载
    //
    public void SendLongDataToUart(byte[] b ,	int count ,int delay_time,int delay_time2 )
    {

        int block_size=10*1024;
        int i;
        int temp;
        try {
            byte SendBuf[] = new byte[count  +block_size-1];
            Arrays.fill(SendBuf,(byte)0);
            //send bin file
            System.arraycopy(b,0,SendBuf,0, count);
            //temp= (count +63)/64;
            temp= (count )/block_size;
            byte[] databuf= new byte[block_size];

            //  obtainMessage(int what, int arg1, int arg2, Object obj);
            //  handler传值
//            handler.sendMessage(handler.obtainMessage(SHOW_PROGRESS, 1, 0,null));
            for(i=0;i<temp-1;i++)
            {
                System.arraycopy(SendBuf,i*block_size,databuf,0,block_size);

                debug.i("quck2", " updating ffont finish:"  +((i+1)*100)/temp +"%");
                iProgress=((i+1)*100)/temp;
                //  handler传值
//                handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
                print(databuf);

                sleep(delay_time2);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally{


            //HdxUtil.SetPrinterPower(0);
        }
        sleep(delay_time);
        //  handler传值
//        handler.sendMessage(handler.obtainMessage(HIDE_PROGRESS, 1, 0,null));

    }
    public void SendLongDataToUart(byte[] b ,	int count  )
    {


        //int block_size=0x2000;
        int block_size=0x4000;
        int i;
        int temp;
        int delay_time =50;
        int delay_time2=0;
        count=b.length;
        boolean  flag = (b.length &(block_size -1) ) != 0 ;
        try {

            if(b.length<=block_size)
            {

                print(b);
                return;
            }

            byte[] databuf= new byte[block_size];
            temp= (count )/block_size;
//            handler.sendMessage(handler.obtainMessage(SHOW_PROGRESS, 1, 0,null));
            for(i=0;i<temp;i++)
            {
                System.arraycopy(b,i*block_size,databuf,0,block_size);

                debug.i("quck2", " updating ffont finish:"  +((i+1)*100)/temp +"%");
                iProgress=((i+1)*100)/temp;
//                handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
                print(databuf);

                sleep(delay_time2);

            }

            if( (b.length &(block_size -1) )!= 0)
            {

                databuf= new byte[(b.length &(block_size -1) )] ;
                int dd = b.length &(block_size -1)  ;
                System.arraycopy(b,i*block_size,databuf,0,dd);
                print(databuf);
            }
            else
            {
//                handler.sendMessage(handler.obtainMessage(HIDE_PROGRESS, 1, 0,null));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{


            //HdxUtil.SetPrinterPower(0);
        }
        sleep(delay_time);
//        handler.sendMessage(handler.obtainMessage(HIDE_PROGRESS, 1, 0,null));


    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }




    final static int Max_Dot=576;
    public void PrintBmp(int startx, Bitmap bitmap) throws IOException {
        // byte[] start1 = { 0x0d,0x0a};
        byte[] start2 = { 0x1D, 0x76, 0x30, 0x30, 0x00, 0x00, 0x01, 0x00 };

        int width = bitmap.getWidth() + startx;
        int height = bitmap.getHeight();
        Bitmap.Config m =bitmap.getConfig();
        // 332  272  ARGB_8888
        debug.e(TAG,"width:  "+width+" height :"+height+"   m:"+ m);
        if (width > Max_Dot)
            width = Max_Dot;
        int tmp = (width + 7) / 8;
        byte[] data = new byte[tmp];
        byte xL = (byte) (tmp % 256);
        byte xH = (byte) (tmp / 256);
        start2[4] = xL;
        start2[5] = xH;
        start2[6] = (byte) (height % 256);
        ;
        start2[7] = (byte) (height / 256);
        ;
        byte SendBuf[] = new byte[start2.length+data.length*height];
        Arrays.fill(SendBuf,(byte)0);
        System.arraycopy(start2,0,SendBuf,0, start2.length);
        //mOutputStream.write(start2);
        try { Thread.sleep(1); } catch (InterruptedException e) { }
        //System.arraycopy(src,0,byteNumCrc,0,4);
        //System.arraycopy(b,0,SendBuf,0, count);


        for (int i = 0; i < height; i++) {

            for (int x = 0; x < tmp; x++)
                data[x] = 0;

            for (int x = startx; x < width; x++) {
                int pixel = bitmap.getPixel(x - startx, i);
                if (Color.red(pixel) == 0 || Color.green(pixel) == 0
                        || Color.blue(pixel) == 0) {
                    // 楂樹綅鍦ㄥ乏锛屾墍浠ヤ娇鐢?28 鍙崇Щ
                    data[x / 8] += 128 >> (x % 8);// (byte) (128 >> (y % 8));
                }
            }


            //mOutputStream.write(data);
            System.arraycopy(data,0,SendBuf,(start2.length+data.length*i), data.length);
            //  try { Thread.sleep(50); } catch (InterruptedException e) { }

        }
        //mOutputStream.write(SendBuf);
        SendLongDataToUart(SendBuf,SendBuf.length);
    }



    private class BmpThread extends Thread {
        public BmpThread() {
        }
        public void run() {
            super.run();
            try {
                Resources r = context.getResources();
                InputStream is = r.openRawResource(R.raw.liau);
                BitmapDrawable bmpDraw = new BitmapDrawable(is);
                Bitmap bmp = bmpDraw.getBitmap();
                PrintBmp(0, bmp);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    //打印小票方法
    public static void PrintString(String heji, String shisou, String zhaoling, String danhao, String time, int j, List<Refund_entty> refund_entty){
        String str = " 打印测试 ";
        byte[] by = str.getBytes();
        Byte[] bytes = toBytes(by);
        Vector<Byte> vector = new Vector<>(Arrays.asList(bytes));
        byte[] ddx = {0x0a, 0x1B,0x0E, 0x1B, 0x74, 15, 0x1D, 0x21, 0x00};
        byte[] ddx1 = {0x0a, 0x0a, 0x1D, 0x21, 0x00};
        byte[] ddx2 = {0x1D, 0x21, 0x00, 0x1B, 0x61, 0};
        byte[] ddx3 = {0x1D, 0x21, 0x00};
        byte[] ddx4 = {0x0a, 0x0a, 0x1D, 0x21, 0x00};
        byte[] ddx5 = {0x0a, 0x1D, 0x21, 0x00, 0x1B, 0x61, 0};
        byte[] ddx6 = {0x1D, 0x21, 0x00, 0x1B, 0x61, 0};
        byte[] ddx7 = {0x0a, 0x0a, 0x1D, 0x21, 0x00, 0x1B, 0x20, 0};
        byte[] ddx8 = {0x1D, 0x21, 0x00, 0x1B, 0x61, 2};
        byte[] ddx9 = {0x0a, 0x1D, 0x21, 0x00, 0x1B, 0x61, 2};
        byte[] ddx10 = {0x0a, 0x0a, 0x0a, 0x1D, 0x21, 0x00, 0x1B, 0x61, 1};
        try {
//            sendCommand("易星生活".getBytes("cp936"), ddx, 0x1B, 0x61, 1, 0x1D, 0x21, 0x01, 0x1D, 0x21, 0x01, 0x1B, 32, 0x0a);
            sendCommand(SharedUtil.getString("name").getBytes("cp936"),  ddx, 0x1B, 0x33, 10, 0x1B, 0x61, 1);
            for (int i=0;i<refund_entty.size();i++){
                sendCommand(((i + 1) + "").getBytes("cp936"), ddx2,0x1B,0x20,4);
                sendCommand(refund_entty.get(i).getName().getBytes("cp936"), ddx3, 0x1B, 0x33, 1);
                if (refund_entty.get(i).getName().toString().length()==1){
                    sendCommand(("        " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==2){
                    sendCommand(("      " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==3){
                    sendCommand(("     " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==4){
                    sendCommand(("    " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==5){
                    sendCommand(("   " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==6){
                    sendCommand(("  " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==7){
                    sendCommand((" " + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if (refund_entty.get(i).getName().toString().length()==8){
                    sendCommand(("" + refund_entty.get(i).getNums()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
                }
                if ((refund_entty.get(i).getNums()+"").length()==1){
                    sendCommand(("       " + StringUtils.stringpointtwo(refund_entty.get(i).getPrice())).getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3);
                }
                if ((refund_entty.get(i).getNums()+"").length()==2){
                    sendCommand(("      " + StringUtils.stringpointtwo(refund_entty.get(i).getPrice())).getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3);
                }
                if ((refund_entty.get(i).getNums()+"").length()==3){
                    sendCommand(("     " + StringUtils.stringpointtwo(refund_entty.get(i).getPrice())).getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3);
                }
                if ((refund_entty.get(i).getNums()+"").length()==4){
                    sendCommand(("    " + StringUtils.stringpointtwo(refund_entty.get(i).getPrice())).getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3);
                }
//                sendCommand(("" + entty.get(i).getNumber()).getBytes("cp936"), ddx3, 0x1D, 0x4C, 0, 0);
//                sendCommand(("" + StringUtils.stringpointtwo(commodities.get(i).getPrice())).getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3);
            }
            sendCommand("------------------------------".getBytes("cp936"), ddx5);
            sendCommand("消费合计:".getBytes("cp936"), ddx6, 0x1B, 0x33, 1);
            sendCommand(heji.getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3, 0x1B, 0x33, 1);
            if (j == 1) {
                sendCommand("移动支付:".getBytes("cp936"), ddx6, 0x1B, 0x33, 1);
            }
            if (j == 0) {
                sendCommand("现金支付:".getBytes("cp936"), ddx6, 0x1B, 0x33, 1);
            }
            sendCommand(shisou.getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3, 0x1B, 0x33, 1);
            sendCommand("找零:".getBytes("cp936"), ddx6);
            sendCommand(zhaoling.getBytes("cp936"), ddx7, 0x1D, 0x4C, 3, 3, 0x1B, 0x33, 1);
            sendCommand(("交易单号:" + danhao).getBytes("cp936"), ddx9, 0x1B, 0x61, 1, 0x0a, 0x1B, 0x33, 1);
            sendCommand(time.getBytes("cp936"), ddx9, 0x1B, 0x61, 1, 0x0a, 0x1B, 0x33, 1);
            sendCommand("服务热线：400-xxx-xxx-xxx".getBytes("cp936"), ddx10, 0x1B, 0x61, 1, 0x0a, 0x1B, 0x33, 1);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

    }
    public static void printstring(String str){
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Byte[] byteList = toBytes(bytes);
        Vector<Byte> vector = new Vector<Byte>(Arrays.asList(byteList));
//        m_Device.sendData(vector);
    }
    //是否自动打印小票和订单标签
    public static void doPrint(Context context, GpService mGpService, BluetoothService mService, String seller_tel, String order_id, long time_long, String time, int n, String pay_money, String really_money, String change_money,
                               int payType, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo ,ArrayList<Self_Service_GoodsInfo> newmSelf_Service_GoodsInfo, String doPackage, String peoplenums, String tablenums,String num) {

        if (SharedUtil.getBoolean("self_print")) {//默认自动打印小票
           if (mSelf_Service_GoodsInfo.size()>0) {
               receiptPrint(context, mService, seller_tel, order_id, time_long, time, n, pay_money, really_money, change_money, payType, mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo, doPackage, peoplenums, tablenums,num,"");
           }
        }

        if(SharedUtil.getBoolean("self_print_order_label")){//是否自动打印订单标签，默认自动打印
            order_RabelPrint(context,mGpService,order_id,time_long, time,n,pay_money,really_money,change_money,payType, mSelf_Service_GoodsInfo);
        }
    }

    //报货打印
    public static void doPrint(Context context, BluetoothService mService, String syy) {

        if (SharedUtil.getBoolean("self_print")) {//默认自动打印小票
            receiptPrint(context,mService,syy);
        }

    }

    //小票打印
    public static void receiptPrint(Context context, String seller_tel, String order_id, long time_long, String time, int n, String pay_money, String really_money, String change_money,
                                    int payType, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo, String doPackage, String peoplenums, String tablenums,String num){
        //获取USb小票打印机状态
        PrintUtil printUtil1 = new PrintUtil(context);
        printUtil1.openButtonClicked();
        //小票打印数据
        String syy = "";
        if (n == 1) {//n=1时间格式为时间戳  n=0 时间格式为 YYYY-MM-DD hh-mm-ss
            syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), seller_tel, order_id, DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), mSelf_Service_GoodsInfo, mSelf_Service_GoodsInfo,
                    payType, Double.parseDouble(pay_money.replace("￥", "").trim()), really_money, pay_money.replace("￥", "").trim(), change_money,doPackage,peoplenums,tablenums,num,"");
        } else {
            syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), seller_tel, order_id, time, mSelf_Service_GoodsInfo, mSelf_Service_GoodsInfo,
                    payType, Double.parseDouble(pay_money.replace("￥", "").trim()), really_money, pay_money.replace("￥", "").trim(), change_money,doPackage,peoplenums,tablenums,num,"");

        }

        if (PrintWired.usbPrint(context,syy)){
        }else {
        }

        //蓝牙小票打印
//        mService.sendMessage(syy, "GBK");
        //USB打印小票
//        printUtil1.printstring(syy);
    }



    //小票打印
    public static void receiptPrint(Context context, BluetoothService mService, String seller_tel, String order_id, long time_long, String time, int n, String pay_money, String really_money, String change_money,
                                    int payType, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo,ArrayList<Self_Service_GoodsInfo> newmSelf_Service_GoodsInfo, String doPackage, String peoplenums, String tablenums,String num,String notes){
        //获取USb小票打印机状态
        PrintUtil printUtil1 = new PrintUtil(context);
        printUtil1.openButtonClicked();
        //小票打印数据
        String syy = "";
        byte[] bytes=null;
        if (n == 1) {//n=1时间格式为时间戳  n=0 时间格式为 YYYY-MM-DD hh-mm-ss
            syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), seller_tel, order_id, DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), mSelf_Service_GoodsInfo, mSelf_Service_GoodsInfo,
                    payType, Double.parseDouble(pay_money.replace("￥", "").trim()), really_money, pay_money.replace("￥", "").trim(), change_money,doPackage,peoplenums,tablenums,num,notes);
//            bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
//                     change_money,doPackage,tablenums,num,notes);
        } else {
            syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), seller_tel, order_id, time, mSelf_Service_GoodsInfo, mSelf_Service_GoodsInfo,
                    payType, Double.parseDouble(pay_money.replace("￥", "").trim()), really_money, pay_money.replace("￥", "").trim(), change_money,doPackage,peoplenums,tablenums,num,notes);
//            bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
//                    change_money,doPackage,tablenums,num,notes);
        }
        if (SharedUtil.getString("print_unms")!=null) {
            if (!SharedUtil.getString("print_unms").equals("")) {
            int j = Integer.parseInt(SharedUtil.getString("print_unms"));
            for (int i = 0; i < j; i++) {
                /**
                 * 厨房打印机的调试
                 */
                if (newmSelf_Service_GoodsInfo.size()>0){
                    if (payType==3){//paytype==3是挂单还是支付的判断
                        if (SharedUtil.getBoolean("kitchen_print")){
                            if (n==1){
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                        if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                            data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }else {
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }

                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }
                            if (bytes!=null){
                                KitchenPrint.PrintfData(bytes,context);
                            }
                        }
                    }else {
                        if (SharedUtil.getfalseBoolean("pay_print")){
                        }else {
                            if (n==1){
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }else {
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }
                            if (bytes!=null){
                                KitchenPrint.PrintfData(bytes,context);
                            }
                        }
                    }
                }
                if (payType==3){
                    if (SharedUtil.getfalseBoolean("list_print")) {
                        if (PrintWired.usbPrint(context, syy)) {
                        } else {
                            printUtil1.printstring(syy);
                            mService.sendMessage(syy, "GBK");
                        }
                    }
                }else{
                    if (PrintWired.usbPrint(context, syy)) {
                    } else {
                        printUtil1.printstring(syy);
                        mService.sendMessage(syy, "GBK");
                    }
                }
            }
        }else {
                /**
                 * 厨房打印机的调试
                 */
                if (newmSelf_Service_GoodsInfo.size()>0){
                    if (payType==3){//paytype==3是挂单还是支付的判断
                        if (SharedUtil.getBoolean("kitchen_print")){
                            if (n==1){
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }else {
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }
                            if (bytes!=null){
                                KitchenPrint.PrintfData(bytes,context);
                            }
                        }
                    }else {
                        if (SharedUtil.getfalseBoolean("pay_print")){
                        }else {
                            if (n==1){
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }else {
                                if (SharedUtil.getMole()!=null){
                                    ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                    for (int k=0;k<SharedUtil.getMole().size();k++){
                                        for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                            if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                                data.add(newmSelf_Service_GoodsInfo.get(g));
                                            }else {
                                            }
                                        }
                                    }
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                            change_money,doPackage,tablenums,num,notes);
                                }else {
                                    bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                            change_money,doPackage,tablenums,num,notes);
                                }
                            }
                            if (bytes!=null){
                                KitchenPrint.PrintfData(bytes,context);
                            }
                        }
                    }
                }
                if (payType==3){
                    if (SharedUtil.getfalseBoolean("list_print")) {
                        if (PrintWired.usbPrint(context, syy)) {
                        } else {
                            printUtil1.printstring(syy);
                            mService.sendMessage(syy, "GBK");
                        }
                    }
                }else{
                    if (PrintWired.usbPrint(context, syy)) {
                    } else {
                        printUtil1.printstring(syy);
                        mService.sendMessage(syy, "GBK");
                    }
                }
            }
        }else {
            /**
             * 厨房打印机的调试
             */
            if (newmSelf_Service_GoodsInfo.size()>0){
                if (payType==3){//paytype==3是挂单还是支付的判断
                    if (SharedUtil.getBoolean("kitchen_print")){
                        if (n==1){
                            if (SharedUtil.getMole()!=null){
                                ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                for (int k=0;k<SharedUtil.getMole().size();k++){
                                    for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                        if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                            data.add(newmSelf_Service_GoodsInfo.get(g));
                                        }else {
                                        }
                                    }
                                }
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                        change_money,doPackage,tablenums,num,notes);
                            }else {
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                        change_money,doPackage,tablenums,num,notes);
                            }
                        }else {
                            if (SharedUtil.getMole()!=null){
                                ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                for (int k=0;k<SharedUtil.getMole().size();k++){
                                    for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                        if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                            data.add(newmSelf_Service_GoodsInfo.get(g));
                                        }else {
                                        }
                                    }
                                }
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                        change_money,doPackage,tablenums,num,notes);
                            }else {
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                        change_money,doPackage,tablenums,num,notes);
                            }
                        }
                        if (bytes!=null){
                            KitchenPrint.PrintfData(bytes,context);
                        }
                    }
                }else {
                    if (SharedUtil.getfalseBoolean("pay_print")){
                    }else {
                        if (n==1){
                            if (SharedUtil.getMole()!=null){
                                ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                for (int k=0;k<SharedUtil.getMole().size();k++){
                                    for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                        if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                            data.add(newmSelf_Service_GoodsInfo.get(g));
                                        }else {
                                        }
                                    }
                                }
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), data, data,
                                        change_money,doPackage,tablenums,num,notes);
                            }else {
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                        change_money,doPackage,tablenums,num,notes);
                            }
                        }else {
                            if (SharedUtil.getMole()!=null){
                                ArrayList<Self_Service_GoodsInfo> data=new ArrayList<>();
                                for (int k=0;k<SharedUtil.getMole().size();k++){
                                    for (int g=0;g<newmSelf_Service_GoodsInfo.size();g++){
                                        if (newmSelf_Service_GoodsInfo.get(g).getTag_id().equals(SharedUtil.getMole().get(k).getTag_id())){
                                            data.add(newmSelf_Service_GoodsInfo.get(g));
                                        }else {
                                        }
                                    }
                                }
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, data, data,
                                        change_money,doPackage,tablenums,num,notes);
                            }else {
                                bytes=BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), time, newmSelf_Service_GoodsInfo, newmSelf_Service_GoodsInfo,
                                        change_money,doPackage,tablenums,num,notes);
                            }
                        }
                        if (bytes!=null){
                            KitchenPrint.PrintfData(bytes,context);
                        }
                    }
                }
            }
            if (payType==3){
                if (SharedUtil.getfalseBoolean("list_print")) {
                    if (PrintWired.usbPrint(context, syy)) {
                    } else {
                        printUtil1.printstring(syy);
                        mService.sendMessage(syy, "GBK");
                    }
                }
            }else{
                if (PrintWired.usbPrint(context, syy)) {
                } else {
                    printUtil1.printstring(syy);
                    mService.sendMessage(syy, "GBK");
                }
            }
        }
        //蓝牙小票打印
//        mService.sendMessage(syy, "GBK");
        //USB打印小票
//        printUtil1.printstring(syy);
    }

    //报货
    public static void receiptPrint(Context context, BluetoothService mService,String syy){
        //获取USb小票打印机状态
        //小票打印数据
        //蓝牙小票打印
        mService.sendMessage(syy, "GBK");
        //USB打印小票
    }

    //订单标签打印
    public static void order_RabelPrint(Context context, GpService mGpService, String order_id, long time_long, String time, int n, String pay_money, String really_money, String change_money, int payType, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo){
        //蓝牙标签打印
        if (n == 1) {
            PrintUtils.Setprint(context, mGpService, order_id.substring(order_id.length() - 4, order_id.length()),
                    DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), mSelf_Service_GoodsInfo);
        } else {
            PrintUtils.Setprint(context, mGpService, order_id.substring(order_id.length() - 4, order_id.length()),
                    time, mSelf_Service_GoodsInfo);
        }
        //Usb标签打印
        try {
            if (n == 1) {
                Printer_activity.setPaintSelf_srevice(context, order_id.substring(order_id.length() - 4, order_id.length()),
                        DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000), mSelf_Service_GoodsInfo);
            } else {
                Printer_activity.setPaintSelf_srevice(context, order_id.substring(order_id.length() - 4, order_id.length()),
                        time, mSelf_Service_GoodsInfo);
            }
        } catch (Exception e) {
            Log.e("barcode", "e000+" + e.toString());
        }
    }
}
