package Utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签打印机
 */
public class UsbPrinterUtil {

    public static final String TAG = "UsbPrinter";

    private final Context mContext;
    private final UsbManager mUsbManager;
    private volatile List<UsbDevice> mUsbPrinterList = null;

    public static String ACTION_USB_PERMISSION = "com.posin.usbdevice.USB_PERMISSION";

    public static interface OnUsbPermissionCallback {
        public void onUsbPermissionEvent(UsbDevice dev, boolean granted);
    }

    private OnUsbPermissionCallback onPermissionCallback = null;

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());

            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (onPermissionCallback != null)
                        onPermissionCallback.onUsbPermissionEvent(device, true);
                } else {
                    if (onPermissionCallback != null)
                        onPermissionCallback.onUsbPermissionEvent(device, false);
                }
                context.unregisterReceiver(this);
            }
        }
    };

    public UsbPrinterUtil(Context context) {
        mContext = context;
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    public List<UsbDevice> getUsbPrinterList() {
        if (mUsbPrinterList == null)
            mUsbPrinterList = findAllUsbPrinter();
        return mUsbPrinterList;
    }


    public boolean requestPermission(UsbDevice usbDevice, OnUsbPermissionCallback callback) {
        if (!mUsbManager.hasPermission(usbDevice)) {
            IntentFilter ifilter = new IntentFilter(ACTION_USB_PERMISSION);
            mContext.registerReceiver(mReceiver, ifilter);

            PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, new Intent(
                    ACTION_USB_PERMISSION), 0);

            onPermissionCallback = callback;
            mUsbManager.requestPermission(usbDevice, pi);
            return false;
        } else {
            return true;
        }
    }

    private List<UsbDevice> findAllUsbPrinter() {
        final List<UsbDevice> result = new ArrayList<UsbDevice>();

        Log.d(TAG, "find usb printer...");
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
//        	Log.d(TAG, String.format("usb %04X:%04X : device_id=%d, device_name=%s",
//        			usbDevice.getVendorId(), usbDevice.getProductId(),
//        			usbDevice.getDeviceId(), usbDevice.getDeviceName()));
            if (isUsbPrinterDevice(usbDevice)) {
                Log.d(TAG, String.format("usb printer %04X:%04X : device_id=%d, device_name=%s",
                        usbDevice.getVendorId(), usbDevice.getProductId(),
                        usbDevice.getDeviceId(), usbDevice.getDeviceName()));
                result.add(usbDevice);
            }
        }

        return result;
    }

    public static boolean isUsbPrinterDevice(final UsbDevice usbDevice) {
        final int vid = usbDevice.getVendorId();
        final int pid = usbDevice.getProductId();

        return (vid == 34918) ||
                (vid == 1137) ||
                (vid == 1659) ||
                (vid == 1137) ||
                (vid == 1155)&&(pid==22339) ||
                (vid == 11575)&&(pid==17046) ||
                (vid == 1208) ||
                (vid == 26728) ||
                (vid == 17224) ||
                (vid == 7358);
    }

}
