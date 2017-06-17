package sudoku.myself.xhc.com.myaccount;

import android.app.Application;
import android.content.Context;

/**
 * Created by xhc on 2016/12/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(this);
//
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);
    }

    public Context getContext(){
        return getApplicationContext();
    }

}
