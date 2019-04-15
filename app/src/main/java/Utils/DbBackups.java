package Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by admin on 2018/9/2.
 * 数据库备份的工具类
 */

public class DbBackups extends AsyncTask<String, Void, Integer> {

        private static final String COMMAND_BACKUP = "backupDatabase";
        public static final String COMMAND_RESTORE = "restroeDatabase";
        private static final int BACKUP_SUCCESS = 1;
        public static final int RESTORE_SUCCESS = 2;
        private static final int BACKUP_ERROR = 3;
        public static final int RESTORE_NOFLEERROR = 4;
        private Context mContext;
        public DbBackups(Context context) {
                this.mContext = context;
        }
        @Override
        protected Integer doInBackground(String... params) {
                // TODO Auto-generated method stub
                File dbFile = mContext.getDatabasePath("yonghu.db");  // 获得数据库路径,默认是/data/data/(包名)org.dlion/databases/
                File exportDir = new File(Environment.getExternalStorageDirectory(),
                        "dbBackup");
                if (!exportDir.exists()) {
                        exportDir.mkdirs();
                }
                File backup = new File(exportDir, dbFile.getName());
                String command = params[0];
                if (command.equals(COMMAND_BACKUP)) {
                        try {
                                backup.createNewFile();
                                fileCopy(dbFile, backup);
                                return BACKUP_SUCCESS;
                        } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                return BACKUP_ERROR;
                        }
                } else if (command.equals(COMMAND_RESTORE)) {
                        try {
                                fileCopy(backup, dbFile);
                                return RESTORE_SUCCESS;
                        } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                return RESTORE_NOFLEERROR;
                        }
                } else {
                        return BACKUP_ERROR;
                }
        }
        private void fileCopy(File dbFile, File backup) throws IOException {
                // TODO Auto-generated method stub
                FileChannel inChannel = new FileInputStream(dbFile).getChannel();
                FileChannel outChannel = new FileOutputStream(backup).getChannel();
                try {
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } finally {
                        if (inChannel != null) {
                                inChannel.close();
                        }
                        if (outChannel != null) {
                                outChannel.close();
                        }
                }
        }
        protected void onPostExecute(Integer result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                switch (result) {
                        case BACKUP_SUCCESS:
                                Toast.makeText(mContext,"数据库备份成功",Toast.LENGTH_SHORT).show();
                                break;
                        case BACKUP_ERROR:
                                Toast.makeText(mContext,"数据库备份失败",Toast.LENGTH_SHORT).show();
                                break;
                        case RESTORE_SUCCESS:
                                Toast.makeText(mContext,"数据库还原成功",Toast.LENGTH_SHORT).show();
                                break;
                        case RESTORE_NOFLEERROR:
                                Toast.makeText(mContext,"数据库还原失败",Toast.LENGTH_SHORT).show();
                                break;
                        default:
                                break;
                }
        }
}