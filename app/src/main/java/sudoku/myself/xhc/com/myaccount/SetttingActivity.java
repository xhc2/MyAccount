package sudoku.myself.xhc.com.myaccount;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import static sudoku.myself.xhc.com.myaccount.Constant.Config.BACKUP;

public class SetttingActivity extends BaseActivity implements View.OnClickListener{
    private final int FILE_SELECT_CODE = 322;
    private final int BACKUPSUCCESS = 11;
    private final int BACKUPFAILD = 12;
    private final int BACKUPWRONG = 13;
    private AccountDao dao;
    private RelativeLayout rlBackUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlBackUp = (RelativeLayout)findViewById(R.id.rl_get_back_up);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.setting);
        toolbar.setSubtitle(R.string.setting);
        dao = new AccountDao(this);
        rlBackUp.setOnClickListener(this);
        getSdcardPermisstion();
    }

    private void getSdcardPermisstion() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        100);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被允许
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showToast( R.string.should_open_sdcard_permisstion);
                finish();
            } else {
                // 还是没开权限
                showToast( R.string.should_open_sdcard_permisstion);
                finish();
            }
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BACKUPSUCCESS:
                    dismissDialogDelay(R.string.back_up_success);
                    break;
                case BACKUPFAILD:
                    dismissDialogDelay(R.string.back_up_faild);
                    break;
                case BACKUPWRONG:
                    dismissDialogDelay(R.string.back_up_wrong);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
                String filePath = null;
                Uri uri = data.getData();
                Log.e("xhc" , " uri "+uri.toString());
                Log.e("xhc" , " scheme "+uri.getScheme());
                if ("content".equalsIgnoreCase(uri.getScheme())) {
                    String[] projection = {"_data"};
                    Cursor cursor = null;

                    try {
                        cursor = SetttingActivity.this.getContentResolver().query(uri, projection, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow("_data");

                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(column_index);
                        }
                        Log.e("xhc"," cursor "+cursor+" filePath "+filePath);

                    } catch (Exception e) {
                        // Eat it
                        Log.e("xhc" , "exception "+e.getMessage());
                    }finally {
                        cursor.close();
                    }
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    filePath = uri.getPath();
                }
                Log.e("xhc" ,"filepath "+filePath);
                if (!TextUtils.isEmpty(filePath)) {
                    startBackUp(filePath);
                } else {
                    showToast(R.string.not_file_back_file);
                }

        }
    }


    /**
     * 为了不让数据重复，或者错误
     * 1.需要导入的数据库的账单第一条数据和apk中的第一条数据相比
     * 一样就不导入
     * 不一样就导入
     */
    private class BackUpThread extends Thread {
        String strPath;

        BackUpThread(String file) {
            strPath = file;
        }

        @Override
        public void run() {
            super.run();

            AccountDao versionDao = new AccountDao();
            AccountDao apkVersiondao = new AccountDao(SetttingActivity.this);
            if(!versionDao.getBackUpDataBase(strPath)){
                handler.sendEmptyMessage( BACKUPFAILD);
                return ;
            }
            long apkVersion = apkVersiondao.getVersion(); //(long)SPUtils.get(SetttingActivity.this , BACKUP , 0L);
            //apk的时间和数据库的时间相比较
            Log.e("xhc"  , " apk version "+apkVersion+" dao "+versionDao.getVersion());
            if(apkVersion == versionDao.getVersion()){
                //不需要备份
                handler.sendEmptyMessage(BACKUPWRONG);
                return ;
            }
            AccountDao orgindao = new AccountDao();
            orgindao.getBackUpDataBase(strPath);

            List<Account> listTemp = orgindao.getAll();
            dao.addAll(listTemp);
            handler.sendEmptyMessage(BACKUPSUCCESS);
        }

    }

    private void startBackUp(String file) {
        showLoadDialog(getString(R.string.loading));
        new BackUpThread(file).start();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    //后期可以用递归实现查找，现在先写死吧
    private String findPath(){
        String string = "sdcard/account_back_up/sqlite-test.db";
        return string;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_get_back_up:
                startBackUp(findPath());
                break;
        }
    }
}
