package sudoku.myself.xhc.com.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryAccount extends AppCompatActivity {

    private RecyclerView rcView ;
    private int category = Constant.Config.DAY;
    private AccountDao dao ;
    private List<List<Account>> list = new ArrayList<>();
    private CategoryAccountAdapter adapter ;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rcView = (RecyclerView)findViewById(R.id.rc_view);
        dao = new AccountDao(this);
        Intent intent = getIntent();

        category = intent.getIntExtra("category" ,Constant.Config.DAY );
        switch (category) {
            case Constant.Config.DAY:
                title = getString(R.string.day_);
                break;
            case Constant.Config.WEEK:
                title = getString(R.string.week_);
                break;
            case Constant.Config.MONTH:
                title = getString(R.string.month_);
                break;
            case Constant.Config.YEAR:
                title = getString(R.string.year_);
                break;
        }
        Log.e("xhc" , "titile -> "+title);
//        toolbar.setTitle(title);


        List<List<Account>> listTemp = null;
        if(category != Constant.Config.WEEK){
            listTemp = dao.getCategoryData(category);
        }
        else{
            Log.e("xhc" , " week ");
            listTemp = dao.getWeekData();
        }
        list.addAll(listTemp);
        adapter = new CategoryAccountAdapter(list , this);

        adapter.setCategory(category);
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

        return super.onOptionsItemSelected(item);
    }
}
