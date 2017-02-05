package sudoku.myself.xhc.com.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected  void showToast(String msg){

        Toast.makeText(this , msg , Toast.LENGTH_SHORT).show();
    }

    protected  void showToast(int stringId){

        showToast(getString(stringId));
    }



}
