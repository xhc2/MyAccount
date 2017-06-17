package sudoku.myself.xhc.com.myaccount;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xhc on 2017/2/5.
 */

public class BackUpData  {

    private static final String FileFormat = "GBK";
    private String backUpFile = "back_up";

    private Context context ;

    public BackUpData(Context context ){
        this.context = context;
        createDirectory();
        createFile();
    }


    //是否有sdcard
    private boolean isUseSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private File getRootFile(){
        File sdCardDir = Environment.getExternalStorageDirectory();
        return  new File(sdCardDir ,getAppName(context) );
    }

    private File getBackFile(){
        return new File(getRootFile().getAbsolutePath() + "/" + backUpFile);
    }

    /*可以创建多层目录但是不能创建文件*/
    private void createDirectory() {
        if (isUseSdcard()) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File file = getRootFile();

            if (!file.exists()) {
                try {
                        /*只能创建目录*/
                    file.mkdirs();
//                    Toast.makeText(context, file.getPath(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e("xhc", " exception " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(context, "sdcard有问题", Toast.LENGTH_LONG).show();
        }
    }

    /*创建文件*/
    private void createFile( ) {
        File file = new File(getRootFile().getAbsolutePath() + "/" + backUpFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.e("back" , "root  file"+file.getAbsolutePath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void appendAccount(Account account){
        StringBuilder sb = new StringBuilder();

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(getBackFile(), true);
            byte[] buffer = sb.toString().getBytes(FileFormat);
			/*是重这个数组中的第几个位置开始写*/
            fileOutputStream.write(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }

            }
        }
    }

    /**
     * 获取app name
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
