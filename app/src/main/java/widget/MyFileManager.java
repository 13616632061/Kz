package widget;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Fragments.Buyoff_Fragment;
import adapters.MyAdapter;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/10.
 */
public class MyFileManager extends ListActivity implements View.OnClickListener {
    /**
     * 文件（夹）名字
     */
    private List<String> items = null;
    /**
     * 文件（夹）路径
     */
    private List<String> paths = null;
    /**
     * 根目录
     **/
    private String rootPath = "/";
    /**
     * 当前目录
     **/
    private String curPath = "/mnt/";
    /**
     * 显示当前目录
     **/
    private TextView mPath;

    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.fileselect);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        mPath = (TextView) findViewById(R.id.mPath);
        buttonConfirm.setOnClickListener(this);
        findViewById(R.id.buttonCancle).setOnClickListener(this);

        getFileDir(curPath);
    }

    /**
     * 获取指定目录下的所有文件(夹)
     *
     * @param filePath
     */
    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        //用来显示 “返回根目录”+"上级目录"
        if (!filePath.equals(rootPath)) {
            items.add("rootPath");
            paths.add(rootPath);

            items.add("parentPath");
            paths.add(f.getParent());
        }
//先排序
        List<File> resultList = null;
        if (files != null) {
            Log.i("hnyer", files.length + " " + filePath);
            resultList = new ArrayList<File>();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.getName().startsWith(".")) {
                    resultList.add(file);
                }
            }

            //
            Collections.sort(resultList, new Comparator<File>() {
                @Override
                public int compare(File bean1, File bean2) {
                    return bean1.getName().toLowerCase().compareTo(bean2.getName().toLowerCase());

                }
            });

            for (int i = 0; i < resultList.size(); i++) {
                File file = resultList.get(i);
                items.add(file.getName());
                paths.add(file.getPath());
            }
        } else {
            Log.i("hnyer", filePath + "无子文件");
        }

        setListAdapter(new MyAdapter(this, items, paths));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));
        if (file.isDirectory()) {
            curPath = paths.get(position);
            getFileDir(paths.get(position));
        } else {
            curPath += "/" + file.getName();
            buttonConfirm.performClick();
//            openFile(file);
            Log.d("print", "路径是什么" + curPath);
        }
    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        startActivity(intent);
    }

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.buttonConfirm:

                Intent data = new Intent(MyFileManager.this, Buyoff_Fragment.class);
                Bundle bundle = new Bundle();
                bundle.putString("file", curPath);
                data.putExtras(bundle);
                setResult(2, data);
                finish();


                break;
            case R.id.buttonCancle:
                finish();
                break;

        }

    }


}
