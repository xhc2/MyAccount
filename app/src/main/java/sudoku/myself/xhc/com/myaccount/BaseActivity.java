package sudoku.myself.xhc.com.myaccount;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };

    protected  void showToast(String msg){

        Toast.makeText(this , msg , Toast.LENGTH_SHORT).show();
    }

    protected  void showToast(int stringId){

        showToast(getString(stringId));
    }
    ProgressDialog dialog ;
    protected void showLoadDialog(String msg){
        if(dialog == null){
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage(msg);
        dialog.show();
    }


    protected void dismissDialogDelay(String msg  ){
        if(dialog != null){
            dialog.setMessage(msg);

            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            } , 700);
        }
    }

    protected void dismissDialogDelay(int stringId  ){
        dismissDialogDelay(getString(stringId ));
    }



    protected void showLoadDialog(int stringId){
        showLoadDialog(getString(stringId));
    }

    protected  void dismissDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
