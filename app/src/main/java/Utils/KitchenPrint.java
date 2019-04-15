package Utils;

import android.content.Context;

/**
 * Created by admin on 2019/3/2.
 */

public class KitchenPrint {

    public static boolean PrintfData(byte[]data, Context context) {
        byte SendCut[]={0x0a,0x0a,0x1d,0x56,0x01};

        Socketmanager mSockManager=new Socketmanager(context);
        mSockManager.threadconnectwrite(data);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mSockManager.getIstate()) {
            return true;
        }
        else {
            return false;
        }
    }


}
