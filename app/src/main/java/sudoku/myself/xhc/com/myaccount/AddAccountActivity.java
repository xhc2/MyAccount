package sudoku.myself.xhc.com.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAccountActivity extends BaseActivity implements TagClickListener, View.OnFocusChangeListener {

    private EditText etMoney, etWhere, etCategory, etDate, etRemark;

    private TextView tvAccount;

    private ToggleButton tbType;

    private FlowLayout flowLayout;

    private Account account = null;

    private boolean modifyFlag = false;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private AccountDao dao ;

    private String categoryItem[] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findView();
        init();
        setListener();
    }

    private void findView() {
        etMoney = (EditText) findViewById(R.id.et_money);
        etWhere = (EditText) findViewById(R.id.et_money_for_what);
        etCategory = (EditText) findViewById(R.id.et_category);
        etDate = (EditText) findViewById(R.id.et_time);
        etRemark = (EditText) findViewById(R.id.et_remark);
        tvAccount = (TextView) findViewById(R.id.tv_type);
        tbType = (ToggleButton) findViewById(R.id.tb_type);
        flowLayout = (FlowLayout) findViewById(R.id.tag_flowlayout);

    }
     private BackUpData backUpData ;
    private void init() {
        backUpData = new BackUpData(this);
        categoryItem = getResources().getStringArray(R.array.category_item);
        dao = new AccountDao(this);
        Intent intent = getIntent();
        account = intent.getParcelableExtra("account");
        if (account != null) {
            //修改
            modifyFlag = true;
            etMoney.setText(""+account.getMoney());
            Log.e("xhc" , "category "+account.getCategory());
            etWhere.setText(account.getWhy());

        } else {
            //新增
            modifyFlag = false;
            account = new Account();
            account.setDate(System.currentTimeMillis());
        }
        updateUI();
    }

    private void updateUI(){

        etDate.setText(sdf.format(new Date(account.getDate())));
        etRemark.setText(account.getRemark());
        if (account.getType() == Account.INCOME) {
            tbType.setChecked(true);
//            tvAccount.setText(R.string.income);
//            account.setType(Account.INCOME);
        } else {
            tbType.setChecked(false);
//            tvAccount.setText(R.string.expend);
//            account.setType(Account.EXPEND);
        }
    }

    private void setListener() {
        tbType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("xhc" , "收入 - 支出"+b);
                if (b) {
                    tvAccount.setText(R.string.income);
                    account.setType(Account.INCOME);
                } else {
                    tvAccount.setText(R.string.expend);
                    account.setType(Account.EXPEND);
                }
            }
        });
        flowLayout.setTagClickListener(this);
        etWhere.setOnFocusChangeListener(this);
        etCategory.setOnFocusChangeListener(this);
        etDate.setOnFocusChangeListener(this);
        etRemark.setOnFocusChangeListener(this);
        etMoney.setOnFocusChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String strMoney = etMoney.getText().toString();
        String strWhy = etWhere.getText().toString();
        String dateStr = etDate.getText().toString();

        if(TextUtils.isEmpty(dateStr)){
            showToast(R.string.input_date_str);
            return ;
        }
        else{
            try{
               Date date =  sdf.parse(dateStr);
                account.setDate(date.getTime());
            }
            catch(Exception e){
                showToast(R.string.input_date);
                return ;
            }
        }

        if(TextUtils.isEmpty(strWhy)){
            showToast(R.string.input_category);
            return ;
        }
        if(account.getCategory() == 0){
            showToast(R.string.input_category);
            return ;
        }

        if(TextUtils.isEmpty(strMoney)){
            showToast(R.string.input_money);
            return ;
        }
        else{
            account.setMoney(Float.parseFloat(strMoney));
        }

        account.setWhy(strWhy);
        account.setRemark(etRemark.getText().toString());
        if (modifyFlag) {
            //是修改
            dao.add(account);
        } else {
            dao.add(account);
        }
        setResult(RESULT_OK);
        finish();

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == etWhere && b) {
            flowLayout.setVisibility(View.VISIBLE);
        } else if (view == etCategory && b) {
            flowLayout.setVisibility(View.GONE);
        } else {
            flowLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void clickTag(int id, View view) {
        String str = ((TextView) view).getText().toString();
        etWhere.setText(str);
        etWhere.setEnabled(false);
        switch (id) {
            case R.id.tag_breakfast:
                account.setCategory(Account.BREAKFAST);
                break;
            case R.id.tag_launch:
                account.setCategory(Account.LAUNCH);
                break;
            case R.id.tag_dinner:
                account.setCategory(Account.DINNER);
                break;
            case R.id.tag_snack:
                account.setCategory(Account.SNACK);
                break;
            case R.id.tag_shopping:
                account.setCategory(Account.SHOPPING);
                break;
            case R.id.tag_travel:
                account.setCategory(Account.TRAVEL);
                break;
            case R.id.tag_haircut:
                account.setCategory(Account.HAIRCUT);
                break;
            case R.id.tag_transporttation:
                account.setCategory(Account.TRANSPORTATION);
                break;
            case R.id.tag_treatment:
                account.setCategory(Account.TREATMENT);
                break;
            case R.id.tag_house:
                account.setCategory(Account.HOUSE);
                break;
            case R.id.tag_other:
                etWhere.setEnabled(true);
                account.setCategory(Account.OTHER);
                etWhere.setText("");
                break;
        }

    }
}
