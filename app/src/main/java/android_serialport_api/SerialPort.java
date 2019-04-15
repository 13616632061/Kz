/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream=null;
	private FileOutputStream mFileOutputStream=null;


	public SerialPort(String dev, int baudrate, int flags) throws IOException {
		this.OpenSerialPort(new File(dev), baudrate, flags);
	}


	private void OpenSerialPort(File device, int baudrate, int flags) throws IOException {
		this.mFd = open(device.getAbsolutePath(), baudrate, flags);
		if(this.mFd == null) {
			Log.e("SerialPort", "native open returns null");
			throw new IOException();
		} else {
			this.mFileInputStream = new FileInputStream(this.mFd);
			this.mFileOutputStream = new FileOutputStream(this.mFd);
		}
	}


	String do_exec(String cmd) {
		String s = "\n";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				s += line + "/n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmd;
	}


	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");

				Log.d("print", "run:的数据"+ device.getAbsolutePath());
//				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
//						+ "exit\n";


//				do_exec("adb shell setenforce 0");
				Log.d(TAG, "SerialPort1111: "+do_exec("adb shell setenforce 0"));
				su.getOutputStream().write(new StringBuilder("chmod 666 ").append(device.getAbsolutePath()).append("\n").append("exit\n").toString().getBytes());

//				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			mFileInputStream = null;
			mFileOutputStream = null;
			throw new IOException();
		}else {
			mFileInputStream = new FileInputStream(mFd);
			mFileOutputStream = new FileOutputStream(mFd);
		}

	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
	private native static FileDescriptor open(String path, int baudrate, int flags);
	public native void close();
	static {
		System.loadLibrary("serial_port");
	}
}
