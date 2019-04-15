package Utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by admin on 2017/6/26.
 */
public class UtilPrint  {
    public static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public Context context;

    public UtilPrint(Context context) {
        this.context=context;
        mService = new BluetoothService(context, mHandler);
        if( mService.isAvailable() == false ){
            Toast.makeText(context, "蓝牙不可用", Toast.LENGTH_LONG).show();
        }

    }

    public void printString(){
        byte[] cmd = new byte[3];
        cmd[2] |= 0x10;
        mService.write(cmd);
        String str= BluetoothPrintFormatUtil.printTitle("易之星");
        mService.sendMessage(str, "GBK");
        cmd[2] &= 0xEF;
        mService.write(cmd);
        LinkedHashMap<String,LinkedList<String>> linkedHashMap=new LinkedHashMap<String, LinkedList<String>>();
        LinkedList<String> linkedList=new LinkedList<String>();
        for (int i=0;i<3;i++){
            linkedList.add("棒棒糖"+"$"+"2"+"$"+"1");
        }
        linkedHashMap.put("",linkedList);
        String str1= BluetoothPrintFormatUtil.printMenuMSG(linkedHashMap);
        mService.sendMessage(str1+"\n","GBK");
        mService.sendMessage("--------------"+"\n","GBK");
        mService.sendMessage("消费合计:"+"50"+"\n","GBK");
        if (1 == 1) {
            mService.sendMessage("移动收银:"+"50"+"\n","GBK");
        }
        if (1 == 0) {
            mService.sendMessage("现金收银:"+""+"\n","GBK");
        }
        mService.sendMessage("找零:"+"0"+"\n","GBK");
        mService.sendMessage("交易单号:"+"170624114497414"+"\n","GBK");
        mService.sendMessage("找零:"+"0"+"\n","GBK");
        mService.sendMessage("服务热线：400-xxx-xxx-xxx"+"\n","GBK");
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(context, "连接成功",
                                    Toast.LENGTH_SHORT).show();

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(context, "设备连接丢失",
                            Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    if (context!=null){
//                        Toast.makeText(context, "关闭设备",
//                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };

}
