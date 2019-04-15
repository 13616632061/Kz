package Utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by admin on 2017/7/13.
 */
public class USBPrinters {
    public static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";

    public static USBPrinters mInstance;

    private Context mContext;
    private UsbDevice mUsbDevice;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;
    private Boolean isprint =true;
    private int j=0;

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        mUsbDevice = usbDevice;
                    } else {
                        Toast.makeText(context, "Permission denied for device " + usbDevice, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (mUsbDevice != null) {
                    isprint=false;
                    Toast.makeText(context, "Device closed", Toast.LENGTH_SHORT).show();
                    if (mUsbDeviceConnection != null) {
                        mUsbDeviceConnection.close();
                    }
                }
            }
        }
    };

    private USBPrinters() {

    }

    public static USBPrinters getInstance() {
        if (mInstance == null) {
            mInstance = new USBPrinters();
        }
        return mInstance;
    }

    /**
     * 初始化打印机，需要与destroy对应
     *
     * @param context 上下文
     */
    public static void initPrinter(Context context) {
        getInstance().init(context);
    }

    /**
     * 销毁打印机持有的对象
     */
    public static void destroyPrinter() {
        getInstance().destroy();
    }

    private void init(Context context) {
        mContext = context;
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        mContext.registerReceiver(mUsbDeviceReceiver, filter);

        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
//            if(mUsbManager.hasPermission(device)){
                mUsbManager.requestPermission(device, mPermissionIntent);
//            }
        }
    }

    private void destroy() {
        if (mContext!=null){
            mContext.unregisterReceiver(mUsbDeviceReceiver);
        }
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
        mContext = null;
        mUsbManager = null;
    }

    /**
     * 打印方法
     * @param msg
     */
    public void print(String msg) {
        isprint=true;
        j=0;
        final String printData = msg;
            if (mUsbDevice != null) {
                while (isprint) {
                    UsbInterface usbInterface = mUsbDevice.getInterface(0);
                    for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                        final UsbEndpoint ep = usbInterface.getEndpoint(i);
//                        switch (ep.getType()){
//                            case UsbConstants.USB_ENDPOINT_XFER_BULK:
//                                break;
//                        }
                        if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                                try {
                                    if(mUsbManager.hasPermission(mUsbDevice)) {
                                        mUsbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
                                    }
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    return;
                                }
                                    if (mUsbDeviceConnection != null) {
                                    mUsbDeviceConnection.claimInterface(usbInterface, true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            byte[] bytes = new byte[0];
                                            try {
                                                bytes = printData.getBytes("GBK");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            if (mUsbDeviceConnection!=null){

                                                int b = mUsbDeviceConnection.bulkTransfer(ep, bytes, bytes.length, 100);
                                                if (b>=0) {
                                                    isprint=false;
                                                }else {
                                                    j++;
                                                }
                                            }
                                            if (j>400){
                                                isprint=false;
                                                Log.e("Return Status", "b-->");
                                            }
//                                            Log.i("Return Status", "b-->" + b);
                                            Log.i("Return Status", "b-->" + bytes.length);
                                        }
                                    }).start();
                                    mUsbDeviceConnection.releaseInterface(usbInterface);
                                    break;
                                }
                            }else {
                                j++;
                            }
                        }else {
                            j++;
                        }
                    }if (j>400){
                        isprint=false;
                    }
                }
            } else {
                Toast.makeText(mContext, "No available USB print device", Toast.LENGTH_SHORT).show();
            }
        }

    }
