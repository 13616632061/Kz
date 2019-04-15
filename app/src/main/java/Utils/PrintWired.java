package Utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import java.util.Iterator;

/**
 * Created by admin on 2018/4/12.
 */

public class PrintWired {



    public static boolean operCash(Context context){
        PendingIntent mPermissionIntent;
        // 取连接到设备上的USB设备集合
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> map = usbManager.getDeviceList();

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        // 没有连接设备
        if (map.isEmpty()){
            if (context!=null){
                Toast.makeText(context, "请连接USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        UsbDevice usbDevice = null;
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            int VendorID = device.getVendorId();
            int ProductID = device.getProductId();
            Log.e("printUsb","VendorID"+VendorID);
            Log.e("printUsb","ProductID"+ProductID);

            if(VendorID == 1155 || VendorID == 1208 || VendorID == 1046||VendorID == 10473||VendorID == 45056||VendorID == 1305||VendorID == 17224){
                if (ProductID == 33054||ProductID == 22592||ProductID==62 || ProductID==22861 || ProductID==649 || ProductID==1042|| ProductID==8213|| ProductID==1803|| ProductID==21892){
                    usbDevice = device;
                }
            }
        }

//        // 遍历集合取指定的USB设备
//        UsbDevice usbDevice = null;
//        for(UsbDevice device : map.values()){
//            // 芯烨  XP-58IIH 的 VendorID = 1155 , ProductID = 1803
//            int VendorID = device.getVendorId();
//            int ProductID = device.getProductId();
//
//
////			if(VendorID == 1155 && ProductID == 1803){
//            if(VendorID == 1155 || VendorID == 1208 || VendorID == 1046||VendorID == 10473||VendorID == 45056||VendorID == 1305){
//                if (ProductID == 33054||ProductID == 22592||ProductID==62 || ProductID==22861 || ProductID==649 || ProductID==1042|| ProductID==8213|| ProductID==1803){
//                    usbDevice = device;
//            }
//                 break;
//            }
//        }
        mPermissionIntent= PendingIntent.getBroadcast(context,0,new Intent("ACTION_USB_PERMISSION"),PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent localPendingIntent = PendingIntent.getBroadcast(context,
//                0, new Intent("ACTION_USB_PERMISSION"),);
        if (usbDevice!=null){
            usbManager.requestPermission(usbDevice,mPermissionIntent);
        }
        // 没有找到设备
        if(usbDevice == null){
            if (context!=null){
                Toast.makeText(context, "没有找到USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        // 程序是否有操作设备的权限
        if(!usbManager.hasPermission(usbDevice)){
            if (context!=null) {
                Toast.makeText(context, "没有权限操作USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        // 设备接口, 注意设备不同接口不同, 这里取第一个
//        Toast.makeText(context, "设备接口个数:" + usbDevice.getInterfaceCount(), Toast.LENGTH_SHORT).show();
//        UsbInterface usbInterface = usbDevice.getInterface(0);

        // 分配端点, 注意设备不同端点不同, 这里取第二个
//        Toast.makeText(context, "分配端点个数:"+usbInterface.getEndpointCount(), Toast.LENGTH_SHORT).show();
//        UsbEndpoint outEndpoint = usbInterface.getEndpoint(0);



//        for(int k = 0; k < usbDevice.getInterfaceCount(); ++k) {
//            usbInterface = usbDevice.getInterface(k);
//            usbEndpointOut = null;
//            usbEndpointOut = null;
//
//            for(int j = 0; j < usbInterface.getEndpointCount(); ++j) {
//                UsbEndpoint endpoint = usbInterface.getEndpoint(j);
//                if(endpoint.getDirection() == 0 && endpoint.getType() == 2) {
//                    usbEndpointOut = endpoint;
//                } else if(endpoint.getDirection() == 128 && endpoint.getType() == 2) {
//                    usbEndpointIn = endpoint;
//                }
//
//                if(usbEndpointOut != null && usbEndpointIn != null) {
//                    break;
//                }
//            }
//
//            if(usbEndpointOut != null && usbEndpointIn != null) {
//                break;
//            }
//        }



//        UsbEndpoint outEndpoint = null;
//
//        for(int j = 0; j < usbInterface.getEndpointCount(); ++j) {
//            UsbEndpoint endpoint = usbInterface.getEndpoint(j);
//            if(endpoint.getDirection() == 0 && endpoint.getType() == 2) {
////                outEndpoint = endpoint;
//            } else if(endpoint.getDirection() == 128 && endpoint.getType() == 2) {
//                outEndpoint = endpoint;
//            }
//            if( outEndpoint != null) {
//                break;
//            }
//        }
        UsbDeviceConnection connection = null;
        UsbInterface usbInterface = null;
        UsbEndpoint usbEndpointOut = null;
        UsbEndpoint usbEndpointIn = null;
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int interfaceIndex = 0; interfaceIndex < interfaceCount; interfaceIndex++) {
            usbInterface = usbDevice.getInterface(interfaceIndex);
            for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                UsbEndpoint ep = usbInterface.getEndpoint(i);
                if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        usbEndpointIn = ep;
                    } else {
                        usbEndpointOut = ep;
                    }
                }
            }
            if ((null == usbEndpointIn) || (null == usbEndpointOut)) {
                usbEndpointOut = null;
                usbEndpointIn = null;
            } else {
//                        mUsbInterface = usbInterface;
//                        mUsbDeviceConnection = mUsbManager.openDevice(device);
                // 打开设备建立连接
                connection = usbManager.openDevice(usbDevice);
                connection.claimInterface(usbInterface, true);
                break;
            }
        }
        if (connection!=null) {
            pushReceiptCash(context, connection, usbEndpointIn);
        }
        connection.close();
        return true;
    }

    public static boolean usbPrint(Context context, String data){
        PendingIntent mPermissionIntent;
        // 取连接到设备上的USB设备集合
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> map = usbManager.getDeviceList();

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        // 没有连接设备
        if (map.isEmpty()){
            if (context!=null){
                Toast.makeText(context, "请连接USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        UsbDevice usbDevice = null;
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            int VendorID = device.getVendorId();
            int ProductID = device.getProductId();
            Log.e("printUsb","VendorID"+VendorID);
            Log.e("printUsb","ProductID"+ProductID);

            if(VendorID == 1155 || VendorID == 1208 || VendorID == 1046||VendorID == 10473||VendorID == 45056||VendorID == 1305){
                if (ProductID == 33054||ProductID == 22592||ProductID==62 || ProductID==22861 || ProductID==649 || ProductID==1042|| ProductID==8213|| ProductID==1803){
                    usbDevice = device;
                }
            }
        }

//        // 遍历集合取指定的USB设备
//        UsbDevice usbDevice = null;
//        for(UsbDevice device : map.values()){
//            // 芯烨  XP-58IIH 的 VendorID = 1155 , ProductID = 1803
//            int VendorID = device.getVendorId();
//            int ProductID = device.getProductId();
//
//
////			if(VendorID == 1155 && ProductID == 1803){
//            if(VendorID == 1155 || VendorID == 1208 || VendorID == 1046||VendorID == 10473||VendorID == 45056||VendorID == 1305){
//                if (ProductID == 33054||ProductID == 22592||ProductID==62 || ProductID==22861 || ProductID==649 || ProductID==1042|| ProductID==8213|| ProductID==1803){
//                    usbDevice = device;
//            }
//                 break;
//            }
//        }
        mPermissionIntent= PendingIntent.getBroadcast(context,0,new Intent("ACTION_USB_PERMISSION"),PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent localPendingIntent = PendingIntent.getBroadcast(context,
//                0, new Intent("ACTION_USB_PERMISSION"),);
        if (usbDevice!=null){
            usbManager.requestPermission(usbDevice,mPermissionIntent);
        }
        // 没有找到设备
        if(usbDevice == null){
            if (context!=null){
                Toast.makeText(context, "没有找到USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        // 程序是否有操作设备的权限
        if(!usbManager.hasPermission(usbDevice)){
            if (context!=null) {
                Toast.makeText(context, "没有权限操作USB设备！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        // 设备接口, 注意设备不同接口不同, 这里取第一个
//        Toast.makeText(context, "设备接口个数:" + usbDevice.getInterfaceCount(), Toast.LENGTH_SHORT).show();
//        UsbInterface usbInterface = usbDevice.getInterface(0);

        // 分配端点, 注意设备不同端点不同, 这里取第二个
//        Toast.makeText(context, "分配端点个数:"+usbInterface.getEndpointCount(), Toast.LENGTH_SHORT).show();
//        UsbEndpoint outEndpoint = usbInterface.getEndpoint(0);



//        for(int k = 0; k < usbDevice.getInterfaceCount(); ++k) {
//            usbInterface = usbDevice.getInterface(k);
//            usbEndpointOut = null;
//            usbEndpointOut = null;
//
//            for(int j = 0; j < usbInterface.getEndpointCount(); ++j) {
//                UsbEndpoint endpoint = usbInterface.getEndpoint(j);
//                if(endpoint.getDirection() == 0 && endpoint.getType() == 2) {
//                    usbEndpointOut = endpoint;
//                } else if(endpoint.getDirection() == 128 && endpoint.getType() == 2) {
//                    usbEndpointIn = endpoint;
//                }
//
//                if(usbEndpointOut != null && usbEndpointIn != null) {
//                    break;
//                }
//            }
//
//            if(usbEndpointOut != null && usbEndpointIn != null) {
//                break;
//            }
//        }



//        UsbEndpoint outEndpoint = null;
//
//        for(int j = 0; j < usbInterface.getEndpointCount(); ++j) {
//            UsbEndpoint endpoint = usbInterface.getEndpoint(j);
//            if(endpoint.getDirection() == 0 && endpoint.getType() == 2) {
////                outEndpoint = endpoint;
//            } else if(endpoint.getDirection() == 128 && endpoint.getType() == 2) {
//                outEndpoint = endpoint;
//            }
//            if( outEndpoint != null) {
//                break;
//            }
//        }
                UsbDeviceConnection connection = null;
                UsbInterface usbInterface = null;
                UsbEndpoint usbEndpointOut = null;
                UsbEndpoint usbEndpointIn = null;
                int interfaceCount = usbDevice.getInterfaceCount();
                for (int interfaceIndex = 0; interfaceIndex < interfaceCount; interfaceIndex++) {
                    usbInterface = usbDevice.getInterface(interfaceIndex);
                    for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                        UsbEndpoint ep = usbInterface.getEndpoint(i);
                        if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                                usbEndpointIn = ep;
                            } else {
                                usbEndpointOut = ep;
                            }
                        }
                    }
                    if ((null == usbEndpointIn) || (null == usbEndpointOut)) {
                        usbEndpointOut = null;
                        usbEndpointIn = null;
                    } else {
//                        mUsbInterface = usbInterface;
//                        mUsbDeviceConnection = mUsbManager.openDevice(device);
                        // 打开设备建立连接
                        connection = usbManager.openDevice(usbDevice);
                        connection.claimInterface(usbInterface, true);
                        break;
                    }
                }

        // 打印数据
//        data += "\n\n\n\n\n\n"; // 这个很重要
        byte[] printData = null;
        try {
            printData = data.getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (connection!=null){
            int out = connection.bulkTransfer(usbEndpointIn, printData, printData.length, 5000);
//            pushReceiptCash(context,connection,usbEndpointIn);
        }
        // 关闭连接
        connection.close();
        return true;
    }


    public static final byte[] PUSH_CASH = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};

    public static boolean pushReceiptCash(Context context,UsbDeviceConnection connection,UsbEndpoint usbEndpointIn) {
        boolean canPush = false;

        if (sendUsbCommand(context,connection,usbEndpointIn,PUSH_CASH)) {
            canPush = true;
        } else {
            canPush = false;
        }
        return canPush;
    }


    //发送信息 一是打印消息，切纸，打开钱箱等
    @SuppressLint("NewApi")
    public static boolean sendUsbCommand(Context context, UsbDeviceConnection connection,UsbEndpoint usbEndpointIn,byte[] Content) {
        boolean Result;
        synchronized (context) {
            int len = -1;
            if (connection != null) {
                len = connection.bulkTransfer(usbEndpointIn, Content, Content.length, 10000);
            }

            if (len < 0) {
                Result = false;
//                Log.i(TAG, "发送失败！ " + len);
            } else {
                Result = true;
//                Log.i(TAG, "发送" + len + "字节数据");
            }
        }
        return Result;
    }

}
