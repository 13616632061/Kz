package android_serialport_api;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by admin on 2018/8/6.
 */

public class SerialPortOperaion {

    public static final int SERIAL_RECEIVED_DATA_MSG = 1;
    private static final String TAG = "SerialPortOperaion";
    private Handler mHandler;
    private SerialParam mSerialParam;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private ReadThread mReadThread;

    public SerialPortOperaion(Handler callback, SerialParam param) {
        this.mHandler = callback;
        this.mSerialParam = param;
    }

    public void StartSerial() throws IOException {
        this.mSerialPort = new SerialPort(this.mSerialParam.device, this.mSerialParam.baudrate, this.mSerialParam.openFlag);
        this.mOutputStream = this.mSerialPort.getOutputStream();
        this.mInputStream = this.mSerialPort.getInputStream();
        this.mReadThread = new ReadThread();
        this.mReadThread.start();
    }

    public void StopSerial() {
        try {
            this.mInputStream.close();
            this.mOutputStream.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        this.mInputStream = null;
        this.mOutputStream = null;
        this.mSerialPort.close();

        try {
            this.mReadThread.join(5000L);
        } catch (InterruptedException var2) {
            ;
        }

    }

    public void WriteData(int... data) {
        try {
            for(int i = 0; i < data.length; ++i) {
                this.mOutputStream.write(data[i]);
            }

            this.mOutputStream.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void WriteData(byte[] data) {
        try {
            this.mOutputStream.write(data);
            this.mOutputStream.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    class ReadThread extends Thread {
        ReadThread() {
        }

        public void run() {
            super.run();

            while(!this.isInterrupted()) {
                try {
                    byte[] buffer = new byte[1024];
                    if(SerialPortOperaion.this.mInputStream == null) {
                        return;
                    }

                    int size = SerialPortOperaion.this.mInputStream.read(buffer);
                    if(size > 0 && SerialPortOperaion.this.mHandler != null) {
                        Message msg = SerialPortOperaion.this.mHandler.obtainMessage(1, SerialPortOperaion.this.new SerialReadData(buffer, size));
                        SerialPortOperaion.this.mHandler.sendMessage(msg);
                    }
                } catch (IOException var4) {
                    var4.printStackTrace();
                    return;
                }
            }

        }
    }

    public class SerialReadData {
        public byte[] data;
        public int size;

        SerialReadData(byte[] buf, int n) {
            this.data = buf;
            this.size = n;
        }
    }

}
