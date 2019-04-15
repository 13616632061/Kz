package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by admin on 2017/7/27.
 */
public class File_Utils {
    public static void copyFile(String oldPath, String newPath) {
         try {
             int bytesum = 0;
             int byteread = 0;
            File oldfile = new File(oldPath);
             if (oldfile.exists()) { //文件存在时   
                 InputStream inStream = new FileInputStream(oldPath); //读入原文件   
                 FileOutputStream fs = new FileOutputStream(newPath);
                 byte[] buffer = new byte[1444];
                 int length;
                 while ( (byteread = inStream.read(buffer)) != -1) {
                     bytesum += byteread; //字节数 文件大小   
                     System.out.println(bytesum);
                     fs.write(buffer,0,byteread);
                    }
                inStream.close();
                }
            }
        catch (Exception e){
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

            }
        }
//    public boolean rename(FileInfo fileInfo, String newName, boolean admitCopyName) {
//        //1.判断参数阈值
//        if (fileInfo == null || newName == null) {
////            Log.e(LOG_TAG, "Rename: null parameter");
//            return false;
//        }
//        //2.得到原文件全路径
//        String oldPath = fileInfo.getFilePath();
////        Log.d(LOG_TAG, "Rename---original path = " + oldPath));
//        //3-1.得到文件所在路径
//        String rootPath = Util.getPathFromFilepath(oldPath); //Util.getPathFromFilepath(String)-自定义方法：得到文件所在路径（即全路径去掉完整文件名）
//        //3-2.得到新全路径
//        String newPath = Util.makePath(rootPath, newName); //Util.makePath（String, String）-自定义方法：根据根路径和文件名形成新的完整路径
//        Log.d(LOG_TAG, "Rename---new Path = " + newPath);
//        //4.比较是否变更了名称
//        if (oldPath.endsWith(newPath)) { //和原来名称一样，不需要改变
//            return true;
//        }
//
//        try {
//            //5.根据新路径得到File类型数据
//            File newFile = new File(newPath);
//            //6.判断是否已经存在同样名称的文件（即出现重名）
//            if (newFile.exists() && !admitCopyName) { //出现重命名且不允许生成副本名
//                return false; //重命名失败
//            }
//            //7.循环判断直到生成可用的副本名
//            while (newFile.exists()) {
////                Log.w(LOG_TAG, "Rename---新文件路径名称已存在文件 ---" + newPath);
//                //重命名重名定义规范--Util.getCopyNameFromOriginal(String)-自定义方法：根据自定义规则生成原名称副本
//                newPath = Util.getCopyNameFromOriginal(newPath);
//                newFile = new File(newPath);
////                Log.i(LOG_TAG, "Rename---new copy Path = " + newPath);
//            }
//            //8.得到原文件File类型数据
//            File file = new File(oldPath);
//            //9.调用固有方法去重命名
//            boolean ret = file.renameTo(newFile);
////            Log.i(LOG_TAG, "Rename---改名成功？ " + ((ret) ? "yes!" : "no!"));
//            if (ret) {
//                //FIXME:这里通过更改形参来改变实参，是不好的写法，不建议这样写！
//                fileInfo.setFileName(Util.getNameFromFilepath(newPath)); //更新文件名
//                fileInfo.setmFilePath(newPath); //更新新路径
//            }
//            return ret;
//        } catch (SecurityException e) {
////            Log.e(LOG_TAG, "Fail to rename file," + e.toString());
//        }
//        return false;
//    }


}
