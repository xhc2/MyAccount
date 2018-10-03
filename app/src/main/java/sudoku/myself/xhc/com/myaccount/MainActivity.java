package sudoku.myself.xhc.com.myaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements OnRecyleItemClick<Account> , OnItemLongClickLis<Account>{

    private final int ADDREQUEST = 100;
    private final int UPDATEREQUEST = 121;
    private final int BACKUPSUCCESS = 11;
    private final int BACKUPFAILD = 12;

    private final int UPDATEUI = 112;
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
                case UPDATEUI:
                    adapter.refreshAllData(list);
                    dismissDialog();
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
        updateUI();
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
        }
    }



    class UpdateUI extends Thread{
        @Override
        public void run() {
            super.run();
            List<Account> listTemp = dao.getAllByTime();
            list.clear();
            if (listTemp != null) {
                list.addAll(listTemp);
            }
            handler.sendEmptyMessage(UPDATEUI);
            updateUI = null;
        }
    }

    private   UpdateUI updateUI = null;



    private void startUpdateUI(){
        if(updateUI == null){
            updateUI = new UpdateUI();
            updateUI.start();
        }
    }

    public void updateUI() {
        showLoadDialog(R.string.loading);
        startUpdateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, CategoryAccount.class);
        if (id == R.id.setting) {
            //设置
            Intent intent2 = new Intent(MainActivity.this,SetttingActivity.class);
            startActivity(intent2);
//            showFileChooser();
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
