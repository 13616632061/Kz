package Utils;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by admin on 2017/6/30.
 */
public class MyXMLReader {

    long lasting =System.currentTimeMillis();
    public static File f=new File(Environment.getExternalStorageDirectory().getPath() + "/Download/"+"text.xml");
    public static DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();

    public static DocumentBuilder builder;


    public static String pagetee() {
        String versionCode=null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            NodeList nl = doc.getElementsByTagName("updateInfo");
            for (int i=0;i<nl.getLength();i++){
                String packageName=doc.getElementsByTagName("packageName").item(i).getFirstChild().getNodeValue();
                versionCode=doc.getElementsByTagName("versionCode").item(i).getFirstChild().getNodeValue();
                String versionName=doc.getElementsByTagName("versionName").item(i).getFirstChild().getNodeValue();
                String forceUpdate=doc.getElementsByTagName("forceUpdate").item(i).getFirstChild().getNodeValue();
                String autoUpdate=doc.getElementsByTagName("autoUpdate").item(i).getFirstChild().getNodeValue();
                String apkUrl=doc.getElementsByTagName("apkUrl").item(i).getFirstChild().getNodeValue();
                return versionCode;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
