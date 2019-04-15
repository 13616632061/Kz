package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * 标签打印机
 */

public class UsbPrinter {

	private static final String TAG = "UsbPrinter";

	private final UsbDevice mDevice;
	private final UsbDeviceConnection mConnection;
	private final UsbInterface mInterface;
	private final UsbEndpoint mEndpoint;

	private static final int TRANSFER_TIMEOUT = 5000;

	Vector<Byte> Command = null;

	public UsbPrinter(Context context, UsbDevice device) throws IOException {
		UsbInterface iface = null;
		UsbEndpoint epout = null;
		
		for(int i=0; i<device.getInterfaceCount(); i++) {
			iface = device.getInterface(i);
			if (iface == null)
				throw new IOException("failed to get interface "+i);

			int epcount = iface.getEndpointCount();
			for (int j = 0; j < epcount; j++) {
				UsbEndpoint ep = iface.getEndpoint(j);
				if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
					epout = ep;
					break;
				}
			}
			
			if(epout != null)
				break;
		}

		if (epout == null) {
			throw new IOException("no output endpoint.");
		}

		mDevice = device;
		mInterface = iface;
		mEndpoint = epout;

		UsbManager usbman = (UsbManager) context
				.getSystemService(Context.USB_SERVICE);
		mConnection = usbman.openDevice(device);

		if (mConnection == null) {
			throw new IOException("failed to open usb device.");
		}

		mConnection.claimInterface(mInterface, true);
	}

	public void write(byte[] data) throws IOException {
		if (mConnection.bulkTransfer(mEndpoint, data, data.length,
				TRANSFER_TIMEOUT) != data.length)
			throw new IOException("failed to write usb endpoint.");
	}

	public void close() {
		mConnection.releaseInterface(mInterface);
		mConnection.close();
	}

	public static enum ALIGNMENT {
		LEFT, CENTER, RIGHT,
	}
	
	public static enum FONT {
		FONT_A,
		FONT_B,
		FONT_C,
	}

	public void selectAlignment(ALIGNMENT alignment) throws IOException {
		int iAlignment = 0;

		switch (alignment) {
		case LEFT:
			iAlignment = 0;
			break;
		case CENTER:
			iAlignment = 1;
			break;
		case RIGHT:
			iAlignment = 2;
			break;
		default:
			iAlignment = 0;
		}

		String command = String.format("ESC a %d", iAlignment);
		byte[] b = EscposUtil.convertEscposToBinary(command);
		if (b != null)
			write(b);
	}

	public void printBitmap(Bitmap b,int nWidth,int x,int y){
		if(b != null) {
			byte[] h=null;
			int width = (nWidth + 7) / 8 * 8;
			int height = b.getHeight() * width / b.getWidth();
			Log.d("BMP", "bmp.getWidth() " + b.getWidth());
			Bitmap grayBitmap = LabelUtils.toGrayscale(b);
			Bitmap rszBitmap = LabelUtils.resizeImage(grayBitmap, width, height);
			byte[] src = LabelUtils.bitmapToBWPix(rszBitmap);
			height = src.length / width;
			width /= 8;
			String str1 = "BITMAP " + x + "," + y + "," + width + "," + height + "," + 0 + ",";
			try {
				h = str1.getBytes("GB2312");
			} catch (Exception e) {
				e.printStackTrace();
			}
			byte[] codecontent = LabelUtils.pixToLabelCmd(src);
			Log.d("LabelCommand", "codecontent" + codecontent);
			try {
				write(h);
				write(codecontent);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			for(int k = 0; k < codecontent.length; ++k) {
//					this.Command.add(Byte.valueOf(codecontent[k]));
//			}
			Log.d("LabelCommand", "codecontent" + codecontent);
		}


	}
	public   void downloadbmp(Bitmap bitmap){
//		try {
//			FileInputStream fis=new FileInputStream("");
//			byte[] data=new byte[fis.available()];
//			String download="DOWNLOAD F,\""+filname+"\","+data.length+",";
//			byte[] downhead=download.getBytes();
//			write(downhead);
//			write(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		String str1 = "SIZE 71mm,35mm\\nGAP 0,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS\\nBITMAP " + 80 + "," + 60 + "," + bitmap.getWidth() + "," + bitmap.getHeight() + "," + 0 + ",";
		String str2="\\nPRINT 1,1";
		try {
			byte[] k= EscposUtil.convertEscposToBinary(str1);
			byte[] k2= EscposUtil.convertEscposToBinary(str2);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] byteArray = baos.toByteArray();

			write(k);
			write(byteArray);
//			write(k2);

		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	private void addStrToCommand(String str) {
		byte[] bs = null;
		if(!str.equals("")) {
			try {
				bs = str.getBytes("GB2312");
			} catch (UnsupportedEncodingException var4) {
				var4.printStackTrace();
			}

			for(int i = 0; i < bs.length; ++i) {
				this.Command.add(Byte.valueOf(bs[i]));
			}
		}

	}

	public void printString(String string, FONT font, Boolean bold, Boolean underlined, Boolean doubleHeight, Boolean doubleWidth) throws IOException {
	    int options = 0;
	    String command = "";
	    byte[] b;

      b = EscposUtil.convertEscposToBinary(command);
      if(b != null) write(b);

	      command = String.format("'%s' LF", string );
	      b = EscposUtil.convertEscposToBinary(command);
	      if(b != null)
		      write(b);
	}
	
	public void cutPaper() throws IOException
	  {
	      String command ="BEEP";
	      byte[] b = EscposUtil.convertEscposToBinary(command);
	      if(b != null)
	      	write(b);

		  command = String.format("'%s' LF", command);
		  b = EscposUtil.convertEscposToBinary(command);
		  if(b != null)
			  write(b);
	  }
	/*public void cutPaper() throws IOException
	{
		String command = "GS V 65 20";
		byte[] b = EscposUtil.convertEscposToBinary(command);
		if(b != null)
			write(b);
	}*/
}
