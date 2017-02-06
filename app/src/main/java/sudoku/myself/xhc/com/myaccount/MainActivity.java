package sudoku.myself.xhc.com.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnRecyleItemClick<Account>{

    private final int ADDREQUEST = 100;
    private final int UPDATEREQUEST = 121;
    private RecyclerView rcView;
    private MyRecyleAdapter adapter;
    private List<Account> list = new ArrayList<Account>();
    private AccountDao dao ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        dao  = new AccountDao(MainActivity.this);
        adapter = new MyRecyleAdapter(list, this);
        adapter.addOnRecyleItemClick(this);
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
        }
        else if(requestCode == UPDATEREQUEST && resultCode == Activity.RESULT_OK){
            //更新成功
            updateUI();
        }

    }

    public void updateUI(){
        List<Account> listTemp =  dao.getAllByTime();
        list.clear();
        if(listTemp != null){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.week_) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View v, Account account, int position) {
        Intent intent = new Intent(MainActivity.this , AddAccountActivity.class);
        intent.putExtra("account" , account);
        startActivityForResult(intent , UPDATEREQUEST);
    }
}
