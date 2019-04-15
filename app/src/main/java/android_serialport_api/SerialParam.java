package android_serialport_api;

/**
 * Created by admin on 2018/8/6.
 */

public class SerialParam {
    public int baudrate;
    public String device;
    public int openFlag;
    public SerialParam(int baud, String dev, int flag) {
        this.baudrate = baud;
        this.device = dev;
        this.openFlag = flag;
    }

}
