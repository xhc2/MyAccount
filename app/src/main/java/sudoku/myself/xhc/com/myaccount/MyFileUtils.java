package sudoku.myself.xhc.com.myaccount;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xhc on 2017/2/19.
 */

public class MyFileUtils {

    public static final String BACKUPPATH = "xhc_back_up";
    public static final String DATABASE = "sqlite-test.db";

    public void backUP() {
        String dbpath = "/data/data/sudoku.myself.xhc.com.myaccount/databases/"+DATABASE;

        makeFileDirSdcrad(getExternalStorageDirectory() + "/"+BACKUPPATH);
        boolean success = copyFile(dbpath, getExternalStorageDirectory() + "/"+BACKUPPATH+"/"
                + DATABASE);
    }

//    public static String getBackuppath(){
//        String result = getExternalStorageDirectory();
//        if(!TextUtils.isEmpty(result)){
//            result += File.separator+BACKUPPATH+File.separator +DATABASE;
//        }
//        return result;
//    }


    public void makeFileDirSdcrad(String path) {
        File f1 = new File(path);
        if (!f1.exists()) {
            f1.mkdirs();
        }
    }

    public static String getExternalStorageDirectory() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        if(sdDir == null){
            return "";
        }
        return sdDir.toString();

    }

    public boolean copyFile(String source, String dest) {
        try {
            File f1 = new File(source);
            File f2 = new File(dest);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);

            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("xhc", "exception " + e.getMessage());
            return false;
        }

        return true;

    }

}
