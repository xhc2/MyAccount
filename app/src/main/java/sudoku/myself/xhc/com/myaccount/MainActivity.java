package sudoku.myself.xhc.com.myaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements OnRecyleItemClick<Account> , OnItemLongClickLis<Account>{

    private final int ADDREQUEST = 100;
    private final int UPDATEREQUEST = 121;
    private final int BACKUPSUCCESS = 11;
    private final int BACKUPFAILD = 12;
    private final int FILE_SELECT_CODE = 322;
    private RecyclerView rcView;
    private MyRecyleAdapter adapter;
    private List<Account> list = new ArrayList<Account>();
    private AccountDao dao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm" , Locale.getDefault());
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BACKUPSUCCESS:
                    dismissDialog();
                    showToast(R.string.back_up_success);
                    updateUI();
                    break;
                case BACKUPFAILD:

                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dao = new AccountDao(MainActivity.this);
        adapter = new MyRecyleAdapter(list, this);
        adapter.addOnRecyleItemClick(this);
        adapter.setOnItemLongClickLis(this);
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //增加账本
                Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivityForResult(intent, ADDREQUEST);
            }
        });

        rcView = (RecyclerView) findViewById(R.id.rc_view);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcView.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDREQUEST && resultCode == Activity.RESULT_OK) {
            //添加成功
            updateUI();
        } else if (requestCode == UPDATEREQUEST && resultCode == Activity.RESULT_OK) {
            //更新成功
            updateUI();
        } else if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            if (resultCode == RESULT_OK) {
                String filePath = null;
                Uri uri = data.getData();

                if ("content".equalsIgnoreCase(uri.getScheme())) {
                    String[] projection = {"_data"};
                    Cursor cursor = null;

                    try {
                        cursor = MainActivity.this.getContentResolver().query(uri, projection, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow("_data");
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(column_index);
                        }
                    } catch (Exception e) {
                        // Eat it
                    }
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    filePath = uri.getPath();
                }

                if (!TextUtils.isEmpty(filePath)) {
                    startBackUp(filePath);
                } else {
                    showToast(R.string.not_file_back_file);
                }
            }

        }


    }

    public void updateUI() {
        List<Account> listTemp = dao.getAllByTime();
        list.clear();


        if (listTemp != null) {
            list.addAll(listTemp);
        }
        adapter.refreshAllData(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void startBackUp(String file) {
        showLoadDialog(getString(R.string.loading));
        new BackUpThread(file).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, CategoryAccount.class);
        //noinspection SimplifiableIfStatement
        if (id == R.id.backup) {
            //导入备份文件

            showFileChooser();
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.week_) {
            intent.putExtra("category", Constant.Config.WEEK);

        } else if (id == R.id.day_) {
            intent.putExtra("category", Constant.Config.DAY);
        } else if (id == R.id.month_) {
            intent.putExtra("category", Constant.Config.MONTH);
        } else if (id == R.id.year_) {
            intent.putExtra("category", Constant.Config.YEAR);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
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


    private class BackUpThread extends Thread {
        String strPath;

        BackUpThread(String file) {
            strPath = file;
        }

        @Override
        public void run() {
            super.run();
            AccountDao orgindao = new AccountDao();
            orgindao.getBackUpDataBase(strPath);
            List<Account> listTemp = orgindao.getAll();
            Log.e("xhc", " all " + listTemp);
            dao.addAll(listTemp);
            handler.sendEmptyMessage(BACKUPSUCCESS);
        }

    }

    @Override
    public void onLongClick(final Account account) {
        if(account == null) return ;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.sure_del)+
                        "\n "+getString(R.string.money)+":"+account.getMoney() +
                        "\n"+getString(R.string.date)+":"+sdf.format(account.getDate())+
                "\n"+getString(R.string.remark)+":"+account.getRemark())
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.delAccount(account);
                        list.remove(account);
                        adapter.refreshAllData(list);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();


    }

    @Override
    public void onItemClick(View v, Account account, int position) {
        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
        intent.putExtra("account", account);
        startActivityForResult(intent, UPDATEREQUEST);
    }
}
